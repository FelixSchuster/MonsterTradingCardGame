package at.fhtw.mtcg.service.battle;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class BattleService implements Service {
    private final BattleController battleController;
    public BattleService() {
        this.battleController = new BattleController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST) {
            return this.battleController.battle(request);
        }

        return null;
    }
}
