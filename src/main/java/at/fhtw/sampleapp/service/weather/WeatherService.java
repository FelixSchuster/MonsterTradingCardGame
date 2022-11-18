package at.fhtw.sampleapp.service.weather;

import at.fhtw.sampleserver.http.ContentType;
import at.fhtw.sampleserver.http.HttpStatus;
import at.fhtw.sampleserver.http.Method;
import at.fhtw.sampleserver.server.Request;
import at.fhtw.sampleserver.server.Response;
import at.fhtw.sampleserver.server.Service;

public class WeatherService implements Service {
    private final WeatherController weatherController;

    public WeatherService() {
        this.weatherController = new WeatherController(new WeatherDAL());
    }

    @Override
    public Response handleRequest(Request request) {
        if (request.getMethod() == Method.GET &&
            request.getPathParts().size() > 1) {
            return this.weatherController.getWeather(request.getPathParts().get(1));
        } else if (request.getMethod() == Method.GET) {
            return this.weatherController.getWeather();
        } else if (request.getMethod() == Method.POST) {
            return this.weatherController.addWeather(request);
        }

        return new Response(
                HttpStatus.BAD_REQUEST,
                ContentType.JSON,
                "[]"
        );
    }
}
