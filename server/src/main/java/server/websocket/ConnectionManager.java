package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<Integer, ConcurrentHashMap<String,Connection>> gameConnections = new ConcurrentHashMap<>();
    //public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID){
        if (gameConnections.get(gameID) == null){
            gameConnections.put(gameID,new ConcurrentHashMap<>());
        }
        var connection = new Connection(username, session, gameID);
        var specGame = gameConnections.get(gameID);
        specGame.put(username,connection);
        gameConnections.replace(gameID,specGame);
    }

    public void remove(String username, int gameID) {
        var specGame = gameConnections.get(gameID);
        specGame.remove(username);

    }

    public void broadcastAll(ServerMessage notification, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        var connections = gameConnections.get(gameID);
        for (var c : connections.values()){
            if (c.session.isOpen()) {
                var note = new Gson().toJson(notification);
                c.send(note);
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList){
            connections.remove(c.username);
        }
    }
    public void broadcast(String excludeUser, ServerMessage notification, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        var connections = gameConnections.get(gameID);
        for (var c : connections.values()){
            if (c.session.isOpen()){
                if (!c.username.equals(excludeUser)) {
                    var note = new Gson().toJson(notification);
                    c.send(note);
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList){
            connections.remove(c.username);
        }
    }

    public void sendToSelf(String user, ServerMessage notification, int gameID) throws IOException {
        var removeList = new ArrayList<Connection>();
        var connections = gameConnections.get(gameID);
        for (var c : connections.values()){
            if (c.session.isOpen()){
                if(c.username.equals(user)){
                    var note = new Gson().toJson(notification);
                    c.send(note);
                }
            }else {
                removeList.add(c);
            }
        }
        for (var c : removeList){
            connections.remove(c.username);
        }
    }
}
