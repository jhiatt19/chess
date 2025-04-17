package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ErrorResponse;
import exception.GameOverException;
import exception.ResponseException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.GameService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import services.AuthService;
import dataaccess.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService = new AuthService(new AuthSqlDataAccess());
    private final GameService gameService = new GameService(new GameSqlDataAccess());
    private final ArrayList<Session> sessionData = new ArrayList<>();
    private final Hashtable<Integer, ArrayList<Session>> gameSessions = new Hashtable<>();


    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getGameID());
                case MAKE_MOVE -> makeMove(session, username, new Gson().fromJson(msg, MakeMoveCommand.class));
                case LEAVE -> leaveGame(session, username, command.getGameID());
                case RESIGN -> resign(session, username, command.getGameID());
            }
        }
        catch (Exception ex) {
            sendMessage(session.getRemote(),new ErrorMessage(ex.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage error) throws IOException {
        var errorMessage = new Gson().toJson(error);
        remote.sendString(errorMessage);
    }

    private void connect(Session session, String username, int gameID) throws IOException, ResponseException, DataAccessException {
        connections.add(username, session, gameID);
        var game = gameService.getGame(gameID);
        String playerColor = "Observer";
        NotificationMessage serverMessage;
        if (game.blackUsername().equals(username)){
            playerColor = "BLACK";
            serverMessage = new NotificationMessage(String.format("%s has joined the game as BLACK",username));
        } else if (game.whiteUsername().equals(username)){
            playerColor = "WHITE";
            serverMessage = new NotificationMessage(String.format("%s has joined the game as WHITE",username));
        } else {
            serverMessage = new NotificationMessage(String.format("%s has joined as an observer",username));
        }
        connections.broadcast(username,serverMessage,gameID);
        LoadGameMessage loadGameMessage = new LoadGameMessage(game.game(),playerColor);
        connections.sendToSelf(username,loadGameMessage,gameID);
    }

    private String getUsername(String authToken) throws DataAccessException, ResponseException {
        var username = authService.checkAuth(authToken);
        return username.username();
    }

    private void saveSession(Integer gameID, Session session){
        var values = gameSessions.get(gameID);
        if (values == null){
            sessionData.add(session);
            gameSessions.put(gameID,sessionData);
            sessionData.clear();
        } else {
            values.add(session);
            gameSessions.replace(gameID, values);
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand msg)
            throws ResponseException, DataAccessException, InvalidMoveException, IOException, GameOverException {
        try {
            var move = msg.getMove();
            var id = msg.getGameID();
            var game = gameService.getGame(id);
            if (game.game().getGameCompleted()){
                throw new GameOverException("Game is over",game.game());
            }
            var blackUser = "";
            var whiteUser = "";
            if (game.blackUsername() != null){
                blackUser = game.blackUsername();
            }
            if (game.whiteUsername() != null) {
                whiteUser = game.whiteUsername();
            }
            if (!blackUser.equals(username) && !whiteUser.equals(username)){
                throw new InvalidMoveException("Cannot make move as an observer");
            }
            if ((blackUser.equals(username) && !game.game().getTeamTurn().equals(ChessGame.TeamColor.BLACK))
                    || (whiteUser.equals(username) && !game.game().getTeamTurn().equals(ChessGame.TeamColor.WHITE))){
                throw new InvalidMoveException("Not your turn, wait till opponent moves");
            }
            var validMove = false;
            var validMoves = game.game().validMoves(move.getStartPosition());
            for (var m : validMoves){
                if (m.getEndPosition().equals(move.getEndPosition())){
                    validMove = true;
                }
            }
            if (!validMove){
                throw new InvalidMoveException("Invalid move, try another move");
            }
            String playerColor = game.game().getTeamTurn().toString();
            game.game().makeMove(move);
            gameService.updateGame(id, "MOVE", game.game());
            var moveMessage = new LoadGameMessage(game.game(),playerColor);
            connections.broadcastAll(moveMessage, game.gameID());

            if (game.game().isInCheckmate(game.game().getTeamTurn())) {
                var finishedGame = new NotificationMessage(String.format("Game is over- %s is in checkmate", game.game().getTeamTurn()));
                connections.broadcastAll(finishedGame, game.gameID());
            } else if (game.game().isInCheck(game.game().getTeamTurn())) {
                var checkMessage = new NotificationMessage(String.format("%s is in check", game.game().getTeamTurn()));
                connections.broadcastAll(checkMessage, game.gameID());
            } else if (game.game().isInStalemate(game.game().getTeamTurn())){
                var staleMate = new NotificationMessage("Game is over- STALEMATE");
                connections.broadcastAll(staleMate, game.gameID());
            }

            var moveNotification = new NotificationMessage(String.format("%s made move " + move.toString(), username));
            connections.broadcast(username, moveNotification, game.gameID());

        } catch (Exception ex){
            sendMessage(session.getRemote(),new ErrorMessage(ex.getMessage()));
        }
    }

    private void leaveGame(Session session, String username, int gameID) throws IOException, ResponseException, DataAccessException {
        var game = gameService.getGame(gameID);
        if(game.whiteUsername() != null  && game.whiteUsername().equals(username)){
            gameService.updateGame(gameID,"WHITE",game.game());
        }
        if(game.blackUsername() != null && game.blackUsername().equals(username)){
            gameService.updateGame(gameID,"BLACK",game.game());
        }
        connections.remove(username,gameID);
        NotificationMessage leave = new NotificationMessage(String.format("%s has left the game",username));
        connections.broadcast(username,leave,gameID);
        var values = gameSessions.get(gameID);
        values.remove(session);
        gameSessions.replace(gameID,values);
    }

    private void resign(Session session, String username, int gameID)
            throws ResponseException, DataAccessException, InvalidMoveException, IOException {
        var game = gameService.getGame(gameID);
        if (!game.whiteUsername().equals(username) && !game.blackUsername().equals(username)){
            throw new InvalidMoveException("Cannot resign as the observer");
        }
        if (game.game().getGameCompleted()){
            throw new InvalidMoveException("Game is finished");
        }
        gameService.updateGame(gameID,"RESIGN",game.game());
        var resignMessage = new NotificationMessage(String.format("%s has resigned",username));
        connections.broadcastAll(resignMessage,gameID);

    }

}
