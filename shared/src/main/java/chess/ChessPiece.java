package chess;

import java.util.ArrayList;
import java.util.Collection;

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
        KING(1),
        QUEEN(2),
        BISHOP(3),
        KNIGHT(4),
        ROOK(5),
        PAWN(6);

        private final int value;
        PieceType(int value){
            this.value = value;
        }

        public int getPieceTypeVal(){
            return value;
        }
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
        if (type == PieceType.KING){
            return new KingsMoveCalculator(board,myPosition).pieceMoves(board,myPosition);
        }
        else if (type == PieceType.KNIGHT){
            return new KnightsMoveCalculator(board,myPosition).pieceMoves(board,myPosition);        }
        return new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return 31 * pieceColor.getTeamColor() * type.getPieceTypeVal();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ChessPiece checker = (ChessPiece) obj;
        return pieceColor == checker.pieceColor && type == checker.type;
    }
}
