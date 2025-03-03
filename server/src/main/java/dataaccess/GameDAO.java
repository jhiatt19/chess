package dataaccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashSet;

public interface GameDAO {
    int createGame(AuthData authUser, String name); //implement throw

    //GameData setGame();

    HashSet<GameData> listGame();

    void joinGame(JoinGameData color, String user);

    void clear();

    void addPlayer(JoinGameData color, String user, GameData game, String otherPlayer);

    GameData findGame(int gameID);
}
