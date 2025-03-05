package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashSet;

public class GameMemoryAccess implements GameDAO {
    HashSet<GameData> gameDB = new HashSet<>();
    int gameID = 0;

    public int createGame(String name) {
        setGameID(gameID+1);
        var madeGame = new GameData(gameID,null,null,name,new ChessGame());
        gameDB.add(madeGame);
        return gameID;
    };

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public HashSet<GameData> listGame(){
        return gameDB;
    };

    public GameData joinGame(JoinGameData playerColor, String username) throws ResponseException {
        for (GameData game : gameDB){
            if (game.gameID() == playerColor.gameID()){
                if (playerColor.playerColor().equals("WHITE")){
                    if (game.whiteUsername() != null) {
                        throw new ResponseException(403, "Error: Already taken");
                    }
                    else {
                        var otherPlayer = game.blackUsername();
                        addPlayer(playerColor, username, game, otherPlayer);
                        return game;
                    }
                }
                else {
                    if (game.blackUsername() != null) {
                        throw new ResponseException(403, "Error: Already taken");
                    }
                    else {
                    var otherPlayer = game.getWhiteUsername();
                    addPlayer(playerColor, otherPlayer, game, username);
                    return game;
                    }
               }
            }
        }
        return null;
    };

    public void addPlayer(JoinGameData playerColor,String whitePlayer,GameData game, String blackPlayer){
        var chessgame = game.game();
        var gameName = game.gameName();
        var madeGame = new GameData(playerColor.gameID(), whitePlayer, blackPlayer, gameName,chessgame);
        gameDB.remove(game);
        gameDB.add(madeGame);
    }

    public void clear(){
        gameDB.clear();
        setGameID(0);
    };

    public GameData findGame(int idGame){
        for (GameData game : gameDB){
            if (game.getGameID() == idGame){
                return game;
            }
        }
        return null;
    }

    public int size() {
        return gameDB.size();
    }

}
