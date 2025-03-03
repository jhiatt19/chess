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

    public GameService() {
        this.gameData = new GameDAO() {
            @Override
            public int createGame(AuthData authUser, String name) {
                return 0;
            }

            @Override
            public HashSet<GameData> listGame() {
                return null;
            }

            @Override
            public void joinGame(JoinGameData color, String user) {

            }

            @Override
            public void clear() {

            }

            @Override
            public void addPlayer(JoinGameData color, String user, GameData game, String otherPlayer) {

            }

            @Override
            public GameData findGame(int gameID) {
                return null;
            }
        };
    }

    public GameService(GameDAO gameData){
        this.gameData = gameData;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public HashSet<GameData> listGame() {
        return gameData.listGame();
    }

    public HashMap<String,Integer> createGame(AuthData authUser, String game){
        int gameID = gameData.createGame(authUser,game);
        HashMap<String,Integer> response = new HashMap<>();
        response.put("gameID", gameID);
        return response;
    }

    public void clear(){
        gameData.clear();
    }

    public void joinGame(JoinGameData game, String username)throws ResponseException {
        var specificGameID = gameData.findGame(game.gameID());
        if (specificGameID == null){
            throw new ResponseException(400,"Error: bad request");
        }
        else {
            gameData.joinGame(game,username);
        }
    }
}
