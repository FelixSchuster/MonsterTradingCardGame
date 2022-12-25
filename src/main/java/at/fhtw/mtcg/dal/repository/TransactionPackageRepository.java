package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.DataNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionPackageRepository {
    private UnitOfWork unitOfWork;
    public TransactionPackageRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public int getAvailablePackageId() {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("SELECT * FROM cards WHERE user_id IS NULL LIMIT 1");
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) { // user does not exist
                throw new DataNotFoundException("No card package available for buying");
            }

            return resultSet.getInt("package_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in getAvailablePackageId: " + e);
        }
    }
}
