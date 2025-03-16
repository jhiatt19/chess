package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTests {

    private UserDAO getUserDAOAccess(Class<? extends UserDAO> userData) throws DataAccessException, SQLException {
        UserDAO userDB;
        if (userData.equals(UserSqlDataAccess.class)) {
            userDB = new UserSqlDataAccess();
        }
        else {
            userDB = new UserMemoryAccess();
        }

        userDB.clear();
        return userDB;
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void createUser(Class<? extends UserDAO> userDBclass) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userDBclass);

        var user = new UserData("Jimmy", "Yang","jimmy@yang.com");
        assertDoesNotThrow(() -> userAccess.createUser(user));
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void createSameUser(Class<? extends UserDAO> userDBclass) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userDBclass);

        var user1 = new UserData("Jimmy", "Yang","jimmy@yang.com");
        var user2 = new UserData("Jimmy", "Bang","jimmy@bang.com");

        userAccess.createUser(user1);
        assertThrows(Exception.class, ()-> userAccess.createUser(user2));
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void createNullUser(Class<? extends UserDAO> userDBclass) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userDBclass);

        var user1 = new UserData("Jimmy", "Yang","jimmy@yang.com");
        var user2 = new UserData(null, "Bang","jimmy@bang.com");

        userAccess.createUser(user1);
        assertThrows(Exception.class, ()-> userAccess.createUser(user2));
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void size(Class<? extends UserDAO> userClearAccess) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userClearAccess);

        var user1 = new UserData("Jimmy", "Yang","jimmy@yang.com");
        var user2 = new UserData("Timmy", "Bang","jimmy@bang.com");
        var user3 = new UserData("Mike","Wazozki", "mike@monstersinc.com");

        userAccess.createUser(user1);
        userAccess.createUser(user2);
        userAccess.createUser(user3);

        assertDoesNotThrow(userAccess::size);
        assertEquals(3,userAccess.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void clear(Class<? extends UserDAO> userClearAccess) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userClearAccess);

        var user1 = new UserData("Jimmy", "Yang","jimmy@yang.com");
        var user2 = new UserData("Timmy", "Bang","jimmy@bang.com");
        var user3 = new UserData("Mike","Wazozki", "mike@monstersinc.com");

        userAccess.createUser(user1);
        userAccess.createUser(user2);
        userAccess.createUser(user3);

        assertDoesNotThrow(userAccess::clear);
        assertDoesNotThrow(userAccess::size);
        assertEquals(0,userAccess.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void getUser(Class<? extends UserDAO> userGetAccess) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userGetAccess);

        var user1 = new UserData("Jimmy", "Yang","jimmy@yang.com");
        var user2 = new UserData("Timmy", "Bang","jimmy@bang.com");
        var user3 = new UserData("Mike","Wazozki", "mike@monstersinc.com");

        userAccess.createUser(user1);
        userAccess.createUser(user2);
        userAccess.createUser(user3);

        assertDoesNotThrow(() -> userAccess.getUser(user1));
        assertDoesNotThrow(() -> userAccess.getUser(user3));
        assertEquals("Jimmy", userAccess.getUser(user1).username());
        assertEquals("Timmy", userAccess.getUser(user2).username());
        assertEquals("Mike", userAccess.getUser(user3).username());
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void getNotRegisteredUser(Class<? extends UserDAO> userGetAccess) throws DataAccessException, ResponseException, SQLException {
        UserDAO userAccess = getUserDAOAccess(userGetAccess);

        var user1 = new UserData("Jimmy", "Yang", "jimmy@yang.com");
        var user2 = new UserData("Timmy", "Bang", "jimmy@bang.com");
        var user3 = new UserData("Mike", "Wazozki", "mike@monstersinc.com");
        var user4 = new UserData("NotValid", "asdfasdf", "testing.fun@notFun.com");

        userAccess.createUser(user1);
        userAccess.createUser(user2);
        userAccess.createUser(user3);

        assertEquals("Jimmy", userAccess.getUser(user1).username());
        assertEquals("Timmy", userAccess.getUser(user2).username());
        assertEquals("Mike", userAccess.getUser(user3).username());
        assertNull(userAccess.getUser(user4));

        userAccess.clear();
    }
}
