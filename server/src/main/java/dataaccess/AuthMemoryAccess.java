package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AuthMemoryAccess implements AuthDAO{
   final private HashSet<AuthData> authDB = new HashSet<>();

    public AuthData setAuth(AuthData authdata){
        var setAuthToken = new AuthData(authdata.authToken(),authdata.username());
        authDB.add(setAuthToken);
        return setAuthToken;
    };

    public AuthData checkAuth(String token){
        for (var data : authDB){
            if (data.authToken().equals(token)){
                return data;
            }
        }
        return null;
    };

    public AuthData deleteAuth(String token){
        for (AuthData data : authDB){
            if (data.authToken().equals(token)){
                authDB.remove(data);
                return data;
            }
        }
       return null;
    };

    public void clear(){
        authDB.clear();
    };

    public int size(){
        return authDB.size();
    }

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
