package at.fhtw.mtcg.service.user;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;
public class UserService implements Service {
    private final UserController userController;
    public UserService() {
        this.userController = new UserController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET && request.getPathParts().size() > 1) {
            return this.userController.getUserData(request);
        }

        if (request.getMethod() == Method.POST) {
            return this.userController.createUser(request);
        }

        if (request.getMethod() == Method.PUT) {
            return this.userController.updateUserData(request);
        }

        return null;
    }
}
