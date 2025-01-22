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
            if (aj == "UL" && position.getColumn() > 0 && position.getRow() < 7) {
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                //if (ChessBoard.getPiece(endPos))
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "UM" && position.getRow() < 7){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn();
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "UR" && position.getColumn() < 7 && position.getRow() < 7){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "R" && position.getColumn() < 7){
                int tempR = position.getRow();
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BR" && position.getColumn() < 7 && position.getRow() > 0){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BM" && position.getRow() > 0){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn();
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "BL" && position.getColumn() > 0 && position.getRow() > 0){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                legalMoves.put(aj, new ChessMove(position, endPos,null));
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj == "L" && position.getColumn() > 0) {
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
        return 31 * board.hashCode() * position.getRow() * position.getColumn();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;;
        KingsMoveCalculator checker = (KingsMoveCalculator) obj;
        return position == checker.position && board == checker.board;
    }
}
