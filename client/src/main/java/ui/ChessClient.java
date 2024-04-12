package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.glassfish.grizzly.http.server.Response;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.io.IOException;
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
  private int currentGameID;
  private final NotificationHandler notificationHandler;
  private String playerColorString;
  private final HashMap<String, Integer> boardLetterMap = new HashMap<>();
  public enum State {
    SIGNEDOUT,
    SIGNEDIN,
    INGAME,
    RESIGN
  }

  public ChessClient(String url, NotificationHandler notificationHandler) {
    server = new ServerFacade(url);
    serverURL = url;
    this.notificationHandler = notificationHandler;
    boardLetterMap.put("a", 1);
    boardLetterMap.put("b", 2);
    boardLetterMap.put("c", 3);
    boardLetterMap.put("d", 4);
    boardLetterMap.put("e", 5);
    boardLetterMap.put("f", 6);
    boardLetterMap.put("g", 7);
    boardLetterMap.put("h", 8);
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
        case "leave" -> leave();
        case "resign" -> resign();
        case "confirm" -> confirmResign();
        case "cancel" -> cancelResign();
        case "redraw" -> redraw();
        case "makemove" -> makemove(params);
        case "highlight" ->
        default -> help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  public String highlight(String... params) throws ResponseException{
    assertInGame();
    if (params.length != 2) {
      throw new ResponseException("Error: Expected highlight <STARTING_POSITION>");
    }

  }

  public String makemove(String... params) throws ResponseException {
    assertInGame();
    if (params.length < 4) {
      throw new ResponseException("Error: Expected makemove <STARTING_POSITION> <NEW_POSITION>");
    }
    int startColumn = boardLetterMap.get(params[0]);
    int startRow = Integer.parseInt(params[1]);
    int endColumn = boardLetterMap.get(params[2]);
    int endRow = Integer.parseInt(params[3]);
    ChessPiece.PieceType promotionType;
    if ((endRow == 8 && playerColorString.equals("WHITE")) || (endRow == 1 && playerColorString.equals("BLACK")) ) {
      promotionType = ChessPiece.PieceType.valueOf(params[4].toUpperCase());
    } else {
      promotionType = null;
    }
    ChessPosition startPosition = new ChessPosition(startRow, startColumn);
    ChessPosition endPosition = new ChessPosition(endRow, endColumn);
    ChessMove move = new ChessMove(startPosition, endPosition, promotionType);
    socket.makeMove(currentUser.authToken(),currentGameID, move);
    return "";
  }
  public String redraw() throws ResponseException{
    assertInGame();
    socket.redraw(playerColorString);
    return "";
  }

  public String cancelResign() throws ResponseException {
    assertResign();
    state = State.INGAME;
    return "Resign cancelled";
  }
  public String confirmResign() throws ResponseException {
    assertResign();
    socket.resign(currentUser.authToken(), currentGameID);
    return "";
  }
  public String resign() throws ResponseException {
    assertInGame();
    state = State.RESIGN;
    return "Are you sure you want to resign? Type 'confirm' if yes or 'cancel' if not.";
  }

  public String leave() throws ResponseException {
    assertInGame();
    state = State.SIGNEDIN;
    socket.leave(currentUser.authToken(), currentGameID);
    return "";
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
    int gameID = idMap.get(gameListID);
    currentGameID = gameID;
    if (params.length > 1) {
      playerColorString = params[1].toUpperCase();
      server.joinGame(gameID, playerColorString, currentUser);
    } else {
      playerColorString = null;
      server.joinGame(gameID, null, currentUser);
    }
    if (playerColorString == null) {
      socket = new WebSocketFacade(serverURL, notificationHandler, null);
    } else if (playerColorString.equals("BLACK")) {
      socket = new WebSocketFacade(serverURL, notificationHandler, ChessGame.TeamColor.BLACK);
    } else if (playerColorString.equals("WHITE")) {
      socket = new WebSocketFacade(serverURL, notificationHandler, ChessGame.TeamColor.WHITE);
    }

    socket.joinGame(currentUser.authToken(), gameID);
    state = State.INGAME;

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
    } else if (state.equals(State.INGAME)) {
      return """
              'help' - List available commands
              'redraw' - Redraw the chess board
              'leave' - Leave the game
              'makemove <STARTING_POSITION> <NEW_POSITION> [PROMOTION_PIECE]' - Move piece at starting position to new position (Positions should follow patter: b 5). Only include promotion piece if valid. 
              'resign' - Resign from game
              'highlight <STARTING_POSITION>' - Highlights available moves for piece at starting position (Positions should follow patter: b 5)
              """;
    } else {
      return """
              'confirm' - Confirm that you want to resign
              'cancel' - Cancel your resign
              """;
    }
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.SIGNEDOUT) {
      throw new ResponseException("You must sign in");
    }
  }

  private void assertResign() throws ResponseException {
    if (state != State.RESIGN) {
      throw new ResponseException("Request to resign first");
    }
  }

  private void assertSignedOut() throws ResponseException {
    if (state == State.SIGNEDIN) {
      throw new ResponseException("Already signed in");
    }
  }

  private void assertInGame() throws ResponseException {
    if (state != State.INGAME) {
      throw new ResponseException("Must login first");
    }
  }
}
