package at.fhtw.mtcg.service.session;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.model.UserCredentials;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

public class SessionController extends Controller {
    public Response login(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);

        try {
            UserCredentials userCredentials = this.getObjectMapper().readValue(request.getBody(), UserCredentials.class);
            String token = userCredentials.getUsername() + "-mtcgToken";
            int userId = sessionRepository.checkForValidCredentials(userCredentials);
            sessionRepository.createToken(userId, token);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, "{\"token\":\"" + token + "\"}");

        } catch (DataNotFoundException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Invalid username/password provided\"}");

        } catch(InsertFailedException e) {
            // e.printStackTrace();

        } catch (DataAccessException e) {
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
