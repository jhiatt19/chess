package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserDAOTests {

    private UserDAO getUserDAOAccess(Class<? extends UserDAO> userData) throws DataAccessException {
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
    void createUser(Class<? extends UserDAO> userDBclass) throws DataAccessException, ResponseException {
        UserDAO userAccess = getUserDAOAccess(userDBclass);

        var user = new UserData("Jimmy", "Yang","jimmy@yang.com");
        assertDoesNotThrow(() -> userAccess.createUser(user));
    }

    @ParameterizedTest
    @ValueSource(classes = {UserSqlDataAccess.class, UserMemoryAccess.class})
    void createSameUser(Class<? extends UserDAO> userDBclass) throws DataAccessException, ResponseException {
        UserDAO userAccess = getUserDAOAccess(userDBclass);

        var user1 = new UserData("Jimmy", "Yang","jimmy@yang.com");
        var user2 = new UserData("Jimmy", "Bang","jimmy@bang.com");
    }
}
