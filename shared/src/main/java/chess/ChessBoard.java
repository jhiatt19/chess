package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[9][9];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; ++j) {
                ChessPosition pos = new ChessPosition(i,j);
                //if (i == 2){


                    //}
                }
            }
        }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        ChessBoard checker = (ChessBoard) obj;
//        for (int i = 0; i < 9; i++){
//            for (int j = 0; j < 9; j++){
//                if (!squares[i][j].equals(checker.squares[i][j])){
//                    return false;
//                }
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        int hashcode = 0;
//        for (int i = 0; i < 9; i++){
//            for (int j = 0; i < 9; j++){
//                hashcode = squares[i][j].hashCode() + hashcode;
//            }
//        }
//        return hashcode;
    //}
}
