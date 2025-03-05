package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashSet;

public interface GameDAO {
    int createGame(String name); //implement throw

    HashSet<GameData> listGame();

    GameData joinGame(JoinGameData color, String user) throws ResponseException;

    void clear();

    void addPlayer(JoinGameData color, String user, GameData game, String otherPlayer);

    GameData findGame(int gameID);

    int size();
}
