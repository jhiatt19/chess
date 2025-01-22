package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class PawnsMoveCalculator {
    private ChessBoard board;
    private final ChessPosition position;
    private final String pawnMoves[] = {"fkul","fkur","fkr","fklr","fkdr","fkdl","fkll","fkl"};
    private Hashtable<String,ChessMove> legalMoves = new Hashtable<>();
    public PawnsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        return new ArrayList<>();
    }
}
