package at.fhtw.mtcg.service.transaction;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class TransactionService implements Service {
    private final TransactionController transactionPackageController;
    public TransactionService() {
        this.transactionPackageController = new TransactionController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.POST && request.getPathParts().size() > 0 && request.getPathParts().get(1).equals("packages")) {
            return this.transactionPackageController.acquirePackage(request);
        }

        return null;
    }
}
