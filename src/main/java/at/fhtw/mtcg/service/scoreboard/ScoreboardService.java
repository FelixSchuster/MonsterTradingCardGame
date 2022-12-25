package at.fhtw.mtcg.service.scoreboard;

import at.fhtw.mtcg.service.stats.StatsController;
import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class ScoreboardService implements Service {
    private final ScoreboardController scoreboardController;
    public ScoreboardService() {
        this.scoreboardController = new ScoreboardController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            return this.scoreboardController.getScoreboard(request);
        }

        return null;
    }
}
