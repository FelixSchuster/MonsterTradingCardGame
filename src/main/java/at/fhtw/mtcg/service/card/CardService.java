package at.fhtw.mtcg.service.card;

import at.fhtw.mtcg.service.session.SessionController;
import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class CardService implements Service {
    private final CardController cardController;
    public CardService() {
        this.cardController = new CardController();
    }
    @Override
    public Response handleRequest(Request request) {

        if (request.getMethod() == Method.GET) {
            return this.cardController.getCards(request);
        }

        return null;
    }
}
