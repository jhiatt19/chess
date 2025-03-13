package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class UserMemoryAccess implements UserDAO {
    final private HashSet<UserData> userDB = new HashSet<>();

    public UserData createUser(UserData user) throws ResponseException {
        var foundUser = this.getUser(user);
        if (foundUser != null){
            throw new ResponseException(401,"Error: Already taken");
        }
        else if (user.username() == null || user.password() == null){
            throw new ResponseException(400,"Error: bad request");
        }
        else {
            var madeUser = new UserData(user.username(), user.password(), user.email());
            userDB.add(madeUser);
            return madeUser;
        }
    };

    public UserData getUser(UserData user){
        for (UserData u : userDB) {
            if (user.username().equals(u.username())) {
                return u;
            }
        }
        return null;
    };

    public void clear(){
        userDB.clear();
    };

    public int size() {
        return userDB.size();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserMemoryAccess that = (UserMemoryAccess) o;
        return Objects.equals(userDB, that.userDB);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userDB);
    }
}

