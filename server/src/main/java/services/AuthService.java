package services;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

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

    public AuthData setAuth(AuthData authdata) throws ResponseException {
        if (authdata.authToken() == null){
            throw new ResponseException(500,"Error: No data saved");
        }
        else {
            return authData.setAuth(authdata);
        }
    }

    public AuthData deleteAuth(String token) throws ResponseException {
        var logOut = authData.deleteAuth(token);
        if (logOut == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        else {
            return logOut;
        }
    }

    public void clear(){
        authData.clear();
    }

    public AuthData checkAuth(String token) throws ResponseException {
        return authData.checkAuth(token);
    }
}
