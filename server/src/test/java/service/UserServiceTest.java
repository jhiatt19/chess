package service;

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
    UserData user = new UserData("Tester","asdf","u@yahoo.com");
    UserData nextUser = new UserData("Swiper","noswiping","dora@explorer.com");
    UserData tweedleDee = new UserData("TweedleDee","tweedleDum","deadPoetsSocieity@taylor.com");

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
    }

    @Test
    void sameUsername() throws ResponseException {
        var sameUser = new UserData("TestUser", "asdfasdf", "user@mail.com");
        sameUser = userService.createUser(sameUser);

        userList.add(sameUser);

        assertEquals(1, userList.size());
        assertTrue(userList.contains(sameUser));

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

    }

    @Test
    void checkUser() throws ResponseException{
        user = userService.createUser(user);
        nextUser = userService.createUser(nextUser);
        tweedleDee = userService.createUser(tweedleDee);

        assertEquals(userService.checkUser(user),user);
        assertEquals(userService.checkUser(nextUser),nextUser);
        assertEquals(userService.checkUser(tweedleDee),tweedleDee);

    }

    @Test
    void noUser() throws ResponseException {
        userService.createUser(user);
        userService.createUser(nextUser);
        userService.createUser(tweedleDee);

        var notAdded = new UserData("TweedleDum", "hehehe", "imnothere@gmail.com");

        assertThrows(ResponseException.class, ()-> userService.checkUser(notAdded));
    }

    @Test
    void size() throws ResponseException {
        user = userService.createUser(user);
        nextUser = userService.createUser(nextUser);
        tweedleDee = userService.createUser(tweedleDee);

        assertEquals(3,userService.size());
    }

    @Test
    void clearUserData() throws ResponseException {
        user = userService.createUser(user);
        nextUser = userService.createUser(nextUser);
        tweedleDee = userService.createUser(tweedleDee);

        userService.clear();

        assertEquals(0,userService.size());
    }
}

