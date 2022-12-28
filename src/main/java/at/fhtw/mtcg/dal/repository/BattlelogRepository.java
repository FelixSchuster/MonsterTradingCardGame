package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.NoRunningBattleException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BattlelogRepository {
    private UnitOfWork unitOfWork;
    public BattlelogRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public int createBattlelog(int battleId, String battlelog) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO battle_logs (battle_id, log) VALUES (?, ?) RETURNING battle_log_id");
            preparedStatement.setInt(1, battleId);
            preparedStatement.setString(2, battlelog);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Battlelog could not be created");
            }

            return resultSet.getInt("battle_log_id");

        } catch(SQLException e){
            throw new DataAccessException("DataAccessException in createBattlelog: " + e);
        }
    }
    public String getLastBattleLogByUserId(int userId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM battles JOIN battle_logs ON battles.battle_id = battle_logs.battle_id WHERE user_1_id = ? OR user_2_id = ? ORDER BY battle_logs.battle_id DESC");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new NoRunningBattleException("Battle not found.");
            }

            return resultSet.getString("log");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getLastBattleLogByUserId: " + e);
        }
    }
}
