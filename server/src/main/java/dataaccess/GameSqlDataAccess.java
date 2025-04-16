package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.JoinGameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class GameSqlDataAccess implements GameDAO{
    String currGameName;
    String currJSON;
    int gameID;
    GameData currGame;
    List<GameData> gamesList = new ArrayList<>();
    public GameSqlDataAccess() {
        try {
            configureDatabase();
            gameID = this.size();
        }
        catch (DataAccessException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public int createGame(String name) throws DataAccessException, ResponseException {
        if (name == null){
            throw new ResponseException(400,"Error: bad request");
        }
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, json) VALUES (?, ?, ?, ?)";
        this.currGameName = name;
        this.currGame = new GameData(gameID+1, null, null, name, new ChessGame());
        this.currJSON = new Gson().toJson(currGame);
        executeUpdate(statement);
        return gameID;
    }

    public GameData updateGame(int gameID, GameData updatedGame, String color) throws DataAccessException, ResponseException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            String updatestmt = "";
            if (color.equals("WHITE")) {
                updatestmt = "UPDATE games SET whiteUsername = NULL, json = ? WHERE gameID = ?";
            } else if (color.equals("BLACK")) {
                updatestmt = "UPDATE games SET blackUsername = NULL, json = ? WHERE gameID = ?";
            } else  if (color.equals("MOVE") || color.equals("RESIGN")){
                updatestmt = "UPDATE games SET json = ? WHERE gameID = ?";
            }
            try (var updateStmt = conn.prepareStatement(updatestmt, Statement.RETURN_GENERATED_KEYS)) {
                updateStmt.setString(1, new Gson().toJson(updatedGame));
                updateStmt.setInt(2, gameID);
                updateStmt.executeUpdate();
                return updatedGame;
            } catch (Exception ex) {
                throw new ResponseException(400, ex.getMessage());
            }
        }
    }

    private void setGameID(int gameID) {
        this.gameID = gameID;
    }
    @Override
    public List<GameData> listGame() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var stmt = "SELECT gameID, json FROM games";
            try (var ps = conn.prepareStatement(stmt)) {
                try (var rs = ps.executeQuery()) {
                    gamesList.clear();
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
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
        try (var conn = DatabaseManager.getConnection()) {
        String stmt;
        String stringJson;
        GameData newGameData;
        gamesList.clear();
        var pullStmt = "SELECT json FROM games WHERE gameID = ?";
            try (var pullSt = conn.prepareStatement(pullStmt, Statement.RETURN_GENERATED_KEYS)) {
                pullSt.setInt(1,color.gameID());
                var rs = pullSt.executeQuery();
                if (rs.next()) {
                    var jsonString = rs.getString("json");
                    newGameData = new Gson().fromJson(jsonString, GameData.class);
                }
                else {
                    throw new SQLException("No json string");
                }
            } catch (SQLException ex) {
                throw new DataAccessException(String.format("Problem getting json: %s", ex.getMessage()));
            }
            if (color.playerColor().equals("WHITE") || color.playerColor().equals("WHITE/BLACK")) {
                var gameData = new GameData(newGameData.gameID(),user, newGameData.blackUsername(), newGameData.gameName(),newGameData.game());
                stringJson = new Gson().toJson(gameData);
                stmt = "UPDATE games " + "SET whiteUsername = ?, json = ? " + "WHERE gameID = ? AND whiteUsername IS NULL";
            }
            else if (color.playerColor().equals("BLACK")) {
                var gameData = new GameData(newGameData.gameID(), newGameData.whiteUsername(), user, newGameData.gameName(),newGameData.game());
                stringJson = new Gson().toJson(gameData);
                stmt = "UPDATE games " + "SET blackUsername = ?, json = ? " + "WHERE gameID = ? AND blackUsername IS NULL";
            }
            else {
                throw new ResponseException(400, "Error: bad request");
            }
            try (var ps = conn.prepareStatement(stmt, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1,user);
                ps.setString(2,stringJson);
                ps.setInt(3,color.gameID());
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
            setGameID(0);
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
                var rowsAffected = ps.executeUpdate();
                if (rowsAffected != 0) {
                    var gameid = ps.getGeneratedKeys() ;
                        if (gameid.next()) {
                            var newGameID = gameid.getInt(1);
                            setGameID(newGameID);
                    }
                }

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
