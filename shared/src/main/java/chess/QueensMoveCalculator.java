package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueensMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public QueensMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
//        diagonalUpRight(position);
//        diagonalDownRight(position);
//        diagonalDownLeft(position);
//        diagonalUpLeft(position);
        return new ArrayList<>();
    }
}
