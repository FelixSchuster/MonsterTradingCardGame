package at.fhtw.mtcg.service.battlelog;

import at.fhtw.mtcg.controller.Controller;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.dal.repository.BattlelogRepository;
import at.fhtw.mtcg.dal.repository.SessionRepository;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;

public class BattlelogController extends Controller {
    public Response getBattlelog(Request request) {
        UnitOfWork unitOfWork = new UnitOfWork();
        SessionRepository sessionRepository = new SessionRepository(unitOfWork);
        BattlelogRepository battlelogRepository = new BattlelogRepository(unitOfWork);
        String token = request.getHeaderMap().getAuthorizationTokenHeader();

        try {
            int userId = sessionRepository.checkForValidToken(token); // check for valid token

            String battlelog = battlelogRepository.getLastBattleLogByUserId(userId);

            unitOfWork.commitTransaction();
            return new Response(HttpStatus.OK, ContentType.PLAIN_TEXT, battlelog);

        } catch(DataNotFoundException e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.NOT_FOUND, ContentType.JSON, "{\"message\":\"The request was fine, but the user has not battled yet\"}");

        } catch(InvalidTokenException e) {
            e.printStackTrace();
            unitOfWork.rollbackTransaction();
            return new Response(HttpStatus.UNAUTHORIZED, ContentType.JSON, "{\"message\":\"Authentication information is missing or invalid\"}");

        } catch(DataAccessException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }

        unitOfWork.rollbackTransaction();
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.JSON, "{\"message\":\"Internal Server Error\"}");
    }
}
