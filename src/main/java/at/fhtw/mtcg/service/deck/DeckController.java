package at.fhtw.mtcg.service.deck;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.CardRepository;
import at.fhtw.mtcg.dal.repository.DeckRepository;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.exception.*;
import at.fhtw.mtcg.model.Card;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class DeckController extends Controller {
    public Response putDeck(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        DeckRepository deckRepository = new DeckRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);

        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token
            int deckId = deckRepository.createDeck(); // create new deck

            String cardIds[] = this.getObjectMapper().readValue(request.getBody(), String[].class);

            if(cardIds.length != 4) {
                throw new InvalidCardAmountException("The provided deck did not include the required amount of cards");
            }

            for(String cardId : cardIds) {
                if(userId != cardRepository.getUserIdByCardId(cardId)) {
                    throw new InvalidCardException("At least one of the provided cards does not belong to the user or is not available.");
                }
                cardRepository.updateDeckIdByCardId(cardId, deckId);
            }

            cardRepository.resetDeckIds(userId, deckId);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\":\"The deck has been successfully configured\"}");

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(InvalidCardException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{\"message\":\"At least one of the provided cards does not belong to the user or is not available.\"}");

        } catch(InvalidCardAmountException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.BAD_REQUEST, ContentType.JSON, "{\"message\":\"The provided deck did not include the required amount of cards\"}");

        } catch(DataAccessException e) {
            // e.printStackTrace();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
    public Response getDeckJson(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            List<Card> cards = cardRepository.getCardsInDeckByUserId(userId);
            String cardsJSON = this.getObjectMapper().writeValueAsString(cards);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, cardsJSON);

        } catch(DataNotFoundException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\":\"The request was fine, but the user doesn't have any cards\"}");

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(DataAccessException e) {
            // e.printStackTrace();

        } catch (JsonProcessingException e) {
            // e.printStackTrace();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
    public Response getDeckPlain(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            List<Card> cards = cardRepository.getCardsInDeckByUserId(userId);
            String cardsPlain = "";
            for(Card card : cards) {
                cardsPlain += card.getId() + " - " + card.getName() + " - " + card.getDamage() + "\n";
            }

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, cardsPlain);

        } catch(DataNotFoundException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\":\"The request was fine, but the user doesn't have any cards\"}");

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(DataAccessException e) {
            // e.printStackTrace();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
