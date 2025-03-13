package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}
