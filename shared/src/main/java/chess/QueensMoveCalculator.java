package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class QueensMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public QueensMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        diagonalUpRight(position);
        diagonalDownRight(position);
        diagonalDownLeft(position);
        diagonalUpLeft(position);
        verticalUp(position);
        verticalDown(position);
        horizontalLeft(position);
        horizontalRight(position);

        return legalMoves;
    }

    public ChessPosition verticalUp(ChessPosition startPos){
        if (startPos.getRow() >= 8){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow() + 1;
        int tempC = startPos.getColumn();
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return  verticalUp(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }

    public ChessPosition verticalDown(ChessPosition startPos){
        if (startPos.getRow() <= 1){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow() - 1;
        int tempC = startPos.getColumn();
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return verticalDown(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }

    public ChessPosition horizontalRight(ChessPosition startPos){
        if (startPos.getColumn() >= 8){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow();
        int tempC = startPos.getColumn() + 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return horizontalRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }

    public ChessPosition horizontalLeft(ChessPosition startPos){
        if (startPos.getColumn() <= 1){
            System.out.println("Hit Edge: " + startPos.getRow() + " " + startPos.getColumn());
            return startPos;
        }
        int tempR = startPos.getRow();
        int tempC = startPos.getColumn() - 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            System.out.println(newPos.getRow() + " " + newPos.getColumn());
            return horizontalLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
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
        QueensMoveCalculator that = (QueensMoveCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.equals(legalMoves, that.legalMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, legalMoves);
    }
}
