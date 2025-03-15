package dataaccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                var nextStatement = conn.createStatement();
                String triggerSQL = finalTrigger[0];
                nextStatement.execute(triggerSQL);
                var lastStatement = conn.createStatement();
                String triggersql = invalidIDtrigger[0];
                lastStatement.execute(triggersql);
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static final String[] finalTrigger = {
            """
            CREATE TRIGGER no_update_notNull_column
            BEFORE UPDATE ON games
            FOR EACH ROW
            BEGIN
                IF OLD.white IS NOT NULL AND NEW.white IS NOT NULL AND OLD.white != NEW.white THEN
                    SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'Cannot modify white column after initial setting.';
                ELSEIF OLD.black IS NOT NULL AND NEW.black IS NOT NULL AND OLD.black != NEW.black THEN
                    SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'Cannot modify black column after initial setting.';
                END IF;
            END;
            """
    };

    static final String[] invalidIDtrigger = {
            """
            CREATE TRIGGER check_for_id
            BEFORE UPDATE ON games
            FOR EACH ROW
            BEGIN
                IF (SELECT COUNT(*) FROM games WHERE gameID = OLD.gameID) = 0 THEN
                    SIGNAL SQLSTATE '45000'
                    SET MESSAGE_TEXT = 'No game with that gameID.';
                END IF;
            END;
            """
    };

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
