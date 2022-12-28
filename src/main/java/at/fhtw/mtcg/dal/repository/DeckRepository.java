package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeckRepository {
    private UnitOfWork unitOfWork;
    public DeckRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public int createDeck() {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO decks (deck_id) VALUES (DEFAULT) RETURNING deck_id");
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Deck could not be created");
            }

            return resultSet.getInt("deck_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in createDeck: " + e);
        }
    }
    public int getDeckIdByUserId(int userId) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM decks JOIN cards ON decks.deck_id = cards.deck_id JOIN users ON cards.user_id = users.user_id WHERE users.user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new DataNotFoundException("The request was fine, but the deck doesn't have any cards");
            }

            return resultSet.getInt("deck_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in createDeck: " + e);
        }
    }
}
