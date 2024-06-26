package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.setBoard(new ChessBoard());
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK,
        NIL
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
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        ChessBoard tempBoard;
        if (piece != null) {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(this.board, startPosition);
            for (ChessMove move : possibleMoves) {
                tempBoard = makeBoardCopy(this.board);
                tempBoard.addPiece(move.getEndPosition(), tempBoard.getPiece(startPosition));
                tempBoard.addPiece(startPosition, null);
                if (!isInCheckTempBoard(piece.getTeamColor(), tempBoard)) {
                    validMoves.add(move);
                }
            }
            return validMoves;
        }
        return null;
    }

    public ChessBoard makeBoardCopy(ChessBoard board) {
        ChessBoard copyBoard = new ChessBoard();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if (board.getPiece(new ChessPosition(i, j)) != null) {
                    TeamColor pieceColor = board.getPiece(new ChessPosition(i, j)).getTeamColor();
                    ChessPiece.PieceType pieceType = board.getPiece(new ChessPosition(i, j)).getPieceType();
                    copyBoard.addPiece(new ChessPosition(i, j), new ChessPiece(pieceColor, pieceType));
                }
            }
        }
        return copyBoard;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMovePositions = validMoves(move.getStartPosition());
        if (validMovePositions.contains(move) && this.board.getPiece(move.getStartPosition()).getTeamColor() == this.turn) {
            if (move.getPromotionPiece() == null) {
                this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition()));
            } else {
                switch(move.getPromotionPiece()) {
                    case QUEEN:
                        this.board.addPiece(move.getEndPosition(), new ChessPiece(this.turn, ChessPiece.PieceType.QUEEN));
                        break;
                    case ROOK:
                        this.board.addPiece(move.getEndPosition(), new ChessPiece(this.turn, ChessPiece.PieceType.ROOK));
                        break;
                    case BISHOP:
                        this.board.addPiece(move.getEndPosition(), new ChessPiece(this.turn, ChessPiece.PieceType.BISHOP));
                        break;
                    case KNIGHT:
                        this.board.addPiece(move.getEndPosition(), new ChessPiece(this.turn, ChessPiece.PieceType.KNIGHT));
                        break;
                }
            }
            this.board.addPiece(move.getStartPosition(), null);
        } else {
            throw new InvalidMoveException();
        }
        if (this.turn == TeamColor.BLACK) {
            this.turn = TeamColor.WHITE;
        } else {
            this.turn = TeamColor.BLACK;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckTempBoard(teamColor, this.board);
    }

    public boolean isInCheckTempBoard(TeamColor teamColor, ChessBoard board) {
        ChessPosition ourKingLocation = getKingPieceLocation(teamColor, board);

        //For tests where there is no king on the board
        if (ourKingLocation == null) {
            return false;
        }

        ChessPiece tempPiece;
        Collection<ChessMove> tempPieceMoves;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                tempPiece = board.getPiece(new ChessPosition(i, j));
                if (tempPiece != null && tempPiece.getTeamColor() != teamColor) {
                    tempPieceMoves = tempPiece.pieceMoves(board, new ChessPosition(i, j));
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

    public ChessPosition getKingPieceLocation(TeamColor teamColor, ChessBoard board) {
        ChessPiece tempPiece;
        ChessPosition tempPosition;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                tempPosition = new ChessPosition(i, j);
                if (board.getPiece(tempPosition) != null) {
                    tempPiece = new ChessPiece(board.getPiece(tempPosition).getTeamColor(), board.getPiece(tempPosition).getPieceType());
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
        boolean noValidMoves = isInStalemate(teamColor);

        if (isInCheck(teamColor) && noValidMoves) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition tempPosition;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                tempPosition = new ChessPosition(i, j);
                if (this.board.getPiece(tempPosition) != null ) {
                    TeamColor tempColor = this.board.getPiece(tempPosition).getTeamColor();
                    if (tempColor == teamColor) {
                        Collection<ChessMove> availableMoves = validMoves(tempPosition);
                        if (!availableMoves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
