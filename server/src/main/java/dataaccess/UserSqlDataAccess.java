package dataaccess;


import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLDataException;
import java.sql.SQLException;

public class UserSqlDataAccess implements UserDAO{
    UserData currUser;
    String currJson;
    String hashedPassword;

    public UserSqlDataAccess(){
        try {
            configureDatabase();
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email, json) VALUES (?, ?, ?, ?)";
        this.hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        this.currUser = new UserData(user.username(), hashedPassword, user.email());
        this.currJson = new Gson().toJson(currUser);
        executeUpdate(statement);
        return currUser;
    }

    @Override
    public UserData getUser(UserData user) {
        return null;
    }

    @Override
    public void clear() throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()){
            var stmt = "TRUNCATE TABLE users";
            var ps = conn.prepareStatement(stmt);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw new DataAccessException(String.format("Unable to clear table: %s", ex.getMessage()));
        }

    }


    @Override
    public int size() throws SQLException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "COUNT(*) FROM users";
            var ps = conn.prepareStatement(stmt);
            try (var rs = ps.executeQuery()) {
                return rs.getInt(1);
                }
            }
    }

    private void executeUpdate(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, currUser.username());
                ps.setString(2, hashedPassword);
                ps.setString(3, currUser.email());
                ps.setString(4,currJson);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to add to table: %s", ex.getMessage()));
        }
    }

    private final String[] create = {
            """
            CREATE TABLE IF NOT EXISTS users (
            `username` VARCHAR(50) NOT NULL,
            `password` VARCHAR(500) NOT NULL,
            `email` VARCHAR(50),
            `json` TEXT NOT NULL,
            PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
            //Any changes made to this require you to go in and manually drop the table to have a new table created
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()){
            for (String line : create){
                try (var preppedStatement = conn.prepareStatement(line)){
                    preppedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
