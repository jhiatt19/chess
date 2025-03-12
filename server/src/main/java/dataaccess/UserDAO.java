package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(UserData user) throws Exception;

    UserData getUser(UserData user);

    void clear();

    int size();
}
