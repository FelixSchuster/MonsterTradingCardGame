package at.fhtw.mtcg.service.user;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class UserService implements Service {

    private final UserController userController;

    public UserService() {
        this.userController = new UserController(new UserDAL());
    }

    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            return this.userController.getUser();
        }

        if (request.getMethod() == Method.POST) {
            return this.userController.addUser(request);
        }

        return null;
    }
}
