package dataaccess;


import exception.ResponseException;
import model.UserData;

import java.sql.SQLDataException;
import java.sql.SQLException;

public class UserSqlDataAccess implements UserDAO{

    public UserSqlDataAccess() {
        configureDatabase();
    }
    @Override
    public UserData createUser(UserData user) {
        return null;
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

    private final String[] create = {

    }

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
