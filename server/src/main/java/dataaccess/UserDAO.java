package dataaccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    UserData createUser(UserData user) throws Exception;

    UserData getUser(UserData user);

    void clear() throws DataAccessException;

    int size() throws DataAccessException, SQLException;
}
