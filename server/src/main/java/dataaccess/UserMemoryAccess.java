package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class UserMemoryAccess implements UserDAO {
    final private HashSet<UserData> userDB = new HashSet<>();

    public UserData createUser(UserData user){
        user = new UserData(user.username(), user.password(), user.email());
        userDB.add(user);
        return user;
    };

    public UserData getUser(UserData user){
        for (UserData u : userDB) {
            if (user.username().equals(u.username())) {
                return u;
            }
        }
        return null;
    };

    public UserData updateUser(){
        return null;
    };

    public void deleteUser(){};

    public void clear(){};

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

