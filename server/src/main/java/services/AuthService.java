package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class AuthService {
    HashMap<String,String> returnVal = new HashMap<>();
    private final AuthDAO authData;

    public AuthService(AuthDAO authData){
        this.authData = authData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData setAuth(AuthData authdata) throws ResponseException, DataAccessException {
        return authData.setAuth(authdata);
    }

    public void deleteAuth(String token) throws ResponseException, SQLException, DataAccessException {
        authData.deleteAuth(token);
    }

    public void clear() throws SQLException, DataAccessException {
        authData.clear();
    }

    public AuthData checkAuth(String token) throws ResponseException, DataAccessException {
        return authData.checkAuth(token);
    }

    public int size() throws SQLException, DataAccessException {
        return authData.size();
    }
}
