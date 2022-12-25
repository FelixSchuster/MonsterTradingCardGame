package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
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
}
