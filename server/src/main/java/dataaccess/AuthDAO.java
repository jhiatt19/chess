package dataaccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    AuthData setAuth(AuthData authdata); //implement throw
    boolean checkAuth(AuthData authdata);
    boolean deleteAuth(String token);
    void clear();
}
