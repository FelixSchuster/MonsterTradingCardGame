package at.fhtw.mtcg.service.deck;

import at.fhtw.server.http.Method;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import at.fhtw.server.server.Service;

public class DeckService implements Service {
    private final DeckController deckController;
    public DeckService() {
        this.deckController = new DeckController();
    }
    @Override
    public Response handleRequest(Request request) {

        if(request.getMethod() == Method.GET && ( request.getParams() == null || request.getParams().equals("format=json") ) ) {
            return this.deckController.getDeckJson(request);
        }

        if(request.getMethod() == Method.GET && request.getParams().equals("format=plain")) {
            return this.deckController.getDeckPlain(request);
        }

        if(request.getMethod() == Method.PUT) {
            return this.deckController.putDeck(request);
        }

        return null;
    }
}
