package server;

import spark.*;

public class Server {

    public static void main(String[] args) {
        Server server = new Server();
        int currentPort = server.run(8080);

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object clearHandler(Request req, Response res) throws Exception {

    }
    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
