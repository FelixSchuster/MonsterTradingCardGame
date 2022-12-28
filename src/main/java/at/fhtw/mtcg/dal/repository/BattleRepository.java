package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.NoRunningBattleException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattleRepository {
    private UnitOfWork unitOfWork;
    public BattleRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public int getRunningBattle() {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM battles WHERE deck_2_id IS NULL LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new NoRunningBattleException("No battle found.");
            }

            return resultSet.getInt("battle_id");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getRunningBattle: " + e);
        }
    }
    public int createBattle(int userId, int deckId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO battles (user_1_id, deck_1_id) VALUES (?, ?) RETURNING battle_id");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, deckId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Battle could not be created");
            }

            return resultSet.getInt("battle_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in createBattle: " + e);
        }
    }
    public void updateUser2IdAndDeck2IdByBattleId(int userId, int battleId, int deck2Id) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE battles SET user_2_id = ?, deck_2_id = ? WHERE battle_id = ?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, deck2Id);
            preparedStatement.setInt(3, battleId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updateUser2IdAndDeck2IdByBattleId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("Battle not found.");
        }
    }
    public int getDeck1IdByBattleId(int battleId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM battles WHERE battle_id = ?");
            preparedStatement.setInt(1, battleId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new NoRunningBattleException("Battle not found.");
            }

            return resultSet.getInt("deck_1_id");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getDeck1IdByBattleId: " + e);
        }
    }

}
