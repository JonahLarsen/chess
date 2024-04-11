package ui;


import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_BG_COLOR_BLACK;

public class ChessBoard {
  private static final int BOARD_SQUARES_LENGTH = 10;
  private static final int SQUARE_CHAR_LENGTH = 1;

  public static void main(String[] args) {
    chess.ChessBoard board = new chess.ChessBoard();
    board.resetBoard();
    drawChessBoard(board, "BLACK");
    drawChessBoard(board, "WHITE");

  }

  public static void drawChessBoard(chess.ChessBoard board, String color) {
    System.out.println();
    var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    String[] header;
    String[] column = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};;

    if (color.equals("WHITE")) {
      header = new String[] {"   ", " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ", "   "};
    } else {
      header = new String[] {"   ", " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a ", "   "};
    }

    drawTopHeader(out, header);
    drawBoard(out, column, board, color);
    drawTopHeader(out, header);

    setTerminalColors(out);
    out.println();
  }

  private static void drawTopHeader(PrintStream out, String[] header) {
    setBorderColors(out);
    for (int i = 0; i < BOARD_SQUARES_LENGTH; i++) {
      drawSpace(out, header[i]);
    }
    setTerminalColors(out);
    out.println();
  }

  private static void drawBoard(PrintStream out, String[] column, chess.ChessBoard board, String color) {
    if (color.equals("WHITE")) {
      for (int i = BOARD_SQUARES_LENGTH - 3; i >= 0; i--) {
        drawRow(out, column, board, i);
        setTerminalColors(out);
        out.println();
      }
    } else {
      for (int i = 0; i < BOARD_SQUARES_LENGTH - 2; i++) {
        drawRow(out, column, board, i);
        setTerminalColors(out);
        out.println();
      }
    }
  }

  private static void drawRow(PrintStream out, String[] column, chess.ChessBoard board, int rowIndex) {
    setBorderColors(out);
    drawSpace(out, column[rowIndex]);
    boolean isWhiteSpace = rowIndex % 2 == 1;

    int columnIndex = 1;
    ChessPosition tempPosition = new ChessPosition(rowIndex + 1, columnIndex);
    ChessPiece tempPiece;
      for (int i = 0; i < BOARD_SQUARES_LENGTH - 2; i++) {
        if (isWhiteSpace) {
          setWhiteSquareColors(out);
          isWhiteSpace = false;
        } else {
          setBlackSquareColors(out);
          isWhiteSpace = true;
        }
        tempPiece = board.getPiece(tempPosition);
        if (tempPiece == null) {
          drawSpace(out, EMPTY);
        } else {
          if (tempPiece.getTeamColor() == WHITE) {
            setWhitePieceColor(out);
          } else {
            setBlackPieceColor(out);
          }
          switch (tempPiece.getPieceType()) {
            case PAWN:
              drawSpace(out, BLACK_PAWN);
              break;
            case ROOK:
              drawSpace(out, BLACK_ROOK);
              break;
            case KNIGHT:
              drawSpace(out, BLACK_KNIGHT);
              break;
            case BISHOP:
              drawSpace(out, BLACK_BISHOP);
              break;
            case QUEEN:
              drawSpace(out, BLACK_QUEEN);
              break;
            case KING:
              drawSpace(out, BLACK_KING);
          }
        }
        columnIndex++;
        tempPosition = new ChessPosition(rowIndex + 1, columnIndex);
      }
    setBorderColors(out);
    drawSpace(out, column[rowIndex]);
  }

  private static void drawSpace(PrintStream out, String text) {
    out.print(text);
  }

  private static void setBorderColors(PrintStream out) {
    out.print(SET_BG_COLOR_LIGHT_GREY);
    out.print(SET_TEXT_COLOR_YELLOW);
  }

  private static void setTerminalColors(PrintStream out) {
    out.print(RESET_BG_COLOR);
  }

  private static void setWhiteSquareColors(PrintStream out) {
    out.print(SET_BG_COLOR_WHITE);
  }

  private static void setBlackSquareColors(PrintStream out) {
    out.print(SET_BG_COLOR_DARK_GREEN);
  }

  private static void setWhitePieceColor(PrintStream out) {
    out.print(SET_TEXT_COLOR_MAGENTA);
  }

  private static void setBlackPieceColor(PrintStream out) {
    out.print(SET_TEXT_COLOR_BLACK);
  }

}
