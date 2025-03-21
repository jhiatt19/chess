package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.JoinGameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class GameDAOTests {

    private GameDAO getGameDAOAccess(Class<? extends GameDAO> gameData) throws DataAccessException, SQLException {
        GameDAO gameDB;
        if (gameData.equals(GameSqlDataAccess.class)) {
            gameDB = new GameSqlDataAccess();
        }
        else {
            gameDB = new GameMemoryAccess();
        }

        gameDB.clear();
        return gameDB;
    }


    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void createGame(Class<? extends GameDAO> gameDBclass) throws DataAccessException, ResponseException, SQLException {
        GameDAO gameAccess = getGameDAOAccess(gameDBclass);

        var gameID = gameAccess.createGame("First game");
        assertEquals(1,gameID);
        assertDoesNotThrow(() -> gameAccess.createGame("Second game"));
    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void createGameNoName(Class<? extends GameDAO> gameDBclass) throws DataAccessException, ResponseException, SQLException {
        GameDAO gameAccess = getGameDAOAccess(gameDBclass);
        assertThrows(Exception.class, () -> gameAccess.createGame(null));
    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void clear(Class<? extends GameDAO> gameClearClass) throws DataAccessException, ResponseException, SQLException{
        GameDAO gameAccess = getGameDAOAccess(gameClearClass);

        assertDoesNotThrow(gameAccess::clear);
        assertEquals(0,gameAccess.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void size(Class<? extends GameDAO> gameSizeClass) throws DataAccessException, ResponseException, SQLException{
        GameDAO gameAccess = getGameDAOAccess(gameSizeClass);

        gameAccess.createGame("First");
        gameAccess.createGame("Second");
        gameAccess.createGame("Third");

        assertDoesNotThrow(gameAccess::size);
        assertEquals(3,gameAccess.size());
    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void findGame(Class<? extends GameDAO> gameDBaccess) throws DataAccessException, ResponseException, SQLException {
        GameDAO gameAccess = getGameDAOAccess(gameDBaccess);

        gameAccess.createGame("Numero Uno");

        GameData tester = new GameData(1,null,null,"Numero Uno", new ChessGame());
        assertEquals(tester, gameAccess.findGame(1));
        assertDoesNotThrow(() -> gameAccess.findGame(1));
    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void findGameBadId(Class<? extends GameDAO> gameDBaccess) throws DataAccessException, ResponseException, SQLException {
        GameDAO gameAccess = getGameDAOAccess(gameDBaccess);

        gameAccess.createGame("Numero Uno");

        assertThrows(Exception.class, () -> gameAccess.findGame(40));
    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void joinGame(Class<? extends GameDAO> gameDBclass) throws DataAccessException, ResponseException, SQLException{
        GameDAO gameAccess = getGameDAOAccess(gameDBclass);

        gameAccess.createGame("First");
        JoinGameData newGame = new JoinGameData(1,"WHITE");
        JoinGameData newGame2 = new JoinGameData(1, "BLACK");

        assertDoesNotThrow(() -> gameAccess.joinGame(newGame, "Jimmy"));
        assertDoesNotThrow(() -> gameAccess.joinGame(newGame2, "Timmy"));

    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void joinGameBad(Class<? extends GameDAO> gameDBclass) throws DataAccessException, ResponseException, SQLException {
        GameDAO gameAccess = getGameDAOAccess(gameDBclass);

        gameAccess.createGame("Bad game");
        JoinGameData badGame1 = new JoinGameData(45, "WHITE");
        JoinGameData badGame2 = new JoinGameData(1,"WHITE");
        JoinGameData badGame3 = new JoinGameData(1,"GREEN");

        //assertThrows(Exception.class, () -> gameAccess.joinGame(badGame1, "General Kenobi"));
        assertDoesNotThrow(() -> gameAccess.joinGame(badGame2, "Padme"));
        assertThrows(Exception.class, () -> gameAccess.joinGame(badGame2, "General Skywalker"));
        assertThrows(Exception.class, () -> gameAccess.joinGame(badGame3, "Yoda"));

    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void listGames(Class<? extends GameDAO> gameDBclass) throws DataAccessException, ResponseException, SQLException {
        GameDAO gameAccess = getGameDAOAccess(gameDBclass);

        gameAccess.createGame("A new Hope");
        JoinGameData game1 = new JoinGameData(1,"WHITE");
        gameAccess.joinGame(game1,"Kenobi");
        JoinGameData game1B = new JoinGameData(1,"BLACK");
        gameAccess.joinGame(game1B,"Vader");

        assertDoesNotThrow(gameAccess::listGame);

        var game = gameAccess.findGame(1);

        var recievedGame = gameAccess.listGame().toArray();
        assertEquals(game,recievedGame[0]);

    }

    @ParameterizedTest
    @ValueSource(classes = {GameSqlDataAccess.class, GameMemoryAccess.class})
    void listGameNone(Class<? extends GameDAO> gameDBclass) throws SQLException,ResponseException,DataAccessException {
        GameDAO gameAccess = getGameDAOAccess(gameDBclass);

        assertDoesNotThrow(gameAccess::listGame);

        gameAccess.clear();
    }
}
