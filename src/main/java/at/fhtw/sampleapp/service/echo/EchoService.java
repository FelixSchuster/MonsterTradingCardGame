package at.fhtw.sampleapp.service.echo;

import at.fhtw.sampleserver.http.ContentType;
import at.fhtw.sampleserver.http.HttpStatus;
import at.fhtw.sampleserver.server.Request;
import at.fhtw.sampleserver.server.Response;
import at.fhtw.sampleserver.server.Service;

public class EchoService implements Service {
    @Override
    public Response handleRequest(Request request) {
        return new Response(HttpStatus.OK,
                            ContentType.PLAIN_TEXT,
                     "Echo-" + request.getBody());
    }
}
