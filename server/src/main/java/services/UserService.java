package services;

import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static model.DataTransformation.transform;

public class UserService {
    Map<String,Object> returnVal;
    private final UserDAO userData;
    String authVal = generateToken();

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
            public boolean deleteUser(UserData user) {
                return false;
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

    public Map<String, Object> createMap(UserData user, String token){
        returnVal.clear();
        HashMap<String, String> response = new HashMap<>();
        AuthData authData = transform(user,token);
        response.put("username", user.username());
        response.put("authToken",token);
        returnVal.put("response",response);
        returnVal.put("authData",authData);
        return returnVal;
    }

    public Map<String, Object> createUser(UserData user) throws ResponseException {
        var userMade = userData.createUser(user);
        if (userData.getUser(user) != null){
            throw new ResponseException(403, "Error: already taken");
        }
        else {
            return createMap(userMade, authVal);
        }
    }

    public Map<String, Object> getAuth(UserData user) throws ResponseException {
        var foundUser = userData.getUser(user);
        if (foundUser == null){
            throw new ResponseException(401,"Error: unauthorized");
        }
        else {
            return createMap(user,authVal);
        }
    }

    public void clear() {
        userData.clear();
    }
}
