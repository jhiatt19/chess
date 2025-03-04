package services;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class GameService {
    HashMap<String,String> returnVal = new HashMap<>();
    private final GameDAO gameData;

    public GameService(GameDAO gameData){
        this.gameData = gameData;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public HashSet<GameData> listGame() {
        return gameData.listGame();
    }

    public int createGame(AuthData authUser, String game){
        return gameData.createGame(authUser,game);
    }

    public void clear(){
        gameData.clear();
    }

    public GameData joinGame(JoinGameData game, String username)throws ResponseException {
        var specificGameID = gameData.findGame(game.gameID());
        var added = gameData.joinGame(game,username);
        if (specificGameID == null || added == null){
            throw new ResponseException(400,"Error: bad request");
        }
        else {
            return added;
        }
    }
}
