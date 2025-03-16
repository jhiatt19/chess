package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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
    int gameID = 0;
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
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
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
    public HashSet<GameData> listGame() throws DataAccessException {
        var gamesList = new HashSet<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT json FROM games";
            try (var ps = conn.prepareStatement(stmt)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var json = rs.getString("json");
                        var game = new Gson().fromJson(json,GameData.class);
                        gamesList.add(game);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Problem getting games from database: %s",ex.getMessage()));
        }
        return gamesList;
    }

    @Override
    public void joinGame(JoinGameData color, String user) throws DataAccessException, SQLException, ResponseException {
        String stmt;
        if (color.playerColor().equals("WHITE") || color.playerColor().equals("WHITE/BLACK")) {
            stmt = "UPDATE games " + "SET whiteUsername = ? " + "WHERE gameID = ? AND whiteUsername IS NULL";
        }
        else if (color.playerColor().equals("BLACK")) {
            stmt = "UPDATE games " + "SET blackUsername = ? " + "WHERE gameID = ? AND blackUsername IS NULL";
        }
        else {
            throw new ResponseException(400, "Error: bad request");
        }

        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,user);
                ps.setInt(2,color.gameID());
                var affectedRows = ps.executeUpdate();
                if (affectedRows == 0){
                    throw new DataAccessException("Color already taken");
                }
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Could not join game: %s",ex.getMessage()));
        }
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
    public GameData findGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT json FROM games WHERE gameID = ?";
            var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,gameID);
            var rs = ps.executeQuery();

            if (rs.next()) {
                var jsonData = rs.getString(1);
                return new Gson().fromJson(jsonData,GameData.class);
            }
            else {
                throw new DataAccessException("Not a valid gameID");
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to find game: %s", ex.getMessage()));
        }
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
                ps.setString(4,currJSON);
                var gameid = ps.executeUpdate();
                setGameID(gameid);
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to add to table: %s", ex.getMessage()));
        }
    }

    private final String[] createGameTable = {
            """
            CREATE TABLE IF NOT EXISTS games (
            `gameID` INT NOT NULL AUTO_INCREMENT,
            `whiteUsername` VARCHAR(50),
            `blackUsername` VARCHAR(50),
            `gameName` VARCHAR(50) NOT NULL,
            `json` TEXT NOT NULL,
            PRIMARY KEY(`gameID`)
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (String line : createGameTable) {
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
