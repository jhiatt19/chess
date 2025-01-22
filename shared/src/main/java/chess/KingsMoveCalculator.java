package chess;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Hashtable;

public class KingsMoveCalculator implements ChessMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final String ajSquares[] = {"UM","UR","R","BR","BM","BL","L","UL"};
    private Hashtable<String,ChessMove> legalMoves = new Hashtable<>();
    public KingsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;

    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        for (String aj : ajSquares){
            if (aj == "UL") {
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                //if (ChessBoard.getPiece(endPos))
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "UM"){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn();
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "UR"){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "R"){
                int tempR = position.getRow();
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BR"){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BM"){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn();
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BL"){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "L") {
                int tempR = position.getRow();
                int tempC = position.getColumn() - 1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
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
