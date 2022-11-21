package at.fhtw.mtcg.service.user;

import at.fhtw.mtcg.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAL {
    private List<User> userData;

    public UserDAL() {
        userData = new ArrayList<>();
        userData.add(new User(1,"Felix", "password", 20, 100));
        userData.add(new User(2,"Bea", "password", 20, 100));
        userData.add(new User(3,"Jovana", "password", 20, 100));
    }

    // get /user
    public List<User> getUser() throws SQLException {

        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/MonsterTradingCardGame", "postgres", "1aAdOX_.((DVTYujopHbe)rs*2nirc");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users");
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            while(resultSet.next())
            {
                System.out.println(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userData;
    }

    // post /user
    public void addUser(User user) {
        userData.add(user);
    }
}
