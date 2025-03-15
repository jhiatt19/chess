package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameMemoryAccess;
import exception.ResponseException;
import model.GameData;
import model.JoinGameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GameService;

import java.sql.SQLException;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    static final GameService GAMESERVICE = new GameService(new GameMemoryAccess());
    HashSet<GameData> gamesList = new HashSet<>();

    @BeforeEach
    void clear() throws ResponseException, SQLException, DataAccessException {
        GAMESERVICE.clear();
    }

    @Test
    void getGame() throws ResponseException, SQLException, DataAccessException {
        GAMESERVICE.clear();
        var gameID2 = GAMESERVICE.createGame("doubleTrouble");
        System.out.println(gameID2);
        var game2 = GAMESERVICE.getGame(gameID2);

        gamesList.add(new GameData(gameID2,null,null,game2.gameName(),game2.game()));

        for (GameData game : gamesList){
            assertEquals(game,GAMESERVICE.getGame(1));
            assertEquals(1,GAMESERVICE.size());
        }
    }

    @Test
    void createGame() throws ResponseException, DataAccessException, SQLException {
        var name = "reallyCoolGameName";
        var gameID = GAMESERVICE.createGame(name);
        var chessGame = GAMESERVICE.getGame(gameID);

        var expectGame = new GameData(gameID,null,null,name,chessGame.getGame());
        gamesList.add(expectGame);

        assertEquals(expectGame,chessGame);
        assertEquals(1,GAMESERVICE.size());
    }


    @Test
    void notValidGameID() throws ResponseException, DataAccessException {
        GAMESERVICE.createGame("soloist");
        GAMESERVICE.createGame("doubleTrouble");
        GAMESERVICE.createGame("TripleThreat");

        assertThrows(ResponseException.class, () -> GAMESERVICE.getGame(9));
    }

    @Test
    void noName() throws ResponseException {
        assertThrows(ResponseException.class, () -> GAMESERVICE.createGame(null));
    }

    @Test
    void listGames() throws ResponseException, DataAccessException {
        var gameID2 = GAMESERVICE.createGame("doubleTrouble");
        var gameID3 = GAMESERVICE.createGame("TripleThreat");

        var game2 = GAMESERVICE.getGame(gameID2);
        var game3 = GAMESERVICE.getGame(gameID3);

        gamesList.add(new GameData(gameID2,null,null,game2.gameName(),game2.game()));
        gamesList.add(new GameData(gameID3,null,null,game3.gameName(),game3.game()));


        assertEquals(gamesList,GAMESERVICE.listGame());
    }

    @Test
    void joinGame() throws ResponseException, DataAccessException, SQLException {
        GAMESERVICE.createGame("soloist");
        GAMESERVICE.createGame("doubleTrouble");
        GAMESERVICE.createGame("TripleThreat");

        assertEquals(3,GAMESERVICE.size());

        var joinGame = new JoinGameData(1,"WHITE");
        var gameData = GAMESERVICE.joinGame(joinGame,"HelloPoppet");
        var updatedGame = new GameData(0,null,null,"test",new ChessGame());
        for (GameData game : gamesList){
            if (game.gameID() == 1){
                gamesList.remove(game);
                updatedGame = new GameData(game.gameID(),"HelloPoppet",game.blackUsername(),game.gameName(),game.game());
                gamesList.add(updatedGame);
                assertEquals(updatedGame,gameData);
            }
        }
    }

    @Test
    void badGameID() throws ResponseException, DataAccessException, SQLException {
        GAMESERVICE.createGame("soloist");
        GAMESERVICE.createGame("doubleTrouble");
        GAMESERVICE.createGame("TripleThreat");

        assertEquals(3,GAMESERVICE.size());

        var joinGame = new JoinGameData(4,"WHITE");

        assertThrows(ResponseException.class, () -> GAMESERVICE.joinGame(joinGame,"HelloPoppet"));
    }

    @Test
    void stealColor() throws ResponseException, DataAccessException, SQLException {
        GAMESERVICE.createGame("soloist");
        GAMESERVICE.createGame("doubleTrouble");
        GAMESERVICE.createGame("TripleThreat");

        assertEquals(3,GAMESERVICE.size());

        var joinGame = new JoinGameData(1,"WHITE");
        GAMESERVICE.joinGame(joinGame,"ImhereFirst");

        assertThrows(ResponseException.class, () -> GAMESERVICE.joinGame(joinGame,"HelloPoppet"));
    }

    @Test
    void size() throws ResponseException, DataAccessException, SQLException {
        GAMESERVICE.createGame("soloist");
        GAMESERVICE.createGame("doubleTrouble");
        GAMESERVICE.createGame("TripleThreat");

        assertEquals(3,GAMESERVICE.size());
    }
    @Test
    void clearAll() throws ResponseException, DataAccessException, SQLException {
        GAMESERVICE.createGame("soloist");
        GAMESERVICE.createGame("doubleTrouble");
        GAMESERVICE.createGame("TripleThreat");
        GAMESERVICE.clear();

        assertEquals(0,GAMESERVICE.size());
    }

}
