package at.fhtw.mtcg.service.scoreboard;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.dal.repository.UserRepository;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.model.UserStats;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class ScoreboardController extends Controller {
    public Response getScoreboard(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        UserRepository userRepository = new UserRepository(unitOfWork);

        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            sessionRepository.checkForValidToken(token); // check for valid token

            List<UserStats> userStats = userRepository.getUserStats();
            String userStatsJson = this.getObjectMapper().writeValueAsString(userStats);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.JSON, userStatsJson);

        } catch(InvalidTokenException e) {
            // e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(JsonProcessingException e) {
            // e.printStackTrace();

        } catch(Exception e) {
            // e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
