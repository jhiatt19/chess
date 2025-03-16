package services;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import dataaccess.UserMemoryAccess;
import exception.ResponseException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
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
        catch (DataAccessException ex){
            throw new ResponseException(403, "Error: already taken");
        }
    }

    public UserData checkUser(UserData user) throws ResponseException, SQLException, DataAccessException {
        UserData foundUser;
        try  {
            foundUser = userData.getUser(user);
        } catch (DataAccessException ex) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        var hashedPassword = BCrypt.checkpw(user.password(), BCrypt.gensalt());
        if (foundUser == null || hashedPassword){
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
