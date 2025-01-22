package chess;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Hashtable;

public class KingsMoveCalculator implements ChessMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final String ajSquares[] = {"UM","UR","R","BR","BM","BL","L","UL"};
    private Hashtable<String,ChessMove> legalMoves = new Hashtable<>();
    //private Hashtable<String,ChessMove> captureMoves = new Hashtable<>();
    public KingsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;

    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        for (String aj : ajSquares){
            if (aj.equals("UL") && position.getColumn() > 0 && position.getRow() < 7) {
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }

                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("UM") && position.getRow() < 7){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn();
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("UR") && position.getColumn() < 7 && position.getRow() < 7){
                int tempR = position.getRow()+1;
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("R") && position.getColumn() < 7){
                int tempR = position.getRow();
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("BR") && position.getColumn() < 7 && position.getRow() > 0){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()+1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("BM") && position.getRow() > 0){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn();
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null){
                    legalMoves.put(aj, new ChessMove(position, endPos,null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("BL") && position.getColumn() > 0 && position.getRow() > 0){
                int tempR = position.getRow()-1;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
            else if (aj.equals("L") && position.getColumn() > 0) {
                int tempR = position.getRow();
                int tempC = position.getColumn() - 1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(aj, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(aj,new ChessMove(position,endPos,null));
                }
                //System.out.println(tempR + " " + tempC + " " + aj);
            }
        }
        return legalMoves.values();
    }

    @Override
    public int hashCode() {
        return 31 * position.getRow() * position.getColumn() + legalMoves.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KingsMoveCalculator checker = (KingsMoveCalculator) obj;
        return position.equals(checker.position) && board.equals(checker.board);
    }
}
