package Managers;

import java.sql.*;

import static Managers.ConnectionManager.logger;

/**
 * Database manager class that handles database connections and operations.
 * Provides centralized database access for the application.
 *
 * @author sh_ub
 */
public class DbManager {
    public static Connection connection;
    private DbRoutesManager dbRoutesManager;
    private DbUsersManager dbUsersManager;

    public DbManager() {
        dbRoutesManager =new DbRoutesManager();
        dbUsersManager = new DbUsersManager();
    }
    /**
     * Establishes a connection to the PostgreSQL database.
     * Uses predefined connection parameters for the database.
     * If connection fails, the application will exit.
     *
     * @author sh_ub
     */
    public void connect() {
        try {
            String DB_URL = "jdbc:postgresql://localhost:5432/studs";
            String DB_USER = "";
            String DB_PASSWORD = "";
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            logger.info("Successfully connected to database: {}!", connection.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            logger.error("Failed to connect to database!");
            System.exit(1);
        }
    }

    public DbRoutesManager getDbRoutesManager() {
        return dbRoutesManager;
    }

    public DbUsersManager getDbUsersManager() {
        return dbUsersManager;
    }
}
