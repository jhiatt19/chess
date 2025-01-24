package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class BishopsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public BishopsMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            diagonalUpRight(position);
            diagonalDownRight(position);
            diagonalDownLeft(position);
            diagonalUpLeft(position);
        }
        else {
            diagonalDownRight(position);
            diagonalDownLeft(position);
            diagonalUpLeft(position);
            diagonalUpRight(position);
        }
        return legalMoves;
    }

    public ChessPosition diagonalUpRight(ChessPosition startPos){
        if (startPos.getRow() >= 8 || startPos.getColumn() >= 8){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow() + 1;
        int tempC = startPos.getColumn() + 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(startPos,newPos,null));
            System.out.println("DUR");
            return diagonalUpRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (board.getPiece(newPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                legalMoves.add(new ChessMove(startPos, newPos, null));
                System.out.println("DURx");
            }
        }
        return newPos;
    }
    public ChessPosition diagonalUpLeft(ChessPosition startPos){
        if (startPos.getRow() >= 8 || startPos.getColumn() <= 1){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow() + 1;
        int tempC = startPos.getColumn() - 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(startPos,newPos,null));
            System.out.println("DUL");
            return diagonalUpLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (board.getPiece(newPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                legalMoves.add(new ChessMove(startPos, newPos, null));
                System.out.println("DULx");
            }
        }
        return newPos;
    }

    public ChessPosition diagonalDownRight(ChessPosition startPos){
        if (startPos.getRow() <= 1 || startPos.getColumn() >= 8){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow() - 1;
        int tempC = startPos.getColumn() + 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(startPos,newPos,null));
            System.out.println("DDR");
            return diagonalDownRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (board.getPiece(newPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                legalMoves.add(new ChessMove(startPos, newPos, null));
                System.out.println("DDRx");
            }
        }
        return newPos;
    }

    public ChessPosition diagonalDownLeft(ChessPosition startPos){
        if (startPos.getRow() <= 1 || startPos.getColumn() <= 1){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow() - 1;
        int tempC = startPos.getColumn() - 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(startPos,newPos,null));
            System.out.println("DDL");
            return diagonalDownLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (board.getPiece(newPos).getTeamColor() != board.getPiece(position).getTeamColor()){
                legalMoves.add(new ChessMove(startPos, newPos, null));
                System.out.println("DDLx");
            }
        }
        return newPos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BishopsMoveCalculator that = (BishopsMoveCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.equals(legalMoves, that.legalMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, legalMoves);
    }
}
