package at.fhtw.mtcg.service.battlelog;

import at.fhtw.mtcg.service.card.CardController;
import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class BattlelogService implements Service {
    private final BattlelogController battlelogController;
    public BattlelogService() {
        this.battlelogController = new BattlelogController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            return this.battlelogController.getBattlelog(request);
        }

        return null;
    }
}
