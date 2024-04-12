package ui;

import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
  private final ChessClient client;

  public Repl(String serverURL) {
    client = new ChessClient(serverURL, this);
  }


  public void run() {
    System.out.println("Welcome to 240 Chess. Type Help for a list of options.");

    Scanner scanner = new Scanner(System.in);
    var result = "";
    while (!result.equals("quit")) {
      printPrompt();
      String line = scanner.nextLine();

      try {
        result = client.eval(line);
        System.out.print(SET_TEXT_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }

  public void receiveServerMessage(String message) {
    System.out.println(message);
    printPrompt();
  }
  public void printPrompt() {
    System.out.print(SET_TEXT_COLOR_GREEN + "\n" + ">>> ");
  }
}
