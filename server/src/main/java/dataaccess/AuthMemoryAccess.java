package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Objects;

public class AuthMemoryAccess implements AuthDAO{
   final private HashMap<String,String> authDB = new HashMap<>();

    public AuthData setAuth(AuthData authdata){
        var setAuthToken = new AuthData(authdata.authToken(),authdata.username());
        authDB.put(setAuthToken.authToken(), setAuthToken.username());
        return setAuthToken;
    };

    public boolean checkAuth(AuthData authdata){
        return authDB.containsKey(authdata.authToken());
    };

    public boolean deleteAuth(String token){
        authDB.remove(token);
        return authDB.containsKey(token);
    };

    public void clear(){
        authDB.clear();
    };

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthMemoryAccess that = (AuthMemoryAccess) o;
        return Objects.equals(authDB, that.authDB);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authDB);
    }
}
