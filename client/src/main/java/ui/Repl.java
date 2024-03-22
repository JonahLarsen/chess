package ui;

public class Repl {
  private final ChessClient client;

  public Repl(String serverURL) {
    client = new ChessClient(serverURL);
  }


  public void run() {
    System.out.println("Welcome to 240 Chess. Type Help for a list of options.");
  }
}
