package services;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

public class GameService {
    HashMap<String,String> returnVal = new HashMap<>();
    private final GameDAO gameData;

    public GameService(GameDAO gameData){
        this.gameData = gameData;
    }

    public HashSet<GameData> listGame() throws ResponseException, DataAccessException {
        return gameData.listGame();
    }

    public int createGame(String game) throws ResponseException, DataAccessException {
        return gameData.createGame(game);
    }

    public void clear() throws SQLException, DataAccessException {
        gameData.clear();
    }

    public void joinGame(JoinGameData game, String username) throws ResponseException, SQLException, DataAccessException {
        try {
            gameData.findGame(game.gameID());
        } catch (DataAccessException ex) {
            throw new ResponseException(400, "Error: bad request");

        }
        try {
            gameData.joinGame(game,username);
        } catch (DataAccessException ex) {
            throw new ResponseException(403, String.format("Error: already taken: %s",ex.getMessage()));
        }
    }

    public GameData getGame(int gameID) throws ResponseException, DataAccessException {
        var game = gameData.findGame(gameID);
        if (game == null){
            throw new ResponseException(400,"Error: bad request");
        }
        return game;
    }

    public int size() throws SQLException, DataAccessException {
        return gameData.size();
    }

}
