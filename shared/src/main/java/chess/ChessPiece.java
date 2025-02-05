package chess;

import java.util.ArrayList;
import java.util.Collection;
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
        PAWN;

    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type.equals(PieceType.KING)){
            return new KingsMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        else if (type.equals(PieceType.KNIGHT)){
            return new KnightsMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        else if (type.equals(PieceType.PAWN)){
            return new PawnsMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        else if (type.equals(PieceType.BISHOP)){
            return new BishopsMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        else if (type.equals(PieceType.ROOK)){
            return new RooksMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        else if (type.equals(PieceType.QUEEN)){
            return new QueensMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" + pieceColor + " " + type +
                '}';
    }
}
