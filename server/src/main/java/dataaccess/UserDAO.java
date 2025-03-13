package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    UserData createUser(UserData user) throws DataAccessException, ResponseException;

    UserData getUser(UserData user) throws SQLException, DataAccessException, ResponseException;

    void clear() throws DataAccessException, SQLException;

    int size() throws DataAccessException, SQLException;
}
