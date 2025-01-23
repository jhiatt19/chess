package chess;

import java.util.*;
import java.util.Collection;

public class KnightsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final String forkMoves[] = {"fkul","fkur","fkr","fklr","fkdr","fkdl","fkll","fkl"};
    private Hashtable<String,ChessMove> legalMoves = new Hashtable<>();
    public KnightsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        for (String fk : forkMoves){
            if (fk.equals("fkul") && position.getColumn() > 1 && position.getRow() <= 6){
                int tempR = position.getRow()+2;
                int tempC = position.getColumn()-1;
                ChessPosition endPos = new ChessPosition(tempR,tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                    legalMoves.put(fk,new ChessMove(position,endPos,null));
                }
            }
            else if (fk.equals("fkur") && position.getRow() <= 6 && position.getColumn() <= 7) {
                int tempR = position.getRow() + 2;
                int tempC = position.getColumn() + 1;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            else if (fk.equals("fkr") && position.getRow() <= 7 && position.getColumn() <= 6) {
                int tempR = position.getRow() + 1;
                int tempC = position.getColumn() + 2;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            else if (fk.equals("fklr") && position.getColumn() <= 6 && position.getRow() > 1) {
                int tempR = position.getRow() - 1;
                int tempC = position.getColumn() + 2;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            else if (fk.equals("fkdr") && position.getColumn() <= 7 && position.getRow() >= 3) {
                int tempR = position.getRow() - 2;
                int tempC = position.getColumn() + 1;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            else if (fk.equals("fkdl") && position.getColumn() > 1 && position.getRow() >= 3) {
                int tempR = position.getRow() - 2;
                int tempC = position.getColumn() - 1 ;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            else if (fk.equals("fkll") && position.getColumn() >= 3 && position.getRow() > 1) {
                int tempR = position.getRow() - 1;
                int tempC = position.getColumn() - 2 ;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            else if (fk.equals("fkl") && position.getColumn() >= 3 && position.getRow() <= 7) {
                int tempR = position.getRow() + 1;
                int tempC = position.getColumn() - 2 ;
                ChessPosition endPos = new ChessPosition(tempR, tempC);
                if (board.getPiece(endPos) == null) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
                else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    legalMoves.put(fk, new ChessMove(position, endPos, null));
                }
            }
            //System.out.println(legalMoves.keySet());
        }
        return legalMoves.values();
    }

    @Override
    public int hashCode() {
        return (17 * position.hashCode() * legalMoves.keySet().hashCode() ^ board.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        KnightsMoveCalculator checker = (KnightsMoveCalculator) obj;
        return position.equals(checker.position) && board.equals(checker.board);
    }
}
