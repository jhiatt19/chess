package dataaccess;

import exception.ResponseException;
import model.GameData;
import model.JoinGameData;

import java.sql.SQLException;
import java.util.List;

public interface GameDAO {
    int createGame(String name) throws DataAccessException, ResponseException; //implement throw

    List<GameData> listGame() throws DataAccessException;

    void joinGame(JoinGameData color, String user) throws ResponseException, SQLException, DataAccessException;

    void clear() throws DataAccessException, SQLException;

    //void addPlayer(JoinGameData color, String user, GameData game, String otherPlayer);

    GameData findGame(int gameID) throws DataAccessException, ResponseException;

    int size() throws SQLException, DataAccessException;

    GameData updateGame(int gameID, GameData game, String color) throws ResponseException, SQLException, DataAccessException;
}
