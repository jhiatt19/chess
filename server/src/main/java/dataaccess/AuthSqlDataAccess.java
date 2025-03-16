package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

public class AuthSqlDataAccess implements AuthDAO{
    AuthData currAuth;
    String currJson;

    public AuthSqlDataAccess() {
        try {
            configureDatabase();
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public AuthData setAuth(AuthData authdata) throws ResponseException, DataAccessException {
        try {
            var statement = "INSERT INTO auth (authToken, username, json) VALUES (?, ?, ?)";
            this.currAuth = new AuthData(authdata.authToken(), authdata.username());
            this.currJson = new Gson().toJson(currAuth);
            executeUpdate(statement);
            return currAuth;
        }
        catch (DataAccessException ex) {
            throw new DataAccessException(String.format("Unable to add auth to table: %s", ex.getMessage()));
        }
    }

    @Override
    public AuthData checkAuth(String token) throws ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT json FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(stmt)) {
                ps.setString(1,token);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var jsonString = rs.getString(1);
                        return new Gson().fromJson(jsonString, AuthData.class);
                    }
                    else {
                        throw new DataAccessException(String.format("Authtoken provided not found: %s"));
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to find user: %s", e.getMessage()));
        }
    }

    @Override
    public void deleteAuth(String token) throws ResponseException, DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "DELETE FROM auth WHERE authToken=?";
        var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,token);
            var rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0){
                throw new DataAccessException("AuthCode not found");
            }
        }
        catch (SQLException ex){
            throw new DataAccessException(String.format("Error occurred: %s", ex.getMessage()));
        }
    }

    @Override
    public void clear() throws DataAccessException, SQLException{
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "TRUNCATE TABLE auth";
            var ps = conn.prepareStatement(stmt);
            ps.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to clear table: %s", ex.getMessage()));
        }
    }

    @Override
    public int size() throws SQLException, DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT COUNT(*) FROM auth";
            var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            var rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        catch (SQLException e){
            throw new DataAccessException(String.format("Unable to get table size: %s", e.getMessage()));
        }
        return 0;
    }

    private void executeUpdate(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,currAuth.authToken());
                ps.setString(2,currAuth.username());
                ps.setString(3,currJson);
                ps.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to add to table: %s", ex.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS auth (
            `authToken` VARCHAR(50) NOT NULL,
            `username` VARCHAR(50) NOT NULL,
            `json` TEXT NOT NULL,
            PRIMARY KEY (`username`)
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String line : createTable) {
                try (var preppedStatement = conn.prepareStatement(line)) {
                    preppedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure table: %s", ex.getMessage()));
        }
    }
}
