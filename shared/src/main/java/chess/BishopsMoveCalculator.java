package chess;

import java.util.*;

public class BishopsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public BishopsMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        //if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            diagonalUpRight(position);
            diagonalDownRight(position);
            diagonalDownLeft(position);
            diagonalUpLeft(position);
//        }
//        else {
//            diagonalDownLeft(position);
//            diagonalUpLeft(position);
//            diagonalUpRight(position);
//            diagonalDownRight(position);
//        }
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
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return diagonalUpRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
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
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return diagonalUpLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DULx " + newPos.getRow() + " " + newPos.getColumn());
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
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return diagonalDownRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DDRx " + newPos.getRow() + " " + newPos.getColumn());
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
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return diagonalDownLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DDLx " + newPos.getRow() + " " + newPos.getColumn());
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
        int hash = Objects.hash(board);
        hash = hash ^ Objects.hash(position);
        hash = hash * Objects.hash(legalMoves);
        hash = hash ^ 7;
        return hash;
    }
}
