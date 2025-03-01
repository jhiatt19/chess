package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(); //implement throw
    AuthData getAuth();
    void deleteAuth();
    void clear();
}
