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
        if (authData.equals(AuthSqlDataAccess.class)) {
            authDB = new AuthSqlDataAccess();
        } else {
            authDB = new AuthMemoryAccess();
        }

        authDB.clear();
        return authDB;
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void setAuth(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var auth = new AuthData(generateToken(), "Jimmy");

        assertDoesNotThrow(() -> authAccess.setAuth(auth));
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
        assertEquals(auth, authAccess.checkAuth(token));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void noValidAuth(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var auth = new AuthData(generateToken(), "Jimmy");
        authAccess.setAuth(auth);

        assertThrows(Exception.class, () -> authAccess.checkAuth(generateToken()));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void deleteAuth(Class<? extends AuthDAO> authDBclass) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var token = generateToken();
        var auth = new AuthData(token, "Jimmy");
        authAccess.setAuth(auth);

        assertDoesNotThrow(() -> authAccess.deleteAuth(token));
        assertThrows(Exception.class, () -> authAccess.checkAuth(token));
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void noAuthDelete(Class<? extends AuthDAO> authDBclass) throws DataAccessException,ResponseException,SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authDBclass);

        var token = generateToken();
        var auth = new AuthData(token, "Jimmy");
        authAccess.setAuth(auth);

        var nullToken = generateToken();
        assertThrows(Exception.class, () -> authAccess.deleteAuth(nullToken));
        assertThrows(Exception.class, () -> authAccess.checkAuth(nullToken));
        assertEquals(1,authAccess.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void size(Class<? extends AuthDAO> authSizeAccess) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authSizeAccess);

        var auth1 = new AuthData(generateToken(), "Jimmy");
        var auth2 = new AuthData(generateToken(), "Billy");
        var auth3 = new AuthData(generateToken(), "Tom");

        authAccess.setAuth(auth1);
        authAccess.setAuth(auth2);
        authAccess.setAuth(auth3);

        assertDoesNotThrow(authAccess::size);
        assertEquals(3, authAccess.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {AuthSqlDataAccess.class, AuthMemoryAccess.class})
    void clear(Class<? extends AuthDAO> authClearAccess) throws DataAccessException, ResponseException, SQLException {
        AuthDAO authAccess = getAuthDAOAccess(authClearAccess);

        var auth1 = new AuthData(generateToken(), "Jimmy");
        var auth2 = new AuthData(generateToken(), "Billy");
        var auth3 = new AuthData(generateToken(), "Tom");

        authAccess.setAuth(auth1);
        authAccess.setAuth(auth2);
        authAccess.setAuth(auth3);

        assertDoesNotThrow(authAccess::clear);
        assertDoesNotThrow(authAccess::size);
        assertEquals(0,authAccess.size());
    }
}
