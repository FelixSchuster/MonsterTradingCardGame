package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.PrimaryKeyAlreadyExistsException;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.model.UserCredentials;
import at.fhtw.mtcg.model.UserData;
import at.fhtw.mtcg.model.UserStats;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private UnitOfWork unitOfWork;
    public UserRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public UserData getUserDataByUsername(String username) {
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
            throw new DataAccessException("DataAccessException in getUserDataByUsername: " + e);
        }
    }
    public UserData getUserDataByUserId(int userId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
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
            throw new DataAccessException("DataAccessException in getUserDataByUsername: " + e);
        }
    }
    public int getUserIdByUsername(String username) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("User not found.");
            }

            return resultSet.getInt("user_id");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserId: " + e);
        }
    }
    public void updateUserDataByUsername(String username, UserData userData) {
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
    public int getCoinsByUserId(int userId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("User not found.");
            }

            return resultSet.getInt("coins");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getCoinsByUserId: " + e);
        }
    }
    public void updateCoinsByUserId(int userId, int coins) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE users SET coins = ? WHERE user_id = ?");
            preparedStatement.setInt(1, coins);
            preparedStatement.setInt(2, userId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updateCoinsByUserId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("User not found.");
        }
    }
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
    public UserStats getUserStatsByUserId(int userId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("User not found.");
            }

            String name = resultSet.getString("name");
            int elo = resultSet.getInt("elo");
            int wins = resultSet.getInt("wins");
            int losses = resultSet.getInt("losses");

            return new UserStats(name, elo, wins, losses);
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserStatsByUserId: " + e);
        }
    }
    public void updateUserStatsByUserId(int userId, UserStats userStats) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE users SET name = ?, elo = ?, wins = ?, losses = ? WHERE user_id = ?");
            preparedStatement.setString(1, userStats.getName());
            preparedStatement.setInt(2, userStats.getElo());
            preparedStatement.setInt(3, userStats.getWins());
            preparedStatement.setInt(4, userStats.getLosses());
            preparedStatement.setInt(5, userId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updateUserStatsByUserId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("User not found.");
        }
    }
    public List<UserStats> getUserStats() {
        List<UserStats> userStats = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM users ORDER BY elo DESC");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("No Users found.");
            }

            String name = resultSet.getString("name");
            int elo = resultSet.getInt("elo");
            int wins = resultSet.getInt("wins");
            int losses = resultSet.getInt("losses");

            userStats.add(new UserStats(name, elo, wins, losses));

            while(resultSet.next()) {
                name = resultSet.getString("name");
                elo = resultSet.getInt("elo");
                wins = resultSet.getInt("wins");
                losses = resultSet.getInt("losses");

                userStats.add(new UserStats(name, elo, wins, losses));
            }

            return userStats;
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserStats: " + e);
        }
    }
}
