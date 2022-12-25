package at.fhtw.mtcg.service.transaction;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.CardRepository;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.dal.repository.TransactionPackageRepository;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.exception.NotEnoughCoinsException;
import at.fhtw.mtcg.model.User;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;

import java.util.List;

public class TransactionController extends Controller {
    public Response acquirePackage(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        TransactionPackageRepository transactionPackageRepository = new TransactionPackageRepository(unitOfWork);
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);
        UserRepository userRepository = new UserRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token
            int packageId = transactionPackageRepository.getAvailablePackageId(); // receive a packageId if available

            List<String> cardIds = cardRepository.getCardIdsByPackageId(packageId); // get cardIds for this package

            for(String cardId : cardIds) {
                cardRepository.updateUserIdByCardId(cardId, userId); // update userId for every cardId
            }

            int coins = userRepository.getCoinsByUserId(userId); // get current coins

            coins -= 5;

            if(coins < 0) {
                throw new NotEnoughCoinsException("Not enough money for buying a card package");
            }

            userRepository.updateCoinsByUserId(userId, coins); // update coins

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, "{\"message\":\"A package has been successfully bought\"}");

        } catch (DataNotFoundException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\":\"No card package available for buying\"}");

        } catch(InvalidTokenException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(NotEnoughCoinsException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{\"message\":\"Not enough money for buying a card package\"}");

        } catch (Exception e) {
            e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
