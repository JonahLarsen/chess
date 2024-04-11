package server.websocket;

import com.google.gson.Gson;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
//    joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
    switch (command.getCommandType()) {
      case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
//      case JOIN_PLAYER -> exit(action.visitorName());
//      case LEAVE -> return;
//      case MAKE_MOVE -> return;


    }
  }

  public void joinObserver(JoinObserver command, Session session) {
    try {
      connections.add(command.getAuthString(), session);
      String message = String.format("woohoo");
      Notification notification = new Notification(message);
      connections.broadcast(command.getAuthString(), notification);
      session.getRemote().sendString(new Gson().toJson(new LoadGame(new GameData(1, null, null, null, null))));
    } catch (Throwable e) {
      return;
    }

  }
}
