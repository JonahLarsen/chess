package ui;

import server.ServerFacade;

public class ChessClient {
  private final ServerFacade server;
  private final String serverURL;


  public ChessClient(String URL) {
    server = new ServerFacade(URL);
    serverURL = URL;
  }
}
