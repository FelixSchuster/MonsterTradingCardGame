package at.fhtw.mtcg.service.trading;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class TradingService implements Service {
    private final TradingController tradingController;
    public TradingService() {
        this.tradingController = new TradingController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            return this.tradingController.getTradingDeals(request);
        }

        if (request.getMethod() == Method.POST) {
            return this.tradingController.createTradingDeal(request);
        }

        if (request.getMethod() == Method.DELETE && request.getPathParts().size() > 1) {
            return this.tradingController.deleteTradingDeal(request);
        }

        return null;
    }
}
