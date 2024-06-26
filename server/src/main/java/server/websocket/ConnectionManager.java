package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
  public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

  public void add(String authToken, Session session, int gameID) {
    var connection = new Connection(authToken, session, gameID);
    connections.put(authToken, connection);
  }

  public void remove(String authToken) {
    connections.remove(authToken);
  }

  public void broadcast(String excludeAuthToken, ServerMessage message, int gameID) throws IOException {
    var removeList = new ArrayList<Connection>();
    for (var c : connections.values()) {
      if (c.session.isOpen()) {
        if (!c.authToken.equals(excludeAuthToken) && c.gameID == gameID) {
          c.send(message.toString());
        }
      } else {
        removeList.add(c);
      }
    }

    // Clean up any connections that were left open.
    for (var c : removeList) {
      connections.remove(c.authToken);
    }
  }
}
