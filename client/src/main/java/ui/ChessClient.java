package ui;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {
  private final ServerFacade server;
  private final String serverURL;
  private State state = State.SIGNEDOUT;

  public enum State {
    SIGNEDOUT,
    SIGNEDIN
  }

  public ChessClient(String URL) {
    server = new ServerFacade(URL);
    serverURL = URL;
  }


  public String eval(String input) {
    try {
      var tokens = input.toLowerCase().split(" ");
      var cmd = (tokens.length > 0) ? tokens[0] : "help";
      var params = Arrays.copyOfRange(tokens, 1, tokens.length);
      return switch (cmd) {
        case "quit" -> quit();
        case "login" -> login();
        case "register" -> register();
        case "logout" -> logout();
        case "createGame" -> createGame();
        case "listGames" -> listGames();
        case "joinGame" -> joinGame();
        case "joinObserver" -> joinObserver();
        default -> help();
      };
    } catch (ResponseException e) {
      return e.getMessage();
    }
  }

  public String quit() throws ResponseException {
    return null;
  }

  public String login() throws ResponseException {
    return null;
  }

  public String register() throws ResponseException {
    return null;
  }

  public String logout() throws ResponseException {
    assertSignedIn();
    return null;
  }

  public String createGame() throws ResponseException {
    assertSignedIn();
    return null;
  }

  public String listGames() throws ResponseException {
    assertSignedIn();
    return null;
  }

  public String joinGame() throws ResponseException {
    assertSignedIn();
    return null;
  }

  public String joinObserver() throws ResponseException {
    assertSignedIn();
    return null;
  }

  public String help() {
    return null;
  }

  private void assertSignedIn() throws ResponseException {
    if (state == State.SIGNEDOUT) {
      throw new ResponseException(400, "You must sign in");
    }
  }
}
