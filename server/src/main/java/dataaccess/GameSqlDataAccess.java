package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.JoinGameData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashSet;

public class GameSqlDataAccess implements GameDAO{
    String currGameName;
    String currJSON;
    int gameID = 1;
    GameData currGame;

    public GameSqlDataAccess() {
        try {
            configureDatabase();
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public int createGame(String name) throws DataAccessException, ResponseException {
        if (name == null){
            throw new ResponseException(400,"Error: bad request");
        }
        var statement = "INSERT INTO games (white, black, gameName, game, json) VALUES (?, ?, ?, ?, ?)";
        this.currGameName = name;
        setGameID(gameID+1);
        this.currGame = new GameData(gameID,null, null, name, new ChessGame());
        this.currJSON = new Gson().toJson(currGame);
        executeUpdate(statement);
        return gameID;
    }

    private void setGameID(int gameID) {
        this.gameID = gameID;
    }
    @Override
    public HashSet<GameData> listGame() {
        return null;
    }

    @Override
    public GameData joinGame(JoinGameData color, String user) throws DataAccessException, SQLException, ResponseException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException, SQLException{
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "TRUNCATE TABLE games";
            var ps = conn.prepareStatement(stmt);
            ps.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to clear table: %s", ex.getMessage()));
        }

    }

    @Override
    public void addPlayer(JoinGameData color, String user, GameData game, String otherPlayer) {

    }

    @Override
    public GameData findGame(int gameID) {
        return null;
    }

    @Override
    public int size() throws SQLException, DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT COUNT(*) FROM games";
            var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            var rs = ps.executeQuery();

            if (rs.next()){
                return rs.getInt(1);
            }
        }
        catch (SQLException e){
            throw new DataAccessException(String.format("Unable to get table size: %s",e.getMessage()));
        }
        return 0;
    }

    private void executeUpdate(String statement) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2,Types.VARCHAR);
                ps.setString(3,currGameName);
                ps.setObject(4,currGame.game());
                ps.setString(5,currJSON);
                var gameid = ps.executeUpdate();
                setGameID(gameid);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to add to table: %s", ex.getMessage()));
        }
    }

    private final String[] createTable = {
            """
            CREATE TABLE IF NOT EXISTS games (
            `gameID` INT NOT NULL AUTO_INCREMENT,
            `white` VARCHAR(50),
            `black` VARCHAR(50),
            `gameName` VARCHAR(50) NOT NULL,
            `game` TEXT NOT NULL,
            `json` TEXT NOT NULL,
            PRIMARY KEY(`gameID`)
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String line : createTable) {
                try (var preppedStatement = conn.prepareStatement(line)) {
                    preppedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure table: %s", ex.getMessage()));
        }
    }
}
