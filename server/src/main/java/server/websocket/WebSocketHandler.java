package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
  private final ConnectionManager connections = new ConnectionManager();
  private final AuthDAO authDAO;
  private final GameDAO gameDAO;
  private UserDAO userDAO;

  public WebSocketHandler() throws DataAccessException {
    authDAO = new AuthDAOSQL();
    gameDAO = new GameDAOSQL();
    userDAO = new UserDAOSQL();
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    switch (command.getCommandType()) {
      case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
      case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
//      case LEAVE -> return;
//      case MAKE_MOVE -> return;


    }
  }

  public void sendError(Session session, String errorMessage) {
    try {
      session.getRemote().sendString(new Gson().toJson(new Error(errorMessage)));
    } catch (IOException e) {
      return;
    }
  }

  public void joinObserver(JoinObserver command, Session session) {
    try {
      AuthData authenticatedUser = authDAO.getAuth(command.getAuthString());
      GameData authenticatedGame = gameDAO.getGame(command.getGameID());
      if (authenticatedGame == null) {
        throw new DataAccessException("error", 500);
      }
      connections.add(authenticatedUser.authToken(), session);
      String message = String.format("%s joined the game \"%s\" as an observer.", authenticatedUser.username(), authenticatedGame.gameName());
      Notification notification = new Notification(message);
      connections.broadcast(command.getAuthString(), notification);
      session.getRemote().sendString(new Gson().toJson(new LoadGame(new GameData(1, null, null, null, null))));
    } catch (DataAccessException e) {
      sendError(session, "Error occurred trying to join game as observer.");
    } catch (IOException e) {
      return;
    }

  }

  public void joinPlayer(JoinPlayer command, Session session) {
    try {
      AuthData authenticatedUser = authDAO.getAuth(command.getAuthString());
      GameData authenticatedGame = gameDAO.getGame(command.getGameID());
      if (authenticatedGame == null) {
        throw new DataAccessException("error", 500);
      } else if ((command.getPlayerColor() == ChessGame.TeamColor.BLACK &&(authenticatedGame.blackUsername() == null || !authenticatedGame.blackUsername().equals(authenticatedUser.username())))
        || (command.getPlayerColor() == ChessGame.TeamColor.WHITE && (authenticatedGame.whiteUsername() == null || !authenticatedGame.whiteUsername().equals(authenticatedUser.username())))) {
        throw new ResponseException(500, "error");
      }
      connections.add(command.getAuthString(), session);
      String message = String.format("%s joined the game \"%s\" as the %s player.", authenticatedUser.username(), authenticatedGame.gameName(), command.getPlayerColor());
      Notification notification = new Notification(message);
      connections.broadcast(command.getAuthString(), notification);
      session.getRemote().sendString(new Gson().toJson(new LoadGame(new GameData(1, null, null, null, null))));
    } catch (DataAccessException e) {
      sendError(session, "Error occurred trying to join game as player.");
    } catch (ResponseException e) {
      sendError(session, "That color is already taken.");
    } catch (IOException e) {
      return;
    } catch (Throwable e) {
      return;
    }
  }
}
