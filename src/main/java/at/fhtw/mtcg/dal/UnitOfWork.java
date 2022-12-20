package at.fhtw.mtcg.dal;

import at.fhtw.mtcg.exception.DataAccessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class UnitOfWork implements AutoCloseable {
    private Connection connection;
    public UnitOfWork() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DataAccessException("Unable to disable autocommit: ", e);
        }
    }
    @Override
    public void close() {
        this.finishWork();
    }
    public void commitTransaction() {
        if (this.connection != null) {
            try {
                this.connection.commit();
            } catch (SQLException e) {
                throw new DataAccessException("Unable to commit transaction: ", e);
            }
        }
    }
    public void rollbackTransaction() {
        if (this.connection != null) {
            try {
                this.connection.rollback();
            } catch (SQLException e) {
                throw new DataAccessException("Unable to rollback transaction: ", e);
            }
        }
    }
    public void finishWork() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException e) {
                throw new DataAccessException("Unable to close connection: ", e);
            }
        }
    }
    public PreparedStatement prepareStatement(String statement) {
        if (this.connection != null) {
            try {
                return this.connection.prepareStatement(statement);
            } catch (SQLException e) {
                throw new DataAccessException("Unable to create prepared statement: ", e);
            }
        }

        throw new DataAccessException("No active connection available.");
    }
}
