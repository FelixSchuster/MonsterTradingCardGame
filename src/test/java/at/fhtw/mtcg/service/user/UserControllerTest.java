package at.fhtw.mtcg.service.user;

import at.fhtw.mtcg.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    @Test
    void testUserServiceGetCompleteList() throws Exception {
        URL url = new URL("http://localhost:10001/user");
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            List<User> userList = new ObjectMapper().readValue(bufferedReader.readLine(), new TypeReference<List<User>>(){});
            assertEquals(3, userList.size());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        bufferedReader.close();
    }
}