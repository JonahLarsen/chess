package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
import webSocketMessages.userCommands.MakeMove;
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
      case LEAVE -> leave();
      case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);


    }
  }

  public void sendError(Session session, String errorMessage) {
    try {
      session.getRemote().sendString(new Gson().toJson(new Error(errorMessage)));
    } catch (IOException e) {
      return;
    }
  }

  public void leave() {}

  public void makeMove(MakeMove command, Session session) {
    try {
      AuthData authenticatedUser = authDAO.getAuth(command.getAuthString());
      GameData authenticatedGame = gameDAO.getGame(command.getGameID());
      if (authenticatedGame == null) {
        throw new DataAccessException("error", 500);
      }
      ChessGame.TeamColor playerColor;
      if (authenticatedGame.whiteUsername().equals(authenticatedUser.username())) {
        playerColor = ChessGame.TeamColor.WHITE;
      } else if (authenticatedGame.blackUsername().equals(authenticatedUser.username())) {
        playerColor = ChessGame.TeamColor.BLACK;
      } else {
        throw new DataAccessException("error", 500); //Player is an observer
      }
      ChessGame gameRep = authenticatedGame.game();
      ChessMove move = command.getMove();
      if (gameRep.getTeamTurn().equals(playerColor)) {
        gameRep.makeMove(move);
      } else {
        throw new DataAccessException("error", 500);
      }
      String message = String.format("%s moved their %s, from %s to %s",
              authenticatedUser.username(), gameRep.getBoard().getPiece(move.getEndPosition()).getPieceType().toString(),
              move.getStartPosition().toString(), move.getEndPosition().toString());
      Notification notification = new Notification(message);
      connections.broadcast(authenticatedUser.authToken(), notification);
      connections.broadcast("", new LoadGame(authenticatedGame));
    } catch (DataAccessException e) {
      sendError(session, "It is not your turn.");
    } catch (InvalidMoveException e) {
      sendError(session, "Error: Invalid move.");
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
    }
  }
}
