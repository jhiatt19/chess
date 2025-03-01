package services;

import dataaccess.UserDAO;
import exception.ResponseException;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class UserService {
    HashMap<String,String> returnVal = new HashMap<>();
    private final UserDAO userData;
    String authVal = generateToken();
    String stringUser = "Username";
    String stringAuthVal = "authToken";

    public UserService() {
        this.userData = new UserDAO() {
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
            public void deleteUser() {

            }

            @Override
            public UserData updateUser() {
                return null;
            }
        };
    }
    public UserService(UserDAO userData){
        this.userData = userData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public HashMap<String, String> createHashMap(String name, String token){
        returnVal.clear();
        returnVal.put(stringUser,name);
        returnVal.put(stringAuthVal,token);
        return returnVal;
    }

    public HashMap<String, String> createUser(UserData user) throws ResponseException {
        var userMade = userData.createUser(user);
        if (userData.getUser(user) != null){
            throw new ResponseException(403, "Error: already taken");
        }
        else {
            return createHashMap(userMade.getUsername(), authVal);
        }
    }

    public HashMap<String, String> getAuth(UserData user) throws ResponseException {
        var foundUser = userData.getUser(user);
        if (foundUser == null){
            throw new ResponseException(401,"Error: unauthorized");
        }
        else {
            return createHashMap(user.username(),authVal);
        }
    }

    public void clear() {
        userData.clear();
    }
}
