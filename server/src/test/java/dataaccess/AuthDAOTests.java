package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTests {

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private AuthDAO getAuthDAOAccess(Class<? extends AuthDAO> authData) throws DataAccessException, SQLException {
        AuthDAO authDB;
        if(authData.equals(AuthSqlDataAccess.class)) {
            authDB = new AuthSqlDataAccess();
        }
        else {
            authDB = new UserMemoryAccess();
        }

        authDB.clear();
        return authDB;
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void setAuth(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var auth = new AuthData(generateToken(),"Jimmy");
        assertDoesNotThrow(()-> authAccess.setAuth(auth));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void setAuthNullFields(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var auth1 = new AuthData(null, "Jimmy");
        var auth2 = new AuthData(generateToken(), null);

        assertThrows(Exception.class, () -> authAccess.setAuth(auth1));
        assertThrows(Exception.class, () -> authAccess.setAuth(auth2));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void checkAuth(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var token = generateToken();
        var auth = new AuthData(token, "Jimmy");

        authAccess.setAuth(auth);

        assertDoesNotThrow(() -> authAccess.checkAuth(token));
        assertEquals(auth,authAccess.checkAuth(token));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void noValidAuth(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {

    }
}
