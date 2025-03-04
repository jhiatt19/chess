package services;

import dataaccess.UserDAO;
import dataaccess.UserMemoryAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import static model.DataTransformation.transform;

public class UserService {
    Map<String,Object> returnVal = new HashMap<>();
    private final UserDAO userData;

    public UserService(UserDAO userData) {
        this.userData = userData;
    }

    public UserData createUser(UserData user) throws ResponseException {
        //System.out.println(userData);
        if (userData.getUser(user) != null){
            throw new ResponseException(403, "Error: already taken");
        }
        else {
            return userData.createUser(user);
        }
    }

    public UserData checkUser(UserData user) throws ResponseException {
        var foundUser = userData.getUser(user);
        if (foundUser == null){
            throw new ResponseException(401,"Error: unauthorized");
        }
        else {
            return foundUser;
        }
    }

    public void clear() {
        userData.clear();
    }
}
