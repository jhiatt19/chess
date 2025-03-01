package dataaccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(); //implement throw

    UserData getUser();

    void clear();

    void deleteUser();

    UserData updateData();
}
