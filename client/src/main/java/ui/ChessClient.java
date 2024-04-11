package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;
import java.util.HashMap;

import static ui.ChessBoard.drawChessBoard;

public class ChessClient {
  private final ServerFacade server;
  private  WebSocketFacade socket;
  private final String serverURL;
  private State state = State.SIGNEDOUT;
  private AuthData currentUser;
  private HashMap<Integer, Integer> idMap= new HashMap<>();
  private final NotificationHandler notificationHandler;
  public enum State {
    SIGNEDOUT,
    SIGNEDIN,
    INGAME
  }

  public ChessClient(String url, NotificationHandler notificationHandler) {
    server = new ServerFacade(url);
    serverURL = url;
    this.notificationHandler = notificationHandler;
  }


  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "quit" -> "quit";
        case "login" -> login(params);
        case "register" -> register(params);
        case "logout" -> logout();
        case "creategame" -> createGame(params);
        case "listgames" -> listGames();
        case "joingame", "observe" -> joinGame(params);
        default -> help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  public String login(String... params) throws ResponseException {
    assertSignedOut();
    if (params.length >= 2) {
      String username = params[0];
      String password = params[1];
      UserData user = new UserData(username, password, null);
      currentUser = server.login(user);
      state = State.SIGNEDIN;
      return "Successfully Logged In";
    }
    throw new ResponseException("Expected <USERNAME> <PASSWORD>");
  }

  public String register(String... params) throws ResponseException {
    assertSignedOut();
    if (params.length >= 3) {
      String username = params[0];
      String password = params[1];
      String email = params[2];
      UserData user = new UserData(username, password, email);
      currentUser = server.register(user);
      state = State.SIGNEDIN;
      return "Successfully Registered new account and logged in.";
    } else {
      throw new ResponseException("Expected <USERNAME> <PASSWORD> <EMAIL>");
    }
  }

  public String logout() throws ResponseException {
    assertSignedIn();
    server.logout(currentUser);
    currentUser = null;
    state = State.SIGNEDOUT;
    return "Successfully Logged Out";
  }

  public String createGame(String... params) throws ResponseException {
    assertSignedIn();
    String gameName = params[0];
    GameData game = new GameData(0, null, null, gameName, null);
    GameData createdGame = server.createGame(currentUser, game);
    return String.valueOf(createdGame.gameID());
  }

  public String listGames() throws ResponseException {
    assertSignedIn();
    idMap.clear();
    var games = server.listGames(currentUser);
    var result = new StringBuilder();
    int gameListID = 1;
    for (var game : games) {
      result.append(gameListID).append(": ").append("GameID: ").append(game.gameID())
              .append(", Game Name: ").append(game.gameName()).append(", Black Player: ").append(game.blackUsername())
              .append(", White Player: ").append(game.whiteUsername()).append("\n");
      idMap.put(gameListID, game.gameID());
      gameListID++;
    }
    return result.toString();
  }

  public String joinGame(String... params) throws ResponseException {
    assertSignedIn();
    if (params.length == 0) {
      throw new ResponseException("Expected <ID> [WHITE|BLACK|<empty>]");
    }
    int gameListID=Integer.parseInt(params[0]);
    int gameID =idMap.get(gameListID);
    String playerColorString;
    if (params.length > 1) {
      playerColorString = params[1].toUpperCase();
      server.joinGame(gameID, playerColorString, currentUser);
    } else {
      playerColorString = null;
      server.joinGame(gameID, null, currentUser);
      state = State.INGAME;
    }
    if (playerColorString == "BLACK") {
      socket = new WebSocketFacade(serverURL, notificationHandler, ChessGame.TeamColor.BLACK);
    } else if (playerColorString == "WHITE") {
      socket = new WebSocketFacade(serverURL, notificationHandler, ChessGame.TeamColor.WHITE);
    } else {
      socket = new WebSocketFacade(serverURL, notificationHandler, null);
    }

    socket.joinGame(currentUser.authToken(), gameID);


    return "Successfully Joined Game";
  }


  public String help() {
    if (state.equals(State.SIGNEDIN)) {
      return """
              'help' - List available commands
              'logout' - Logout of Chess
              'createGame <NAME>' - Create a new Chess game
              'listGames' - List all Chess games
              'joinGame <ID> [WHITE|BLACK|<empty>]' - Join a Chess game
              'observe <ID>' Observe a Chess game
              """;
    } else if (state.equals(State.SIGNEDOUT)) {
      return """
              'help' - List available commands
              'quit' - Exit the Chess program
              'login <USERNAME> <PASSWORD>' - Login to Chess account
              'register <USERNAME> <PASSWORD> <EMAIL>' - Create a Chess account
              """;
    } else {
      return """
              'help' - List available commands
              'redraw' - Redraw the chess board
              'leave' - Leave the game
              'makemove <STARTING_POSITION> <NEW_POSITION>' - Move piece at starting position to new position
              'resign' - Resign from game
              'highlight <STARTING_POSITION>' - Highlights available moves for piece at starting position
              """;
    }
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.SIGNEDOUT) {
      throw new ResponseException("You must sign in");
    }
  }

  private void assertSignedOut() throws ResponseException {
    if (state == State.SIGNEDIN) {
      throw new ResponseException("Already signed in");
    }
  }

  private void assertInGame() throws ResponseException {
    if (state == State.INGAME) {
      throw new ResponseException("Player is in game");
    }
  }
}
