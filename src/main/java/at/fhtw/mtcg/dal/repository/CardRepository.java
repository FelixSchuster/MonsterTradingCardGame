package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.PrimaryKeyAlreadyExistsException;
import at.fhtw.mtcg.model.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardRepository {
    private UnitOfWork unitOfWork;
    public CardRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public String createCard(Card card) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO cards (card_id, name, damage) VALUES (?, ?, ?) RETURNING card_id");
            preparedStatement.setString(1, card.getId());
            preparedStatement.setString(2, card.getName());
            preparedStatement.setFloat(3, card.getDamage());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Card could not be created");
            }

            return resultSet.getString("card_id");

        } catch(SQLException e){
            if (e.getSQLState().startsWith("23")) {
                throw new PrimaryKeyAlreadyExistsException("At least one card in the packages already exists");
            }

            throw new DataAccessException("DataAccessException in createUser: " + e);
        }
    }
    public void updatePackageId(String cardId, int packageId) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE cards SET package_id = ? WHERE card_id = ?");
            preparedStatement.setInt(1, packageId);
            preparedStatement.setString(2, cardId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updatePackageId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("Package not found.");
        }
    }
    public List<String> getCardIdsByPackageId(int packageId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE package_id = ?");
            preparedStatement.setInt(1, packageId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Package not found.");
            }

            List<String> cardIds = new ArrayList<>();
            cardIds.add(resultSet.getString("card_id"));

            while(resultSet.next()) {
                cardIds.add(resultSet.getString("card_id"));
            }

            return cardIds;
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getCardIdsByPackageId: " + e);
        }
    }
    public void updateUserIdByCardId(String cardId, int userId) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE cards SET user_id = ? WHERE card_id = ?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, cardId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updatePackageId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("Package not found.");
        }
    }
}
