package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ErrorResponse;
import exception.ResponseException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import services.GameService;
import websocket.commands.UserGameCommand;
import services.AuthService;
import dataaccess.*;
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
    public void onMessage(Session session, String msg) {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(),session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getGameID());
                case MAKE_MOVE -> makeMove(session, username, UserGameCommand.CommandType.MAKE_MOVE);
                case LEAVE -> leaveGame(session, username, command.getGameID());
                case RESIGN -> resign(session, username, UserGameCommand.CommandType.RESIGN);
            }
        } catch (ResponseException ex) {
            ex.printStackTrace();
            System.out.println("Error: unauthorized");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    private void connect(Session session, String username, int gameID) throws IOException, ResponseException, DataAccessException {
        connections.add(username, session, gameID);
        var game = gameService.getGame(gameID);
        NotificationMessage serverMessage;
        if (game.blackUsername().equals(username)){
            serverMessage = new NotificationMessage(String.format("%s has joined the game as BLACK",username));
        } else if (game.whiteUsername().equals(username)){
            serverMessage = new NotificationMessage(String.format("%s has joined the game as WHITE",username));
        } else {
            serverMessage = new NotificationMessage(String.format("%s has joined as an observer",username));
        }
        connections.broadcast(username,serverMessage);
        LoadGameMessage loadGameMessage = new LoadGameMessage(game.game());
        connections.sendToSelf(username,loadGameMessage);
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

    private void makeMove(Session session, String username, UserGameCommand.CommandType command){

    }

    private void leaveGame(Session session, String username, int gameID){
        connections.remove(username);
    }

    private void resign(Session session, String username, UserGameCommand.CommandType command){

    }

}
