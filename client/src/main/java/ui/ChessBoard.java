package ui;


import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_BLACK;

public class ChessBoard {
  private static final int BOARD_SQUARES_LENGTH = 8;
  private static final int SQUARE_CHAR_LENGTH = 1;
  private static final int BORDER_LENGTH = 10;
  private static final String EMPTY = "   ";

  public static void main(String[] args) {
    chess.ChessBoard board = new chess.ChessBoard();
    drawChessBoard(board, "WHITE");
  }

  public static void drawChessBoard(chess.ChessBoard board, String color) {
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    String[] header;

    if (color.equals("WHITE")) {
      header = new String[] {EMPTY, " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", EMPTY};
      String[] column = {"1", "2", "3", "4", "5", "6", "7", "8"};
    } else {
      header = new String[] {EMPTY, "h", "g", "f", "e", "d", "c", "b", "a", EMPTY};
      String[] column = {"8", "7", "6", "5", "4", "3", "2", "1"};
    }

    drawTopHeader(out, header);
  }

  private static void drawTopHeader(PrintStream out, String[] header) {
    setBorderColors(out
    );
    for (int i = 0; i < BORDER_LENGTH; i++) {
      drawSpace(out, header[i]);
    }
  }

  private static void drawSpace(PrintStream out, String text) {
    out.print(text);
  }

  private static void setBorderColors(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_YELLOW);
  }

}
