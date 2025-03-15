package dataaccess;

import exception.ResponseException;
import model.GameData;
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
}
