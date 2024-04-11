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
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
  private final ConcurrentHashMap<Integer, ConnectionManager> gameManagers = new ConcurrentHashMap<>();
  private final ConnectionManager connections = new ConnectionManager();
  private final AuthDAO authDAO;
  private final GameDAO gameDAO;

  public WebSocketHandler() throws DataAccessException {
    authDAO = new AuthDAOSQL();
    gameDAO = new GameDAOSQL();
  }

  @OnWebSocketMessage
  public void onMessage(Session session, String message) throws IOException {
    UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
    switch (command.getCommandType()) {
      case JOIN_OBSERVER -> joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
      case JOIN_PLAYER -> joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
      case LEAVE -> leave(new Gson().fromJson(message, Leave.class), session);
      case MAKE_MOVE -> makeMove(new Gson().fromJson(message, MakeMove.class), session);
      case RESIGN -> resign(new Gson().fromJson(message, Resign.class), session);
    }
  }

  public void sendError(Session session, String errorMessage) {
    try {
      session.getRemote().sendString(new Gson().toJson(new Error(errorMessage)));
    } catch (IOException e) {
      return;
    }
  }

  public void resign(Resign command, Session session) {
    try {
      AuthData authenticatedUser = authDAO.getAuth(command.getAuthString());
      GameData authenticatedGame = gameDAO.getGame(command.getGameID());
      if (authenticatedGame == null) {
        throw new DataAccessException("error", 500);
      }
      getPlayerColor(authenticatedGame, authenticatedUser);
      ChessGame gameRep = gameDAO.getGame(authenticatedGame.gameID()).game();
      if (gameRep.getTeamTurn().equals(ChessGame.TeamColor.NIL)) {
        throw new IllegalArgumentException();
      }
      gameRep.setTeamTurn(ChessGame.TeamColor.NIL);
      gameDAO.updateGame(authenticatedGame.whiteUsername(), authenticatedGame.blackUsername(),
              gameRep, authenticatedGame.gameID());
      String message = String.format("%s resigned from the game \"%s\".", authenticatedUser.username(), authenticatedGame.gameName());
      Notification notification = new Notification(message);
      connections.broadcast("", notification);
    } catch (DataAccessException e) {
      sendError(session, "Error: Cannot resign from game with provided credentials");
    } catch (IOException e) {
      return;
    } catch (IllegalArgumentException e) {
      sendError(session, "Error: Game is over.");
    }
  }

  public void leave(Leave command, Session session) {
    try {
      AuthData authenticatedUser = authDAO.getAuth(command.getAuthString());
      GameData authenticatedGame = gameDAO.getGame(command.getGameID());
      if (authenticatedGame == null) {
        throw new DataAccessException("error", 500);
      }
      if (authenticatedGame.whiteUsername() != null &&
              authenticatedGame.whiteUsername().equals(authenticatedUser.username())) {
        gameDAO.updateGame(null, authenticatedGame.blackUsername(),
                authenticatedGame.game(), authenticatedGame.gameID());
      } else if (authenticatedGame.blackUsername() != null &&
              authenticatedGame.blackUsername().equals(authenticatedUser.username())) {
        gameDAO.updateGame(authenticatedGame.whiteUsername(), null,
                authenticatedGame.game(), authenticatedGame.gameID());
      }
      connections.remove(command.getAuthString());
      String message = String.format("%s left the game \"%s\".", authenticatedUser.username(), authenticatedGame.gameName());
      Notification notification = new Notification(message);
      connections.broadcast(command.getAuthString(), notification);
    } catch (DataAccessException e) {
      sendError(session, "Error: Unable to remove user with provided credentials from game.");
    } catch (IOException e) {
      return;
    }
  }

  public void makeMove(MakeMove command, Session session) {
    try {
      AuthData authenticatedUser = authDAO.getAuth(command.getAuthString());
      GameData authenticatedGame = gameDAO.getGame(command.getGameID());
      if (authenticatedGame == null) {
        throw new DataAccessException("error", 500);
      }
      ChessGame.TeamColor playerColor = getPlayerColor(authenticatedGame, authenticatedUser);
      ChessGame gameRep = authenticatedGame.game();
      ChessMove move = command.getMove();
      if (gameRep.getTeamTurn().equals(ChessGame.TeamColor.NIL)) {
        throw new IllegalArgumentException();
      } else if (gameRep.getTeamTurn().equals(playerColor)) {
        gameRep.makeMove(move);
        gameDAO.updateGame(authenticatedGame.whiteUsername(), authenticatedGame.blackUsername(),
                gameRep, authenticatedGame.gameID());
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
      sendError(session, "Error: It is not your turn.");
    } catch (InvalidMoveException e) {
      sendError(session, "Error: Invalid move.");
    } catch (IOException e) {
      return;
    } catch (IllegalArgumentException e) {
      sendError(session, "Error: Game is over.");
    }
  }

  private static ChessGame.TeamColor getPlayerColor(GameData authenticatedGame, AuthData authenticatedUser) throws DataAccessException {
    ChessGame.TeamColor playerColor;
    if (authenticatedGame.whiteUsername() != null &&
            authenticatedGame.whiteUsername().equals(authenticatedUser.username())) {
      playerColor = ChessGame.TeamColor.WHITE;
    } else if (authenticatedGame.blackUsername() != null &&
            authenticatedGame.blackUsername().equals(authenticatedUser.username())) {
      playerColor = ChessGame.TeamColor.BLACK;
    } else {
      throw new DataAccessException("error", 500); //Player is an observer
    }
    return playerColor;
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
      sendError(session, "Error: That color is already taken.");
    } catch (IOException e) {
      return;
    }
  }
}