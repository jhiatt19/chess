package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    public KnightsMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPos){
        one(startPos);
        two(startPos);
        three(startPos);
        four(startPos);
        five(startPos);
        six(startPos);
        seven(startPos);
        eight(startPos);
        return legalMoves;
    }

    public void one(ChessPosition startPos){
        if (startPos.getRow() > 1 && startPos.getColumn() < 7) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() + 2;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void two(ChessPosition startPos){
        if (startPos.getRow() > 1 && startPos.getColumn() > 2) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() - 2;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void three(ChessPosition startPos){
        if (startPos.getRow() > 2 && startPos.getColumn() < 8) {
            int tempR = startPos.getRow() - 2;
            int tempC = startPos.getColumn() + 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void four(ChessPosition startPos){
        if (startPos.getRow() > 2 && startPos.getColumn() > 1) {
            int tempR = startPos.getRow() - 2;
            int tempC = startPos.getColumn() - 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void five(ChessPosition startPos){
        if (startPos.getRow() < 8 && startPos.getColumn() < 7) {
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() + 2;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void six(ChessPosition startPos){
        if (startPos.getRow() < 8 && startPos.getColumn() > 2) {
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() - 2;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void seven(ChessPosition startPos){
        if (startPos.getRow() < 7 && startPos.getColumn() < 8) {
            int tempR = startPos.getRow() + 2;
            int tempC = startPos.getColumn() + 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
    public void eight(ChessPosition startPos){
        if (startPos.getRow() < 7 && startPos.getColumn() > 1) {
            int tempR = startPos.getRow() + 2;
            int tempC = startPos.getColumn() - 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
            else if (!board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, tempPos, null));
            }
        }
    }
}
