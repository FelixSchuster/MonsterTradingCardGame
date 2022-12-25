package at.fhtw.mtcg.dal.repository;

import at.fhtw.mtcg.dal.UnitOfWork;
import at.fhtw.mtcg.exception.DataAccessException;
import at.fhtw.mtcg.exception.InsertFailedException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PackageRepository {
    private UnitOfWork unitOfWork;
    public PackageRepository(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
    public int createPackage() {
        try {
            PreparedStatement preparedStatement = this.unitOfWork.prepareStatement("INSERT INTO packages (package_id) VALUES (DEFAULT) RETURNING package_id");
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()) {
                throw new InsertFailedException("Package could not be created");
            }

            return resultSet.getInt("package_id");

        } catch (SQLException e) {
            throw new DataAccessException("DataAccessException in createPackage: " + e);
        }
    }
}
