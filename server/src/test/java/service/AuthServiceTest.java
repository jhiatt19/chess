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
    static final AuthService authService = new AuthService(new AuthMemoryAccess());
    private static String generateToken(){
        return UUID.randomUUID().toString();
    }

    @BeforeEach
    void clear() throws ResponseException {
        authService.clear();
    }

    @Test
    void setAuth() throws ResponseException {
        var auth = new AuthData(generateToken(),"yoyoMa");
        auth = authService.setAuth(auth);

        assertFalse(Boolean.parseBoolean(auth.authToken()));
    }

    @Test
    void noAuthCode() throws ResponseException {
        var auth2 = new AuthData(null,"papi");

        assertThrows(ResponseException.class, () -> authService.setAuth(auth2));
    }

    @Test
    void noUser() throws ResponseException {
        var noUser = new AuthData(generateToken(),null);

        assertThrows(ResponseException.class, () -> authService.setAuth(noUser));
    }

    @Test
    void checkUser() throws ResponseException {
        var token = generateToken();
        var user2 = new AuthData(token,"Namaste");
        authService.setAuth(user2);
        assertEquals(user2,authService.checkAuth(token));
    }

    @Test
    void badAuth() throws ResponseException {
        assertNull(authService.checkAuth(generateToken()));
    }

    @Test
    void logOut() throws ResponseException {
        var log = new AuthData(generateToken(),"MyBoi");
        log = authService.setAuth(log);
        var logout = authService.deleteAuth(log.authToken());

        assertNull(authService.checkAuth(logout.authToken()));
    }

    @Test
    void badAuthCode() throws ResponseException {
        assertThrows(ResponseException.class, () -> authService.deleteAuth(generateToken()));
        assertThrows(ResponseException.class, () -> authService.deleteAuth(null));
    }

    @Test
    void size() throws ResponseException{
        var token = generateToken();
        var user2 = new AuthData(token,"Namaste");
        var auth = new AuthData(generateToken(),"yoyoMa");
        authService.setAuth(auth);
        authService.setAuth(user2);

        assertEquals(2,authService.size());
    }

    @Test
    void clearAll() throws ResponseException {
        authService.clear();

        assertEquals(0,authService.size());
    }
}
