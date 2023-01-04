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
    public void updatePackageIdByCardId(String cardId, int packageId) {
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
        List<String> cardIds = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE package_id = ?");
            preparedStatement.setInt(1, packageId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Package not found.");
            }

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
    public List<String> getCardIdsByDeckId(int deckId) {
        List<String> cardIds = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE deck_id = ?");
            preparedStatement.setInt(1, deckId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Deck not found.");
            }

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
            throw new DataNotFoundException("Card not found.");
        }
    }
    public List<Card> getCardsByUserId(int userId) {
        List<Card> cards = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("The request was fine, but the user doesn't have any cards");
            }

            String cardId = resultSet.getString("card_id");
            String name = resultSet.getString("name");
            Float damage = resultSet.getFloat("damage");

            cards.add(new Card(cardId, name, damage));

            while(resultSet.next()) {
                cardId = resultSet.getString("card_id");
                name = resultSet.getString("name");
                damage = resultSet.getFloat("damage");

                cards.add(new Card(cardId, name, damage));
            }

            return cards;
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getCardsByUserId: " + e);
        }
    }
    public int getUserIdByCardId(String cardId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE card_id = ?");
            preparedStatement.setString(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Card not found.");
            }

            return resultSet.getInt("user_id");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserIdByCardId: " + e);
        }
    }
    public void updateDeckIdByCardId(String cardId, int deckId) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE cards SET deck_id = ? WHERE card_id = ?");
            preparedStatement.setInt(1, deckId);
            preparedStatement.setString(2, cardId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updateDeckIdByCardId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("Card not found.");
        }
    }
    public void resetDeckIds(int userId, int deckId) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE cards SET deck_id = NULL WHERE user_id = ? AND deck_id != ?");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, deckId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in resetDeckIds: " + e);
        }

        if(numberOfUpdatedRows == 0) { // do nothing to prevent rollback!
            // throw new DataNotFoundException("Card not found.");
        }
    }
    public List<Card> getCardsInDeckByUserId(int userId) {
        List<Card> cards = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE user_id = ? AND deck_id IS NOT NULL");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("The request was fine, but the user doesn't have any cards");
            }

            String cardId = resultSet.getString("card_id");
            String name = resultSet.getString("name");
            Float damage = resultSet.getFloat("damage");

            cards.add(new Card(cardId, name, damage));

            while(resultSet.next()) {
                cardId = resultSet.getString("card_id");
                name = resultSet.getString("name");
                damage = resultSet.getFloat("damage");

                cards.add(new Card(cardId, name, damage));
            }

            return cards;
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getCardsByUserId: " + e);
        }
    }
    public int getAvailablePackageId() {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE user_id IS NULL LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new DataNotFoundException("No card package available for buying");
            }

            return resultSet.getInt("package_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in getAvailablePackageId: " + e);
        }
    }
    public Card getCardByCardId(String cardId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE card_id = ?");
            preparedStatement.setString(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Card not found.");
            }

            String name = resultSet.getString("name");
            Float damage = resultSet.getFloat("damage");

            return new Card(cardId, name, damage);
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getCardByCardId: " + e);
        }
    }
    public int getUserIdByDeckId(int deckId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE deck_id = ?");
            preparedStatement.setInt(1, deckId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Deck not found.");
            }

            return resultSet.getInt("user_id");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getUserIdByDeckId: " + e);
        }
    }
    public int getDeckIdByCardId(String cardId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE card_id = ?");
            preparedStatement.setString(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("Card not found.");
            }

            return resultSet.getInt("deck_id");
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getDeckIdByCardId: " + e);
        }
    }
    public void updateTradingDealIdByCardId(String cardId, String tradingDealId) {
        int numberOfUpdatedRows = 0;

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("UPDATE cards SET trading_deal_id = ? WHERE card_id = ?");
            preparedStatement.setString(1, tradingDealId);
            preparedStatement.setString(2, cardId);
            numberOfUpdatedRows = preparedStatement.executeUpdate();
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in updateTradingDealIdByCardId: " + e);
        }

        if(numberOfUpdatedRows == 0) {
            throw new DataNotFoundException("Card not found.");
        }
    }
}
