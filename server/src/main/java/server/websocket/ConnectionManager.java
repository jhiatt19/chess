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
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID){
        var connection = new Connection(username, session, gameID);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcastAll(ServerMessage notification) throws IOException {
        for (var c : connections.values()){
            if (c.session.isOpen()) {
                var note = new Gson().toJson(notification);
                c.send(note);
            }
        }
    }
    public void broadcast(String excludeUser, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
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

    public void sendToSelf(String user, ServerMessage notification) throws IOException {
        for (var c : connections.values()){
            if (c.session.isOpen()){
                if(c.username.equals(user)){
                    var note = new Gson().toJson(notification);
                    c.send(note);
                }
            }
        }
    }
}
