package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.ChessBoard;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.Leave;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
  Session session;
  NotificationHandler notificationHandler;
  ChessGame.TeamColor teamColor;


  public WebSocketFacade(String url, NotificationHandler notificationHandler, ChessGame.TeamColor teamColor) throws ResponseException {
    this.teamColor = teamColor;
    try {
      url = url.replace("http", "ws");
      URI socketURI = new URI(url + "/connect");
      this.notificationHandler = notificationHandler;

      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, socketURI);

      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
          if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            Error errorMessage = new Gson().fromJson(message, Error.class);
            notificationHandler.receiveServerMessage(errorMessage.getErrorMessage());
          } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            Notification notifMessage = new Gson().fromJson(message, Notification.class);
            notificationHandler.receiveServerMessage(notifMessage.getMessage());
          } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            LoadGame loadGameMessage = new Gson().fromJson(message, LoadGame.class);
            if (teamColor == ChessGame.TeamColor.BLACK) {
              ChessBoard.drawChessBoard(loadGameMessage.getGame().game().getBoard(), "BLACK" );
            } else {
              ChessBoard.drawChessBoard(loadGameMessage.getGame().game().getBoard(),"WHITE");
            }

          }
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(ex.getMessage());
    }
  }


  public void leave(String authToken, int gameID) throws ResponseException {
    try {
      Leave message = new Leave(authToken, gameID);
      this.session.getBasicRemote().sendText(new Gson().toJson(message));
      this.session.close();
    } catch (IOException ex) {
      throw new ResponseException(ex.getMessage());
    }
  }
  public void joinGame(String authToken, int gameID) throws ResponseException {
    try {
      if (this.teamColor == null) {
        JoinObserver message = new JoinObserver(authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(message));
      } else {
        JoinPlayer message = new JoinPlayer(authToken, teamColor, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(message));
      }
    } catch (IOException e) {
      throw new ResponseException(e.getMessage());
    }
  }

  //Endpoint requires this method, but you don't have to do anything
  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {
  }
}
