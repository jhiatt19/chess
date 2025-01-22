package chess;

import java.util.*;
import java.util.ArrayList;
import java.util.Collection;

public class KnightsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    public KnightsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        return new ArrayList<>();
    }
}
