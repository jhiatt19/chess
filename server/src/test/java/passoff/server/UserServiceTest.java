package passoff.server;

import dataaccess.UserMemoryAccess;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    static final UserService userService = new UserService(new UserMemoryAccess());
    ArrayList<UserData> userList = new ArrayList<>();

    @BeforeEach
    void clear() throws ResponseException {
        userService.clear();
    }

    @Test
        //Create User
    void createUser() throws ResponseException {
        var user = new UserData("TestUser", "asdf", "user@mail.com");
        user = userService.createUser(user);

        ArrayList<UserData> userList = new ArrayList<>();
        userList.add(user);

        assertEquals(1, userList.size());
        assertTrue(userList.contains(user));
        assertEquals(userService.checkUser(user), user);
    }

    @Test
    void sameUsername() throws ResponseException {
        var sameUser = new UserData("TestUser", "asdfasdf", "user@mail.com");
        sameUser = userService.createUser(sameUser);

        userList.add(sameUser);

        assertEquals(1, userList.size());
        assertTrue(userList.contains(sameUser));
        assertEquals(userService.checkUser(sameUser), sameUser);

    }

    @Test
    void samePassword() throws ResponseException {
    var user = new UserData("TestUser", "asdf", "user@mail.com");
        user = userService.createUser(user);
        var samePassword = new UserData("TestUser1", "asdf", "user@mail.com");
        samePassword = userService.createUser(samePassword);

        userList.add(user);
        userList.add(samePassword);

        assertEquals(2, userList.size());
        assertTrue(userList.contains(samePassword));
        assertEquals(userService.checkUser(samePassword), samePassword);

    }
}

