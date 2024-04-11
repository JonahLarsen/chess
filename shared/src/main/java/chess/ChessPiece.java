package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
      if (this.type == PieceType.KING) {
        return kingPieceMoves(board, myPosition);
      } else if (this.type == PieceType.BISHOP) {
        return bishopPieceMoves(board, myPosition);
      } else if (this.type == PieceType.ROOK) {
        return rookPieceMoves(board, myPosition);
      } else if (this.type == PieceType.QUEEN) {
        return queenPieceMoves(board, myPosition);
      } else if (this.type == PieceType.KNIGHT) {
        return knightPieceMoves(board, myPosition);
      } else if (this.type == PieceType.PAWN) {
        return pawnPieceMoves(board, myPosition);
      }

      return null;
    }

    public Collection<ChessMove> kingPieceMoves(ChessBoard board, ChessPosition myPosition) {
      ArrayList<ChessMove> validMoves = new ArrayList<>();
      //Top
      ChessPosition abovePosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() );
      if (abovePosition.getRow() <= 8 && (board.getPiece(abovePosition) == null || board.getPiece(abovePosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, abovePosition, null));
      }
      //Bottom
      ChessPosition belowPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() );
      if (belowPosition.getRow() >= 1 && (board.getPiece(belowPosition) == null || board.getPiece(belowPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, belowPosition, null));
      }
      //Right
      ChessPosition rightPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1 );
      if (rightPosition.getColumn() <= 8 && (board.getPiece(rightPosition) == null || board.getPiece(rightPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, rightPosition, null));
      }
      //Left
      ChessPosition leftPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1 );
      if (leftPosition.getRow() >= 1 && (board.getPiece(leftPosition) == null || board.getPiece(leftPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, leftPosition, null));
      }
      //TopLeft
      ChessPosition topLeftPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
      if (topLeftPosition.getRow() <= 8 && topLeftPosition.getColumn() >= 1 && (board.getPiece(topLeftPosition) == null || board.getPiece(topLeftPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, topLeftPosition, null));
      }
      //TopRight
      ChessPosition topRightPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1 );
      if (topRightPosition.getRow() <= 8 && topRightPosition.getColumn() <= 8 && (board.getPiece(topRightPosition) == null || board.getPiece(topRightPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, topRightPosition, null));
      }
      //BottomLeft
      ChessPosition bottomLeftPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1 );
      if (bottomLeftPosition.getRow() >= 1 && bottomLeftPosition.getColumn() >= 1 && (board.getPiece(bottomLeftPosition) == null || board.getPiece(bottomLeftPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, bottomLeftPosition, null));
      }
      //BottomRight
      ChessPosition bottomRightPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1 );
      if (bottomRightPosition.getRow() >= 1 && bottomRightPosition.getColumn() <= 8 && (board.getPiece(bottomRightPosition) == null || board.getPiece(bottomRightPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, bottomRightPosition, null));
      }

      return validMoves;
    }

    public Collection<ChessMove> bishopPieceMoves(ChessBoard board, ChessPosition myPosition) {
      ArrayList<ChessMove> validMoves = new ArrayList<>();
      //Top Right
      ChessPosition tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
      while(tempPosition.getRow() <= 8 && tempPosition.getColumn() <= 8) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition,tempPosition, null));
          if(board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow() + 1, tempPosition.getColumn() + 1);
        } else {
          break;
        }
      }
      //Top Left
      tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
      while(tempPosition.getRow() <= 8 && tempPosition.getColumn() >= 1) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition,tempPosition, null));
          if(board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow() + 1, tempPosition.getColumn() - 1);
        } else {
          break;
        }
      }
      //Bottom Right
      tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
      while(tempPosition.getRow() >= 1 && tempPosition.getColumn() <= 8) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition,tempPosition, null));
          if(board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow() - 1, tempPosition.getColumn() + 1);
        } else {
          break;
        }
      }
      //Bottom Left
      tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
      while(tempPosition.getRow() >= 1 && tempPosition.getColumn() >= 1) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition,tempPosition, null));
          if(board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow() - 1, tempPosition.getColumn() - 1);
        } else {
          break;
        }
      }

      return validMoves;
    }

    public Collection<ChessMove> rookPieceMoves(ChessBoard board, ChessPosition myPosition) {
      ArrayList<ChessMove> validMoves = new ArrayList<>();
      //Up
      ChessPosition tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
      while (tempPosition.getRow() <= 8) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition, tempPosition, null));
          if (board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow() + 1, tempPosition.getColumn());
        } else {break;}
      }
      //Down
      tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
      while (tempPosition.getRow() >= 1) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition, tempPosition, null));
          if (board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow() - 1, tempPosition.getColumn());
        } else {break;}
      }
      //Left
      tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
      while (tempPosition.getColumn() >= 1) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition, tempPosition, null));
          if (board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn() - 1);
        } else {break;}
      }
      //Right
      tempPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
      while (tempPosition.getColumn() <= 8) {
        if (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
          validMoves.add(new ChessMove(myPosition, tempPosition, null));
          if (board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() != this.getTeamColor()) {
            break;
          }
          tempPosition = new ChessPosition(tempPosition.getRow(), tempPosition.getColumn() + 1);
        } else {break;}
      }
      return validMoves;
    }

    public Collection<ChessMove> queenPieceMoves(ChessBoard board, ChessPosition myPosition) {
      ArrayList<ChessMove> validMoves = new ArrayList<>();
      validMoves.addAll(bishopPieceMoves(board, myPosition));
      validMoves.addAll(rookPieceMoves(board, myPosition));
      return validMoves;
    }

    public Collection<ChessMove> knightPieceMoves(ChessBoard board, ChessPosition myPosition) {
      ArrayList<ChessMove> validMoves = new ArrayList<>();
      ChessPosition tempPosition;
      //Above Left
      tempPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1);
      if (tempPosition.getRow() <= 8 && tempPosition.getColumn() >= 1 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Above Right
      tempPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1);
      if (tempPosition.getRow() <= 8 && tempPosition.getColumn() <= 8 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Left Above
      tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2);
      if (tempPosition.getRow() <= 8 && tempPosition.getColumn() >= 1 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Left Below
      tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2);
      if (tempPosition.getRow() >= 1 && tempPosition.getColumn() >= 1 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Below Left
      tempPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1);
      if (tempPosition.getRow() >= 1 && tempPosition.getColumn() >= 1 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Below Right
      tempPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1);
      if (tempPosition.getRow() >= 1 && tempPosition.getColumn() <= 8 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Right Above
      tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2);
      if (tempPosition.getRow() <= 8 && tempPosition.getColumn() <= 8 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }
      //Right Below
      tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2);
      if (tempPosition.getRow() >= 1 && tempPosition.getColumn() <= 8 && (board.getPiece(tempPosition) == null || board.getPiece(tempPosition).getTeamColor() != this.getTeamColor())) {
        validMoves.add(new ChessMove(myPosition, tempPosition, null));
      }

      return validMoves;
    }

    public Collection<ChessMove> pawnPieceMoves(ChessBoard board, ChessPosition myPosition) {
      ArrayList<ChessMove> validMoves = new ArrayList<>();
      boolean hasNotMoved = (this.getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) || (this.getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7);

      //White Pawn

      if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
        ChessPosition tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        //Straight Ahead
        if (board.getPiece(tempPosition) == null && tempPosition.getRow() != 8) {
          validMoves.add(new ChessMove(myPosition, tempPosition, null));
        } else if (board.getPiece(tempPosition) == null && tempPosition.getRow() == 8) {
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.QUEEN));
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.BISHOP));
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.ROOK));
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.KNIGHT));
        }
        //Double Move
        if (hasNotMoved) {
          ChessPosition tempPosition2 = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
          if (board.getPiece(tempPosition) == null && board.getPiece(tempPosition2) == null) {
            validMoves.add(new ChessMove(myPosition, tempPosition2, null));
          }
        }

        //Capture Piece Right
        tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        if (tempPosition.getRow() <= 8 && tempPosition.getColumn() <= 8 && board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
          setBeginningRow(myPosition, validMoves, tempPosition);
        }
        //Capture Piece Left
        tempPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (tempPosition.getRow() <= 8 && tempPosition.getColumn() >= 1 && board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
          setBeginningRow(myPosition, validMoves, tempPosition);
        }
      } else {
      //Black Pawn
        ChessPosition tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        //Straight Ahead
        if (board.getPiece(tempPosition) == null && tempPosition.getRow() != 1) {
          validMoves.add(new ChessMove(myPosition, tempPosition, null));
        } else if (board.getPiece(tempPosition) == null && tempPosition.getRow() == 1) {
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.QUEEN));
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.BISHOP));
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.ROOK));
          validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.KNIGHT));
        }
        //Double Move
        if (hasNotMoved) {
          ChessPosition tempPosition2 = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
          if (board.getPiece(tempPosition) == null && board.getPiece(tempPosition2) == null) {
            validMoves.add(new ChessMove(myPosition, tempPosition2, null));
          }
        }

        //Capture Piece Right
        tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (tempPosition.getRow() >= 1 && tempPosition.getColumn() <= 8 && board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
          if (tempPosition.getRow() == 1) {
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.QUEEN));
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.BISHOP));
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.ROOK));
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.KNIGHT));
          } else {
            validMoves.add(new ChessMove(myPosition, tempPosition, null));
          }
        }
        //Capture Piece Left
        tempPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        if (tempPosition.getRow() >= 1 && tempPosition.getColumn() >= 1 && board.getPiece(tempPosition) != null && board.getPiece(tempPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
          if (tempPosition.getRow() == 1) {
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.QUEEN));
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.BISHOP));
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.ROOK));
            validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.KNIGHT));
          } else {
            validMoves.add(new ChessMove(myPosition, tempPosition, null));
          }
        }


      }


      return validMoves;
    }

  private void setBeginningRow(ChessPosition myPosition, ArrayList<ChessMove> validMoves, ChessPosition tempPosition) {
    if (tempPosition.getRow() == 8) {
      validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.QUEEN));
      validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.BISHOP));
      validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.ROOK));
      validMoves.add(new ChessMove(myPosition, tempPosition, PieceType.KNIGHT));
    } else {
      validMoves.add(new ChessMove(myPosition, tempPosition, null));
    }
  }


  @Override
  public String toString() {
    return "ChessPiece{" +
            "pieceColor=" + pieceColor +
            ", type=" + type +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ChessPiece that=(ChessPiece) o;
    return pieceColor == that.pieceColor && type == that.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(pieceColor, type);
  }
}
