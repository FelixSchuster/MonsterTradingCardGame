package at.fhtw.mtcg.service._package;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.CardRepository;
import at.fhtw.mtcg.dal.repository.PackageRepository;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.exception.ForbiddenException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.exception.PrimaryKeyAlreadyExistsException;
import at.fhtw.mtcg.model.Card;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.List;

public class PackageController extends Controller {
    public Response createPackage(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        PackageRepository packageRepository = new PackageRepository(unitOfWork);
        CardRepository cardRepository = new CardRepository(unitOfWork);
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        UserRepository userRepository = new UserRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            if(userId != userRepository.getUserIdByUsername("admin")) { // check if token matches given username
                throw new ForbiddenException("Provided user is not 'admin'");
            }

            Card cards[] = this.getObjectMapper().readValue(request.getBody(), Card[].class);

            int packageId = packageRepository.createPackage(); // create package

            List<String> cardIds = new ArrayList<>();

            for (Card card : cards) { // create Cards
                String cardId = cardRepository.createCard(card);
                System.out.println(cardId);
                cardIds.add(cardId);
            }

            for(String cardId : cardIds) { // update packageId for newly created cards
                cardRepository.updatePackageId(cardId, packageId);
            }

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\":\"Package and cards successfully created\"");

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(PrimaryKeyAlreadyExistsException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.CONFLICT, ContentType.JSON, "{\"message\":\"At least one card in the packages already exists\"}");

        } catch(ForbiddenException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.FORBIDDEN, ContentType.JSON, "{\"message\":\"Provided user is not 'admin'\"}");

        } catch (InsertFailedException e) {
            // e.printStackTrace();

        } catch (JsonProcessingException e) {
            // e.printStackTrace();

        }  catch (Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
