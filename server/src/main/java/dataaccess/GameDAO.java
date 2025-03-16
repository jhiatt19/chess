package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.sql.SQLException;
import java.util.HashSet;

public interface GameDAO {
    int createGame(String name) throws DataAccessException, ResponseException; //implement throw

    HashSet<GameData> listGame();

    void joinGame(JoinGameData color, String user) throws ResponseException, SQLException, DataAccessException;

    void clear() throws DataAccessException, SQLException;

    //void addPlayer(JoinGameData color, String user, GameData game, String otherPlayer);

    GameData findGame(int gameID) throws DataAccessException, ResponseException;

    int size() throws SQLException, DataAccessException;
}
