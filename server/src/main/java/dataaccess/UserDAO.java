package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(UserData user); //implement throw

    UserData getUser(UserData user);

    void clear();

    int size();
}
