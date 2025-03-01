package services;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class AuthService {
    HashMap<String,String> returnVal = new HashMap<>();
    private final AuthDAO authData;

    public AuthService() {
        this.authData = new AuthDAO() {
            @Override
            public AuthData createAuth() {
                return null;
            }

            @Override
            public AuthData getAuth() {
                return null;
            }

            @Override
            public void deleteAuth() {

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
}
