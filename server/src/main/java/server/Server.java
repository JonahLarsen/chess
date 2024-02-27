package server;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import com.google.gson.Gson;

import javax.xml.crypto.Data;

public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;


  public Server() {
    AuthDAO authDAO = new AuthDAOMemory();
    GameDAO gameDAO = new GameDAOMemory();
    UserDAO userDAO = new UserDAOMemory();

    this.authService = new AuthService(authDAO);
    this.gameService = new GameService(gameDAO);
    this.userService = new UserService(userDAO);
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
    private Object logoutHandler(Request req, Response res) throws DataAccessException {
      var user = new Gson().fromJson(req.body(), UserData.class);
      this.userService.logout()
    }
    private Object loginHandler(Request req, Response res) throws DataAccessException {
      var user = new Gson().fromJson(req.body(), UserData.class);
      AuthData authToken = this.userService.login(user);
      this.authService.createAuth(authToken);
      res.status(200);
      return new Gson().toJson(authToken);
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
