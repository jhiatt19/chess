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
        try {
            return authData.setAuth(authdata);
        } catch (DataAccessException ex) {
            throw new ResponseException(400,"Error: bad request");
        }

    }

    public void deleteAuth(String token) throws ResponseException, SQLException, DataAccessException {
        try {
            authData.deleteAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    public void clear() throws SQLException, DataAccessException {
        authData.clear();
    }

    public AuthData checkAuth(String token) throws ResponseException, DataAccessException {
        try {
            return authData.checkAuth(token);
        } catch (DataAccessException ex) {
            throw new ResponseException(400, "Error: bad request");
        }
    }

    public int size() throws SQLException, DataAccessException {
        return authData.size();
    }
}
