package at.fhtw;

import at.fhtw.mtcg.service.session.SessionService;
import at.fhtw.mtcg.service.user.UserService;
import at.fhtw.server.utils.Router;
import at.fhtw.server.server.Server;
import at.fhtw.sampleapp.service.echo.EchoService;
import at.fhtw.sampleapp.service.weather.WeatherService;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(10001, configureRouter());
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static Router configureRouter() {
        Router router = new Router();
        router.addService("/weather", new WeatherService());
        router.addService("/echo", new EchoService());
        router.addService("/users", new UserService());
        router.addService("/sessions", new SessionService());

        return router;
    }
}
