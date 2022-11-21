package at.fhtw.mtcg.service.user;

import at.fhtw.mtcg.model.User;
import at.fhtw.sampleapp.controller.Controller;
import at.fhtw.sampleapp.model.Weather;
import at.fhtw.server.http.ContentType;
import at.fhtw.server.http.HttpStatus;
import at.fhtw.server.server.Request;
import at.fhtw.server.server.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.sql.SQLException;
import java.util.List;

public class UserController extends Controller {

    private UserDAL userDAL;

    public UserController(UserDAL userDAL) {
        this.userDAL = userDAL;
    }

    // get /user
    public Response getUser() {
        try {
            List userData = this.userDAL.getUser();
            String userDataJSON = this.getObjectMapper().writeValueAsString(userData);

            return new Response(
                    HttpStatus.OK,
                    ContentType.JSON,
                    userDataJSON
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new Response(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ContentType.JSON,
                    "{ \"message\" : \"Internal Server Error\" }"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // POST /user
    public Response addUser(Request request) {
        try {
            User user = this.getObjectMapper().readValue(request.getBody(), User.class);
            this.userDAL.addUser(user);

            return new Response(
                    HttpStatus.CREATED,
                    ContentType.JSON,
                    "{ message: \"Success\" }"
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new Response(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ContentType.JSON,
                "{ \"message\" : \"Internal Server Error\" }"
        );
    }
}
