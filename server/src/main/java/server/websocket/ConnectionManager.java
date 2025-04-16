package server.websocket;

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

    public void broadcast(String excludeUser, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()){
            if (c.session.isOpen()){
                if (!c.username.equals(excludeUser)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        for (var c : removeList){
            connections.remove(c.username);
        }
    }
}
