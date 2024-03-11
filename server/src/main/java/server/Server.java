package server;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.GamesListWrapper;
import service.UserService;
import spark.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collection;

import model.GameData;


public class Server {
    private AuthService authService;
    private GameService gameService;
    private UserService userService;



  public Server() {
//    this.authService = new AuthService(new AuthDAOMemory());
//    this.userService = new UserService(new UserDAOMemory());
//    this.gameService = new GameService(new GameDAOMemory());
    try {
      this.authService = new AuthService(new AuthDAOSQL());
      this.userService = new UserService(new UserDAOMemory());
      this.gameService = new GameService(new GameDAOMemory());
    } catch (DataAccessException e) {
      System.out.println("Unable to instantiate Server");
    }

  }

    public static void main(String[] args) {
      Server server = new Server();
      int currentPort = server.run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearHandler);
        Spark.post("/user", this::registerHandler);
        Spark.post("/session", this::loginHandler);
        Spark.delete("/session", this::logoutHandler);
        Spark.get("/game", this::listGamesHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(DataAccessException e, Request req, Response res) {
        res.status(e.getStatusCode());
        switch(e.getStatusCode()) {
          case 400:
            res.body("{\"message\": \"Error: bad request\"}");
            break;
          case 401:
            res.body("{\"message\": \"Error: unauthorized\"}");
            break;
          case 403:
            res.body("{\"message\": \"Error: already taken\"}");
            break;
          default:
            res.status(500);
            res.body("{\"message\": \"Error: description\"}");
        }

    }
    private Object joinGameHandler(Request req, Response res) throws DataAccessException {
      String authToken = req.headers("authorization");
      this.authService.getAuth(authToken);
      JoinGameObject joinGame = new Gson().fromJson(req.body(), JoinGameObject.class);
      if (joinGame.playerColor() == null) {
        //TODO: Add player as watcher
      } else if (!joinGame.playerColor().equals("BLACK") && !joinGame.playerColor().equals("WHITE")) {
        throw new DataAccessException("Error", 400);
      }
      String username = this.authService.getAuth(authToken).username();
      this.gameService.joinGame(username, joinGame.playerColor(), joinGame.gameID());
      res.status(200);
      return "{}";
    }
    private Object createGameHandler(Request req, Response res) throws DataAccessException {
      String authToken = req.headers("authorization");
      var newGame = new Gson().fromJson(req.body(), GameData.class);
      this.authService.getAuth(authToken);
      int gameID = this.gameService.createGame(newGame.gameName());
      GameData createdGame = new GameData(gameID, null, null, null, null);
      res.status(200);
      return new Gson().toJson(createdGame);

    }
    private Object listGamesHandler(Request req, Response res) throws DataAccessException {
      String authToken = req.headers("authorization");
      this.authService.getAuth(authToken);
      Collection<GameData> games = this.gameService.listGames();
      GamesListWrapper wrappedGames = new GamesListWrapper(games);
      res.status(200);

      return new Gson().toJson(wrappedGames);

    }
    private Object logoutHandler(Request req, Response res) throws DataAccessException {
      String authToken = req.headers("authorization");
      this.authService.logout(authToken);
      res.status(200);
      return "{}";
    }
    private Object loginHandler(Request req, Response res) throws DataAccessException {
      var user = new Gson().fromJson(req.body(), UserData.class);
      AuthData auth = this.userService.login(user);
      this.authService.createAuth(auth);
      res.status(200);
      return new Gson().toJson(auth);
    }
    private Object clearHandler(Request req, Response res) throws DataAccessException {
        authService.deleteAllAuth();
        userService.deleteAllUsers();
        gameService.deleteAllGames();
        res.status(200);
        return "{}";
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
      var newUser = new Gson().fromJson(req.body(), UserData.class);
      if (newUser.username() == null || newUser.password() == null || newUser.email() == null) {
        throw new DataAccessException("", 400);
      }
      AuthData auth = this.userService.register(newUser);
      this.authService.createAuth(auth);
      res.status(200);
      return new Gson().toJson(auth);

    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
