package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ErrorResponse;
import exception.ResponseException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import services.AuthService;
import dataaccess.*;
import websocket.messages.ServerMessage;


import javax.management.Notification;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService = new AuthService(new AuthSqlDataAccess());

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(),session);
            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command.getGameID());
                case MAKE_MOVE -> makeMove(session, username, UserGameCommand.CommandType.MAKE_MOVE);
                case LEAVE -> leaveGame(session, username, UserGameCommand.CommandType.LEAVE);
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

    private void connect(Session session, String username, int gameID) throws IOException {
        connections.add(username, session, gameID);
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(username,serverMessage);

    }

    private String getUsername(String authToken) throws DataAccessException, ResponseException {
        var username = authService.checkAuth(authToken);
        return username.username();
    }

    private void saveSession(Integer gameID, Session session){
    }

    private void makeMove(Session session, String username, UserGameCommand.CommandType command){

    }

    private void leaveGame(Session session, String username, UserGameCommand.CommandType command){

    }

    private void resign(Session session, String username, UserGameCommand.CommandType command){

    }

}
