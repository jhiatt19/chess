package chess;

import java.util.*;
import java.util.Hashtable;

public class KingsMoveCalculator implements ChessMovesCalculator {
    private ChessBoard board;
    private ChessPosition position;
    private ChessGame.TeamColor color;
    private String ajsquares[] = {"UL","UM","UR","R","BR","BM","BL","L"};
    private Hashtable<String,ChessPosition> tempMoves = new Hashtable<>(8);
    private Hashtable<String,ChessMove> legalMoves = new Hashtable<>();
    public KingsMoveCalculator(ChessBoard board, ChessPosition position,ChessGame.TeamColor color) {
        this.board = board;
        this.position = position;
        this.color = color;
            //System.out.println("Key: " + aj + " Value: " + tempMoves.get(aj));

    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        for (String aj : ajsquares) {
            tempMoves.put(aj, position);
        }
        for (String aj : tempMoves.keySet()){
            if (aj == "UL") {
                ChessMove temp = [position.getRow()][position.getColumn()];
                legalMoves.put(aj,temp);
            }
        }
            return legalMoves.values();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return false;
    }


}
