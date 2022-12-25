package at.fhtw.mtcg.service.user;

import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.exception.*;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.model.UserCredentials;
import at.fhtw.mtcg.model.UserData;
import at.fhtw.mtcg.controller.Controller;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
public class UserController extends Controller {

    // get /users/{username}
    public Response getUserData(Request request) {

        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        String username = request.getPathParts().get(1);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            if(userId != userRepository.getUserIdByUsername(username)) { // check if token matches given username
                throw new InvalidTokenException("Authentication information is missing or invalid");
            }

            UserData userData = userRepository.getUserDataByUsername(username);
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, userDataJSON);

        } catch(DataNotFoundException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\":\"User not found.\"}");

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

    // PUT /users/{username}
    public Response updateUserData(Request request) {

        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        String username = request.getPathParts().get(1);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            if(userId != userRepository.getUserIdByUsername(username)) { // check if token matches given username
                throw new InvalidTokenException("Authentication information is missing or invalid");
            }

            UserData userData = this.getObjectMapper().readValue(request.getBody(), UserData.class);
            userRepository.updateUserDataByUsername(username, userData);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\":\"User successfully updated.\"}");

        } catch(DataNotFoundException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\":\"User not found.\"}");

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch (JsonProcessingException e) {
            // e.printStackTrace();

        } catch(DataAccessException e) {
            // e.printStackTrace();

        } catch (Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }

    // POST /users
    public Response createUser(Request request) {

        // TODO: send hashed password

        UnitOfWork unitOfWork = new UnitOfWork();
        UserRepository userRepository = new UserRepository(unitOfWork);

        try {
            UserCredentials userCredentials = this.getObjectMapper().readValue(request.getBody(), UserCredentials.class);
            userRepository.createUser(userCredentials);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.CREATED, ContentType.JSON, "{\"message\":\"User successfully created\"}");

        } catch(PrimaryKeyAlreadyExistsException e) {
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"User with same username already registered\"}");

        } catch (InsertFailedException e) {
            // e.printStackTrace();

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
