package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.board.getPiece(startPosition);
        if (piece == null) {
            return null;
        } else {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(this.board, startPosition);
        }
        return null; //Change
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition ourKingLocation = getKingPieceLocation(teamColor);
        ChessPiece tempPiece;
        Collection<ChessMove> tempPieceMoves;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                tempPiece = this.board.getPiece(new ChessPosition(i, j));
                if (tempPiece != null && tempPiece.getTeamColor() != teamColor) {
                    tempPieceMoves = tempPiece.pieceMoves(this.board, new ChessPosition(i, j));
                    for (ChessMove move : tempPieceMoves) {
                        if (move.getEndPosition().equals(ourKingLocation)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition getKingPieceLocation(TeamColor teamColor) {
        ChessPiece tempPiece;
        ChessPosition tempPosition;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                tempPosition = new ChessPosition(i, j);
                if (this.board.getPiece(tempPosition) != null) {
                    tempPiece = new ChessPiece(this.board.getPiece(tempPosition).getTeamColor(), this.board.getPiece(tempPosition).getPieceType());
                    if (tempPiece.getTeamColor() == teamColor && tempPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        return tempPosition;

                    }
                }

            }
        }
        return null;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
