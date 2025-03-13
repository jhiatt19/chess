package services;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.UserMemoryAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.*;

import static model.DataTransformation.transform;

public class UserService {
    Map<String,Object> returnVal = new HashMap<>();
    private final UserDAO userData;

    public UserService(UserDAO userData) {
        this.userData = userData;
    }

    public UserData createUser(UserData user) throws ResponseException, DataAccessException {
        //System.out.println(userData);
        try {
            return userData.createUser(user);
        }
        catch (Exception ex){
            throw new DataAccessException(String.format("Did not add user", ex.getMessage()));
        }
    }

    public UserData checkUser(UserData user) throws ResponseException, SQLException, DataAccessException {
        var foundUser = userData.getUser(user);
        if (foundUser == null){
            throw new ResponseException(401,"Error: unauthorized");
        }
        else if (!foundUser.password().equals(user.password())){
            throw new ResponseException(401,"Error: unauthorized");
        }

        else {
            return foundUser;
        }
    }

    public void clear() throws DataAccessException, SQLException {
        userData.clear();
    }

    public int size() throws DataAccessException, SQLException {
        return userData.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserService that = (UserService) o;
        return Objects.equals(returnVal, that.returnVal) && Objects.equals(userData, that.userData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnVal, userData);
    }
}
