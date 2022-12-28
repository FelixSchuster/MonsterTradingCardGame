package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;
import at.fhtw.mtcg.exception.InsertFailedException;
import at.fhtw.mtcg.exception.PrimaryKeyAlreadyExistsException;
import at.fhtw.mtcg.model.TradingDeal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TradingRepository {
    private UnitOfWork unitOfWork;
    public TradingRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public List<TradingDeal> getTradingDeals() {
        List<TradingDeal> tradingDeals = new ArrayList<>();

        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards JOIN trading_deals ON cards.trading_deal_id = cards.trading_deal_id");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DataNotFoundException("The request was fine, but there are no trading deals available");
            }

            String tradingDealId = resultSet.getString("trading_deal_id");
            String cardToTrade = resultSet.getString("card_id");
            String type = resultSet.getString("type");
            Float minimumDamage = resultSet.getFloat("minimum_damage");

            tradingDeals.add(new TradingDeal(tradingDealId, cardToTrade, type, minimumDamage));

            while(resultSet.next()) {
                tradingDealId = resultSet.getString("trading_deal_id");
                cardToTrade = resultSet.getString("card_id");
                type = resultSet.getString("type");
                minimumDamage = resultSet.getFloat("minimum_damage");

                tradingDeals.add(new TradingDeal(tradingDealId, cardToTrade, type, minimumDamage));
            }

            return tradingDeals;
        }

        catch(SQLException e) {
            throw new DataAccessException("DataAccessException in getTradingDeals: " + e);
        }
    }
    public String createTradingDeal(TradingDeal tradingDeal) {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO trading_deals (trading_deal_id, type, minimum_damage) VALUES (?, ?, ?) RETURNING trading_deal_id");
            preparedStatement.setString(1, tradingDeal.getId());
            preparedStatement.setString(2, tradingDeal.getType());
            preparedStatement.setFloat(3, tradingDeal.getMinimumDamage());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Trading deal could not be created");
            }

            return resultSet.getString("trading_deal_id");

        } catch(SQLException e){
            if (e.getSQLState().startsWith("23")) {
                throw new PrimaryKeyAlreadyExistsException("A deal with this deal ID already exists.");
            }

            throw new DataAccessException("DataAccessException in createTradingDeal: " + e);
        }
    }
}
