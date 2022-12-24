package at.fhtw.mtcg.service._package;

import at.fhtw.mtcg.service.session.SessionController;
import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class PackageService implements Service {
    private final PackageController packageController;
    public PackageService() {
        this.packageController = new PackageController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST) {
            return this.packageController.createPackage(request);
        }

        return null;
    }
}
