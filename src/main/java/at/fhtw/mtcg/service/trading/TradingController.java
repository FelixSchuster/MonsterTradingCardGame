package at.fhtw.mtcg.service.trading;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.CardRepository;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.dal.repository.TradingRepository;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InvalidCardException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.model.Card;
import at.fhtw.mtcg.model.TradingDeal;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class TradingController extends Controller {
    public Response getTradingDeals(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        TradingRepository tradingRepository = new TradingRepository(unitOfWork);

        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            sessionRepository.checkForValidToken(token); // check for valid token

            List<TradingDeal> tradingDeals = tradingRepository.getTradingDeals();

            String tradingDealsJson = this.getObjectMapper().writeValueAsString(tradingDeals);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, tradingDealsJson);

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(DataNotFoundException e){
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NO_CONTENT, ContentType.JSON, "{\"message\":\"The request was fine, but there are no trading deals available\"}");

        } catch(JsonProcessingException e) {
            // e.printStackTrace();

        } catch(Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
    public Response createTradingDeal(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        TradingRepository tradingRepository = new TradingRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);

        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            TradingDeal tradingDeal = this.getObjectMapper().readValue(request.getBody(), TradingDeal.class);
            String cardId = tradingDeal.getCardToTrade();

            String tradingDealId = tradingRepository.createTradingDeal(tradingDeal);

            // TODO: fix the bugs

            if(!(userId == cardRepository.getUserIdByCardId(cardId) && cardRepository.getDeckIdByCardId(cardId) == null)) {
                throw new InvalidCardException("The deal contains a card that is not owned by the user or locked in the deck.");
            }

            cardRepository.updateTradingDealIdByCardId(tradingDeal.getCardToTrade(), tradingDealId);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\":\"Trading deal successfully created\"}");

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(InvalidCardException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{\"message\":\"The deal contains a card that is not owned by the user or locked in the deck.\"}");

        } catch(JsonProcessingException e) {
            // e.printStackTrace();

        } catch(Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
    public Response deleteTradingDeal(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        TradingRepository tradingRepository = new TradingRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);

        String tradingDealId =  request.getPathParts().get(1);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            // TODO: finish implementation


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
