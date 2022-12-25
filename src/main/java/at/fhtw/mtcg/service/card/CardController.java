package at.fhtw.mtcg.service.card;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.CardRepository;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.UserData;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

public class CardController extends Controller {
    public Response getCards(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            List<Card> cards = cardRepository.getCardsByUserId(userId);
            String cardsJSON = this.getObjectMapper().writeValueAsString(cards);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, cardsJSON);

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

        } catch (JsonProcessingException e) {
            // e.printStackTrace();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
