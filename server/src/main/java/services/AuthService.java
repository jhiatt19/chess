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

    public AuthService() {
        this.authData = new AuthDAO() {
            @Override
            public AuthData setAuth(AuthData authdata) {
                return null;
            }

            @Override
            public AuthData checkAuth(String adsfa) {
                return null;
            }

            @Override
            public boolean deleteAuth(String auth) {
                return false;
            }

            @Override
            public void clear() {

            }
        };
    }

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
        if (token == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (authData.deleteAuth(token)) {
            throw new ResponseException(200, "");
        } else {
            throw new ResponseException(500, "Error: Failed to delete Auth token");
        }
    }

    public void clear(){
        authData.clear();
    }

    public AuthData checkAuth(String token) throws ResponseException {
        return authData.checkAuth(token);
    }
}
