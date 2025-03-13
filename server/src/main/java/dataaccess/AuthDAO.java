package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public interface AuthDAO {
    AuthData setAuth(AuthData authdata) throws ResponseException, DataAccessException; //implement throw
    AuthData checkAuth(String token) throws ResponseException, DataAccessException;
    AuthData deleteAuth(String token) throws ResponseException, DataAccessException, SQLException;
void clear() throws SQLException, DataAccessException;
    int size() throws SQLException, DataAccessException;
}
