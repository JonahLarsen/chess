package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that=(ChessBoard) o;
        return Arrays.deepEquals(chessBoard, that.chessBoard);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chessBoard);
    }

    private ChessPiece[][] chessBoard;
    public ChessBoard() {
        this.chessBoard = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.chessBoard[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (chessBoard[position.getRow()][position.getColumn()] == null) {
            return null;
        }
        return chessBoard[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.chessBoard[i][j] = null;
            }
        }

        //First White Row

        ChessPiece whiteRookOne = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition rookPosition = new ChessPosition(1, 1);
        this.addPiece(rookPosition, whiteRookOne);

        ChessPiece knightOne = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPosition knightPosition = new ChessPosition(1, 2);
        this.addPiece(knightPosition,knightOne);


        ChessPiece bishopOne = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPosition bishopPosition = new ChessPosition(1, 3);
        this.addPiece(bishopPosition, bishopOne);

        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPosition queenPosition = new ChessPosition(1, 4);
        this.addPiece(queenPosition, whiteQueen);

        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPosition whiteKingPosition = new ChessPosition(1, 5);
        this.addPiece(whiteKingPosition, whiteKing);

        ChessPiece bishopTwo = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPosition bishopTwoPosition = new ChessPosition(1, 6);
        this.addPiece(bishopTwoPosition, bishopTwo);

        ChessPiece knightTwo = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPosition knightTwoPosition = new ChessPosition(1, 7);
        this.addPiece(knightTwoPosition, knightTwo);

        ChessPiece rookTwo = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPosition rookTwoPosition = new ChessPosition(1, 8);
        this.addPiece(rookTwoPosition, rookTwo);

        //White Pawns

        ChessPiece whitePawnOne = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnOnePosition = new ChessPosition(2, 1);
        this.addPiece(whitePawnOnePosition, whitePawnOne);

        ChessPiece whitePawnTwo = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnTwoPosition = new ChessPosition(2, 2);
        this.addPiece(whitePawnTwoPosition, whitePawnTwo);

        ChessPiece whitePawnThree = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnThreePosition = new ChessPosition(2, 3);
        this.addPiece(whitePawnThreePosition, whitePawnThree);

        ChessPiece whitePawnFour = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnFourPosition = new ChessPosition(2, 4);
        this.addPiece(whitePawnFourPosition, whitePawnFour);

        ChessPiece whitePawnFive = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnFivePosition = new ChessPosition(2, 5);
        this.addPiece(whitePawnFivePosition, whitePawnFive);

        ChessPiece whitePawnSix = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnSixPosition = new ChessPosition(2, 6);
        this.addPiece(whitePawnSixPosition, whitePawnSix);

        ChessPiece whitePawnSeven = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnSevenPosition = new ChessPosition(2, 7);
        this.addPiece(whitePawnSevenPosition, whitePawnSeven);

        ChessPiece whitePawnEight = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPosition whitePawnEightPosition = new ChessPosition(2, 8);
        this.addPiece(whitePawnEightPosition, whitePawnEight);

        //Black Pawns

        ChessPiece blackPawnOne = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnOnePosition = new ChessPosition(7, 1);
        this.addPiece(blackPawnOnePosition, blackPawnOne);

        ChessPiece blackPawnTwo = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnTwoPosition = new ChessPosition(7, 2);
        this.addPiece(blackPawnTwoPosition, blackPawnTwo);

        ChessPiece blackPawnThree = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnThreePosition = new ChessPosition(7, 3);
        this.addPiece(blackPawnThreePosition, blackPawnThree);

        ChessPiece blackPawnFour = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnFourPosition = new ChessPosition(7, 4);
        this.addPiece(blackPawnFourPosition, blackPawnFour);

        ChessPiece blackPawnFive = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnFivePosition = new ChessPosition(7, 5);
        this.addPiece(blackPawnFivePosition, blackPawnFive);

        ChessPiece blackPawnSix = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnSixPosition = new ChessPosition(7, 6);
        this.addPiece(blackPawnSixPosition, blackPawnSix);

        ChessPiece blackPawnSeven = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnSevenPosition = new ChessPosition(7, 7);
        this.addPiece(blackPawnSevenPosition, blackPawnSeven);

        ChessPiece blackPawnEight = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        ChessPosition blackPawnEightPosition = new ChessPosition(7, 8);
        this.addPiece(blackPawnEightPosition, blackPawnEight);

        //Black First Row

        ChessPiece blackRookOne = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPosition blackRookOnePosition = new ChessPosition(8, 1);
        this.addPiece(blackRookOnePosition, blackRookOne);

        ChessPiece blackKnightOne = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPosition blackKnightOnePosition = new ChessPosition(8, 2);
        this.addPiece(blackKnightOnePosition, blackKnightOne);

        ChessPiece blackBishopOne = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPosition blackBishopOnePosition = new ChessPosition(8, 3);
        this.addPiece(blackBishopOnePosition, blackBishopOne);

        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPosition blackQueenPosition = new ChessPosition(8, 4);
        this.addPiece(blackQueenPosition, blackQueen);

        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPosition blackKingPosition = new ChessPosition(8, 5);
        this.addPiece(blackKingPosition, blackKing);

        ChessPiece blackBishopTwo = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPosition blackBishopTwoPosition = new ChessPosition(8, 6);
        this.addPiece(blackBishopTwoPosition, blackBishopTwo);

        ChessPiece blackKnightTwo = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPosition blackKnightTwoPosition = new ChessPosition(8, 7);
        this.addPiece(blackKnightTwoPosition, blackKnightTwo);

        ChessPiece blackRookTwo = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPosition blackRookTwoPosition = new ChessPosition(8, 8);
        this.addPiece(blackRookTwoPosition, blackRookTwo);
    }

    @Override
    public String toString() {
        System.out.println("BEGIN");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(this.chessBoard[i][j] + "\s\s");
            }
            System.out.println("");
        }
        System.out.println("END\n\n\n");
        return "hello";
    }
}
