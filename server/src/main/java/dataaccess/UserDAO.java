package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(UserData user); //implement throw

    UserData getUser(UserData user);

    void clear();

    boolean deleteUser(UserData user);

    UserData updateUser();

    int size();
}
