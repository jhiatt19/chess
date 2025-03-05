package service;

import dataaccess.AuthMemoryAccess;
import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.AuthService;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


public class AuthServiceTest {
    static final AuthService AUTHSERVICE = new AuthService(new AuthMemoryAccess());
    private static String generateToken(){
        return UUID.randomUUID().toString();
    }

    @BeforeEach
    void clear() throws ResponseException {
        AUTHSERVICE.clear();
    }

    @Test
    void setAuth() throws ResponseException {
        var auth = new AuthData(generateToken(),"yoyoMa");
        auth = AUTHSERVICE.setAuth(auth);

        assertFalse(Boolean.parseBoolean(auth.authToken()));
    }

    @Test
    void noAuthCode() throws ResponseException {
        var auth2 = new AuthData(null,"papi");

        assertThrows(ResponseException.class, () -> AUTHSERVICE.setAuth(auth2));
    }

    @Test
    void noUser() throws ResponseException {
        var noUser = new AuthData(generateToken(),null);

        assertThrows(ResponseException.class, () -> AUTHSERVICE.setAuth(noUser));
    }

    @Test
    void checkUser() throws ResponseException {
        var token = generateToken();
        var user2 = new AuthData(token,"Namaste");
        AUTHSERVICE.setAuth(user2);
        assertEquals(user2,AUTHSERVICE.checkAuth(token));
    }

    @Test
    void badAuth() throws ResponseException {
        assertNull(AUTHSERVICE.checkAuth(generateToken()));
    }

    @Test
    void logOut() throws ResponseException {
        var log = new AuthData(generateToken(),"MyBoi");
        log = AUTHSERVICE.setAuth(log);
        var logout = AUTHSERVICE.deleteAuth(log.authToken());

        assertNull(AUTHSERVICE.checkAuth(logout.authToken()));
    }

    @Test
    void badAuthCode() throws ResponseException {
        assertThrows(ResponseException.class, () -> AUTHSERVICE.deleteAuth(generateToken()));
        assertThrows(ResponseException.class, () -> AUTHSERVICE.deleteAuth(null));
    }

    @Test
    void size() throws ResponseException{
        var token = generateToken();
        var user2 = new AuthData(token,"Namaste");
        var auth = new AuthData(generateToken(),"yoyoMa");
        AUTHSERVICE.setAuth(auth);
        AUTHSERVICE.setAuth(user2);

        assertEquals(2,AUTHSERVICE.size());
    }

    @Test
    void clearAll() throws ResponseException {
        AUTHSERVICE.clear();

        assertEquals(0,AUTHSERVICE.size());
    }
}
