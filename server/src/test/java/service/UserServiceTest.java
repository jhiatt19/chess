package service;

import dataaccess.DataAccessException;
import dataaccess.UserMemoryAccess;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.UserService;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    static final UserService USERSERVICE = new UserService(new UserMemoryAccess());
    ArrayList<UserData> userList = new ArrayList<>();
    UserData user = new UserData("Tester","asdf","u@yahoo.com");
    UserData nextUser = new UserData("Swiper","noswiping","dora@explorer.com");
    UserData tweedleDee = new UserData("TweedleDee","tweedleDum","deadPoetsSocieity@taylor.com");

    @BeforeEach
    void clear() throws ResponseException, DataAccessException, SQLException {
        USERSERVICE.clear();
    }

    @Test
        //Create User
    void createUser() throws ResponseException, DataAccessException {
        var user = new UserData("TestUser", "asdf", "user@mail.com");
        user = USERSERVICE.createUser(user);

        ArrayList<UserData> userList = new ArrayList<>();
        userList.add(user);

        assertEquals(1, userList.size());
        assertTrue(userList.contains(user));
    }

    @Test
    void sameUsername() throws ResponseException, DataAccessException {
        var sameUser = new UserData("TestUser", "asdfasdf", "user@mail.com");
        sameUser = USERSERVICE.createUser(sameUser);

        userList.add(sameUser);

        assertEquals(1, userList.size());
        assertTrue(userList.contains(sameUser));

    }

    @Test
    void samePassword() throws ResponseException, DataAccessException {
    var user = new UserData("TestUser", "asdf", "user@mail.com");
        user = USERSERVICE.createUser(user);
        var samePassword = new UserData("TestUser1", "asdf", "user@mail.com");
        samePassword = USERSERVICE.createUser(samePassword);

        userList.add(user);
        userList.add(samePassword);

        assertEquals(2, userList.size());
        assertTrue(userList.contains(samePassword));

    }

    @Test
    void checkUser() throws ResponseException, DataAccessException, SQLException {
        user = USERSERVICE.createUser(user);
        nextUser = USERSERVICE.createUser(nextUser);
        tweedleDee = USERSERVICE.createUser(tweedleDee);

        assertEquals(USERSERVICE.checkUser(user),user);
        assertEquals(USERSERVICE.checkUser(nextUser),nextUser);
        assertEquals(USERSERVICE.checkUser(tweedleDee),tweedleDee);

    }

    @Test
    void noUser() throws ResponseException, DataAccessException {
        USERSERVICE.createUser(user);
        USERSERVICE.createUser(nextUser);
        USERSERVICE.createUser(tweedleDee);

        var notAdded = new UserData("TweedleDum", "hehehe", "imnothere@gmail.com");

        assertThrows(ResponseException.class, ()-> USERSERVICE.checkUser(notAdded));
    }

    @Test
    void size() throws ResponseException, DataAccessException, SQLException {
        user = USERSERVICE.createUser(user);
        nextUser = USERSERVICE.createUser(nextUser);
        tweedleDee = USERSERVICE.createUser(tweedleDee);

        assertEquals(3,USERSERVICE.size());
    }

    @Test
    void clearUserData() throws ResponseException, DataAccessException, SQLException {
        user = USERSERVICE.createUser(user);
        nextUser = USERSERVICE.createUser(nextUser);
        tweedleDee = USERSERVICE.createUser(tweedleDee);

        USERSERVICE.clear();

        assertEquals(0,USERSERVICE.size());
    }
}

