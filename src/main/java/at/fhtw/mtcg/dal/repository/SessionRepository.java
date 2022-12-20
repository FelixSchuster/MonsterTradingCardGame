package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InvalidTokenException;
import at.fhtw.mtcg.model.UserCredentials;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionRepository {
    private UnitOfWork unitOfWork;
    public SessionRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public int checkForValidCredentials(UserCredentials userCredentials) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, userCredentials.getUsername());
            preparedStatement.setString(2, userCredentials.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) { // user does not exist
                throw new DataNotFoundException("Invalid username/password provided");
            }

            return resultSet.getInt("user_id");

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("DataAccessException in isValidCredentials: " + e);
        }
    }
    public void saveToken(Integer userId, String token) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO tokens (user_id, token) VALUES (?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, token);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in saveToken: " + e);
        }
    }
    public void checkForValidToken(String username, String token) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM tokens JOIN users ON tokens.user_id = users.user_id WHERE users.username = ? AND tokens.token = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) { // token for this user is invalid
                throw new InvalidTokenException("Authentication information is missing or invalid");
            }

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in isValidToken: " + e);
        }
    }
}