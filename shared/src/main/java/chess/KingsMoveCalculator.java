package chess;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Hashtable;

public class KingsMoveCalculator implements ChessMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final String ajSquares[] = {"UM","UR","R","BR","BM","BL","L","UL"};
    private Hashtable<String,ChessPosition> tempMoves = new Hashtable<>(8);
    private Hashtable<String,ChessMove> legalMoves = new Hashtable<>();
    public KingsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;

    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        for (String aj : ajSquares) {
            tempMoves.put(aj, position);
        }
        for (String aj : tempMoves.keySet()){
            if (aj == "UL") {
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()-1;
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "UM"){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn();
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "UR"){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()+1;
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "R"){
                int tempR = position.getRow();
                int tempC = position.getColumn()+1;
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BR"){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()+1;
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BM"){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn();
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BL"){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()-1;
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "L") {
                int tempR = position.getRow();
                int tempC = position.getColumn() - 1;
                legalMoves.put(aj, new ChessMove(position, new ChessPosition(tempR,tempC),null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
        }
        return legalMoves.values();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
