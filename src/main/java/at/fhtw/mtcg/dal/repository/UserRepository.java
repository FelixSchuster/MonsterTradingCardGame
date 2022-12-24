package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.PrimaryKeyAlreadyExistsException;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.UserCredentials;
import at.fhtw.mtcg.model.UserData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class UserRepository {
    private UnitOfWork unitOfWork;
    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    // get /users/{username}
    public UserData getUserData(String username) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("User not found.");
            }

            String name = resultSet.getString("name");
            String bio = resultSet.getString("bio");
            String image = resultSet.getString("image");

            return new UserData(name, bio, image);
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserData: " + e);
        }
    }
    public int getUserId(String username) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("User not found.");
            }

            int userId = resultSet.getInt("user_id");

            return userId;
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserId: " + e);
        }
    }
    public void updateUserData(String username, UserData userData) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE users SET name = ?, bio = ?, image = ? WHERE username = ?");
            preparedStatement.setString(1, userData.getName());
            preparedStatement.setString(2, userData.getBio());
            preparedStatement.setString(3, userData.getImage());
            preparedStatement.setString(4, username);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updateUserData: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("User not found.");
        }
    }

    // post /users
    public int createUser(UserCredentials userCredentials) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?) RETURNING user_id");
            preparedStatement.setString(1, userCredentials.getUsername());
            preparedStatement.setString(2, userCredentials.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("User could not be created");
            }

            return resultSet.getInt("user_id");
        }

        catch(SQLException e) {
            if(e.getSQLState().startsWith("23")) {
                throw new PrimaryKeyAlreadyExistsException("User with same username already registered");
            }

            throw new DataAccessException("DataAccessException in createUser: " + e);
        }
    }
}
