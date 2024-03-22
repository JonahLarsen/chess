package ui;

import java.util.Scanner;

import static ui.EscapeSequences.SET_BG_COLOR_BLUE;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class Repl {
  private final ChessClient client;

  public Repl(String serverURL) {
    client = new ChessClient(serverURL);
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
        System.out.print(SET_BG_COLOR_BLUE + result);
      } catch (Throwable e) {
        var msg = e.toString();
        System.out.print(msg);
      }
    }
    System.out.println();
  }


  private void printPrompt() {
    System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN);
  }
}
