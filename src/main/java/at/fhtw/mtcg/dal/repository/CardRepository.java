package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.PrimaryKeyAlreadyExistsException;
import at.fhtw.mtcg.model.Card;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
