package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition pos = new ChessPosition(i, j);
                if (i == 1) {
                    if (j == 1 || j == 8) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                    } else if (j == 2 || j == 7) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                    } else if (j == 3 || j == 6) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                    } else if (j == 4) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                    } else {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                    }
                } else if (i == 8) {
                    if (j == 1 || j == 8) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
                    } else if (j == 2 || j == 7) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
                    } else if (j == 3 || j == 6) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
                    } else if (j == 4) {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
                    } else {
                        addPiece(pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
                    }
                } else if (i == 2) {
                    addPiece(pos, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
                } else if (i == 7) {
                    addPiece(pos, new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
                } else {
                    squares[i][j] = null;
                }
            }
        }
    }

    public ChessPosition findKing(ChessGame.TeamColor color) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; ++j) {
                if (squares[i][j].getTeamColor().equals(color)) {
                    if (squares[i][j].getPieceType().equals(ChessPiece.PieceType.KING)) {
                        return new ChessPosition(i, j);
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<ChessPosition> find(ChessGame.TeamColor color) {
        var allPieceMoves = new ArrayList<ChessPosition>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; ++j) {
                if (squares[i][j].getTeamColor().equals(color)) {
                    var type = new ChessPosition(i, j);
                    allPieceMoves.add(type);
                }

            }
        }
        return allPieceMoves;
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

}
