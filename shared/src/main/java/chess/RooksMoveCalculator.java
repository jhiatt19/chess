package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RooksMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public RooksMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        VerticalUp(position);
        VerticalDown(position);
        HorizontalLeft(position);
        HorizontalRight(position);

        return legalMoves;
    }

    public ChessPosition VerticalUp(ChessPosition startPos){
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
            return  VerticalUp(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }

    public ChessPosition VerticalDown(ChessPosition startPos){
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
            return VerticalDown(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }

    public ChessPosition HorizontalRight(ChessPosition startPos){
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
            return HorizontalRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }

    public ChessPosition HorizontalLeft(ChessPosition startPos){
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
            return HorizontalLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                System.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
        }
        return newPos;
    }
}
