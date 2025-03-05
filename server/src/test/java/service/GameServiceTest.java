package service;

import chess.ChessGame;
import dataaccess.GameMemoryAccess;
import exception.ResponseException;
import model.GameData;
import model.JoinGameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import services.GameService;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    static final GameService gameService = new GameService(new GameMemoryAccess());
    HashSet<GameData> gamesList = new HashSet<>();

    @BeforeEach
    void clear() throws ResponseException {
        gameService.clear();
    }

    @Test
    void getGame() throws ResponseException {
        gameService.clear();
        var gameID2 = gameService.createGame("doubleTrouble");
        System.out.println(gameID2);
        var game2 = gameService.getGame(gameID2);

        gamesList.add(new GameData(gameID2,null,null,game2.gameName(),game2.game()));

        for (GameData game : gamesList){
            assertEquals(game,gameService.getGame(1));
            assertEquals(1,gameService.size());
        }
    }

    @Test
    void createGame() throws ResponseException {
        var name = "reallyCoolGameName";
        var gameID = gameService.createGame(name);
        var chessGame = gameService.getGame(gameID);

        var expectGame = new GameData(gameID,null,null,name,chessGame.getGame());
        gamesList.add(expectGame);

        assertEquals(expectGame,chessGame);
        assertEquals(1,gameService.size());
    }


    @Test
    void notValidGameID() throws ResponseException{
        gameService.createGame("soloist");
        gameService.createGame("doubleTrouble");
        gameService.createGame("TripleThreat");

        assertThrows(ResponseException.class, () -> gameService.getGame(9));
    }

    @Test
    void noName() throws ResponseException {
        assertThrows(ResponseException.class, () -> gameService.createGame(null));
    }

    @Test
    void listGames() throws ResponseException {
        var gameID2 = gameService.createGame("doubleTrouble");
        var gameID3 = gameService.createGame("TripleThreat");

        var game2 = gameService.getGame(gameID2);
        var game3 = gameService.getGame(gameID3);

        gamesList.add(new GameData(gameID2,null,null,game2.gameName(),game2.game()));
        gamesList.add(new GameData(gameID3,null,null,game3.gameName(),game3.game()));


        assertEquals(gamesList,gameService.listGame());
    }

    @Test
    void joinGame() throws ResponseException {
        gameService.createGame("soloist");
        gameService.createGame("doubleTrouble");
        gameService.createGame("TripleThreat");

        assertEquals(3,gameService.size());

        var joinGame = new JoinGameData(1,"WHITE");
        var gameData = gameService.joinGame(joinGame,"HelloPoppet");
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
    void badGameID() throws ResponseException {
        gameService.createGame("soloist");
        gameService.createGame("doubleTrouble");
        gameService.createGame("TripleThreat");

        assertEquals(3,gameService.size());

        var joinGame = new JoinGameData(4,"WHITE");

        assertThrows(ResponseException.class, () -> gameService.joinGame(joinGame,"HelloPoppet"));
    }

    @Test
    void stealColor() throws ResponseException {
        gameService.createGame("soloist");
        gameService.createGame("doubleTrouble");
        gameService.createGame("TripleThreat");

        assertEquals(3,gameService.size());

        var joinGame = new JoinGameData(1,"WHITE");
        gameService.joinGame(joinGame,"ImhereFirst");

        assertThrows(ResponseException.class, () -> gameService.joinGame(joinGame,"HelloPoppet"));
    }

    @Test
    void size() throws ResponseException{
        gameService.createGame("soloist");
        gameService.createGame("doubleTrouble");
        gameService.createGame("TripleThreat");

        assertEquals(3,gameService.size());
    }
    @Test
    void clearAll() throws ResponseException{
        gameService.createGame("soloist");
        gameService.createGame("doubleTrouble");
        gameService.createGame("TripleThreat");
        gameService.clear();

        assertEquals(0,gameService.size());
    }

    @Test
    void noGames() throws ResponseException {
        assertThrows(ResponseException.class, () -> gameService.listGame());
    }

}
