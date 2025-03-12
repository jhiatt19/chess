package dataaccess;


import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;

import java.sql.SQLDataException;
import java.sql.SQLException;

public class UserSqlDataAccess implements UserDAO{

    public UserSqlDataAccess() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(user);
        executeUpdate(statement, user, json);
        return new UserData(user.username(), user.password(), user.email());
    }

    @Override
    public UserData getUser(UserData user) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return 0;
    }

    private void executeUpdate(String statement, UserData user, String json) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                ps.setString(2, user.password());
                ps.setString(3, user.email());
                ps.setString(4,json);
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
            `password` VARCHAR(50) NOT NULL,
            `email` VARCHAR(50),
            PRIMARY KEY (username)
            );
            """
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
