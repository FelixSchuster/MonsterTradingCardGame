package at.fhtw.mtcg.service.session;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class SessionService implements Service {
    private final SessionController sessionController;
    public SessionService() {
        this.sessionController = new SessionController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST) {
            return this.sessionController.login(request);
        }

        return null;
    }
}
