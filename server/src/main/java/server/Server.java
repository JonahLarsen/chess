package server;

import dataAccess.DataAccessException;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import javax.xml.crypto.Data;

public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;

    public Server() {
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();
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
        Spark.exception(DataAccessException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(DataAccessException e, Request req, Response res) {
        //TODO
    }
    private Object clearHandler(Request req, Response res) throws Exception {
        authService.deleteAllAuth();
        userService.deleteAllUsers();
        gameService.deleteAllGames();

        res.status(200);
        return "";
    }

    private Object registerHandler(Request req, Response res) throws Exception {
        return null;
    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
