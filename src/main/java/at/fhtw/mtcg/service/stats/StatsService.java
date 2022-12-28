package at.fhtw.mtcg.service.stats;

import at.fhtw.mtcg.service.session.SessionController;
import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class StatsService implements Service {
    private final StatsController statsController;
    public StatsService() {
        this.statsController = new StatsController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            return this.statsController.getStats(request);
        }

        return null;
    }
}
