package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AuthMemoryAccess implements AuthDAO{
   final private HashSet<AuthData> authDB = new HashSet<>();

    public AuthData setAuth(AuthData authdata) throws ResponseException{
        if (authdata.authToken() == null || authdata.username() == null){
            throw new ResponseException(400,"Error: bad request");
        }
        else {
            var setAuthToken = new AuthData(authdata.authToken(), authdata.username());
            authDB.add(setAuthToken);
            return setAuthToken;
        }
    };

    public AuthData checkAuth(String token) throws ResponseException{
        for (var data : authDB){
            if (data.authToken().equals(token)){
                return data;
            }
        }
        throw new ResponseException(401, "Error: bad request");
    };

    public void deleteAuth(String token) throws ResponseException{
        var deleted = false;
        for (AuthData data : authDB){
            if (data.authToken().equals(token)){
                authDB.remove(data);
                deleted = true;
            }
        }
        if (!deleted) {
            throw new ResponseException(401, "Error: unauthorized");
        }
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
