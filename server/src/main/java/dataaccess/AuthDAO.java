package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData setAuth(AuthData authdata); //implement throw
    AuthData checkAuth(String token);
    AuthData deleteAuth(String token);
    void clear();
    int size();
}
