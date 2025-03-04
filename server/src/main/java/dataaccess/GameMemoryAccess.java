package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import java.util.HashSet;

public class GameMemoryAccess implements GameDAO {
    HashSet<GameData> gameDB = new HashSet<>();
    public int createGame(AuthData authUser, String name) {
        int gameID = gameDB.size()+1;
        var madeGame = new GameData(gameID,authUser.username(),null,name,new ChessGame());
        gameDB.add(madeGame);
        return gameID;
    };

//    public GameData setGame() {
//        return null;
//    };

    public HashSet<GameData> listGame(){
        return gameDB;
    };

    public GameData joinGame(JoinGameData playerColor, String username) {
        for (GameData game : gameDB){
            if (game.gameID() == playerColor.gameID() && playerColor.playerColor() != null){
                if (playerColor.playerColor().equals("WHITE")){
                    var otherPlayer = game.blackUsername();
                    addPlayer(playerColor,username,game,otherPlayer);
                    return game;
                }
                else {
                    var otherPlayer = game.getWhiteUsername();
                    addPlayer(playerColor,username,game,otherPlayer);
                    return game;
                }
            }
        }
        return null;
    };

    public void addPlayer(JoinGameData playerColor,String username,GameData game, String otherPlayer){
        var chessgame = game.game();
        var gameName = game.gameName();
        var madeGame = new GameData(playerColor.gameID(), username, otherPlayer, gameName,chessgame);
        gameDB.remove(game);
        gameDB.add(madeGame);
    }

    public void clear(){
        gameDB.clear();
    };

    public GameData findGame(int idGame){
        for (GameData game : gameDB){
            if (game.getGameID() == idGame){
                return game;
            }
        }
        return null;
    }

}
