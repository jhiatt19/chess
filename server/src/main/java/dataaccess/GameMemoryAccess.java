package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.JoinGameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GameMemoryAccess implements GameDAO {
    List<GameData> gameDB = new ArrayList<>();
    int gameID = 0;

    public int createGame(String name) throws ResponseException {
        if (name == null){
            throw new ResponseException(400,"Error: bad request");
        }
        setGameID(gameID+1);
        var madeGame = new GameData(gameID,null,null,name,new ChessGame());
        gameDB.add(madeGame);
        return gameID;
    };

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public List<GameData> listGame(){
        HashMap<Integer,GameData> modifiedDB = new HashMap<>();
        for(GameData game : gameDB){
            modifiedDB.put(game.gameID(),game);
        }
        return gameDB;
    };

    public void joinGame(JoinGameData playerColor, String username) throws ResponseException {
        for (GameData game : gameDB){
            if (game.gameID() == playerColor.gameID()){
                if (playerColor.playerColor().equals("WHITE") || playerColor.playerColor().equals("WHITE/BLACK")){
                    if (game.whiteUsername() != null) {
                        throw new ResponseException(403, "Error: Already taken");
                    }
                    else {
                        var otherPlayer = game.blackUsername();
                        addPlayer(playerColor, username, game, otherPlayer);
                        break;
                    }
                }
                else if (playerColor.playerColor().equals("BLACK")){
                    if (game.blackUsername() != null) {
                        throw new ResponseException(403, "Error: Already taken");
                    }
                    else {
                    var otherPlayer = game.getWhiteUsername();
                    addPlayer(playerColor, otherPlayer, game, username);
                    break;
                    }
               }
                else {
                    throw new ResponseException(400, "Error: bad request");
                }
            }
        }
    };

    private void addPlayer(JoinGameData playerColor,String whitePlayer,GameData game, String blackPlayer){
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

    public GameData findGame(int idGame) throws ResponseException {
        for (GameData game : gameDB){
            if (game.getGameID() == idGame){
                return game;
            }
        }
        throw new ResponseException(400, "Error: bad request");
    }

    public int size() {
        return gameDB.size();
    }

    @Override
    public GameData updateGame(int gameID, GameData game, String color) throws ResponseException, SQLException, DataAccessException {
        return null;
    }

}
