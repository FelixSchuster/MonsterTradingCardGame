package at.fhtw.mtcg.dal;

import at.fhtw.mtcg.exception.DataAccessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection {
    private static final DatabaseConnection databaseConnection = new DatabaseConnection();
    public static DatabaseConnection getInstance() {
        return databaseConnection;
    }
    public Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/MonsterTradingCardGame", "postgres", "1aAdOX_.((DVTYujopHbe)rs*2nirc");
        } catch (SQLException e) {
            throw new DataAccessException("[!] Unable to connect to database:", e);
        }
    }
}
