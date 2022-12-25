package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.*;
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

            if(!resultSet.next()) {
                throw new InvalidCredentialsException("Invalid username/password provided");
            }

            return resultSet.getInt("user_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in checkForValidCredentials: " + e);
        }
    }
    public int createToken(Integer userId, String token) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO tokens (user_id, token) VALUES (?, ?) RETURNING token_id");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, token);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Token could not be created");
            }

            return resultSet.getInt("token_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in createToken: " + e);
        }
    }
    public int checkForValidToken(String token) {
        if(token == null) { // token is missing
            throw new InvalidTokenException("Authentication information is missing or invalid");
        }

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM tokens JOIN users ON tokens.user_id = users.user_id WHERE tokens.token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) { // token for this user is invalid
                throw new InvalidTokenException("Authentication information is missing or invalid");
            }

            return resultSet.getInt("user_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in checkForValidToken: " + e);
        }
    }
}
