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
        diagonalUpRight(position);
        diagonalDownRight(position);
        diagonalDownLeft(position);
        diagonalUpLeft(position);
        return legalMoves;
    }

    public ChessPosition diagonalUpRight(ChessPosition startPos){
        if (startPos.getRow() >= 8 || startPos.getColumn() >= 8){
            return startPos;
        }
        int tempR = startPos.getRow() + 1;
        int tempC = startPos.getColumn() + 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            return diagonalUpRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
            }
        }
        return newPos;
    }
    public ChessPosition diagonalUpLeft(ChessPosition startPos){
        if (startPos.getRow() >= 8 || startPos.getColumn() <= 1){
            return startPos;
        }
        int tempR = startPos.getRow() + 1;
        int tempC = startPos.getColumn() - 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            return diagonalUpLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
            }
        }
        return newPos;
    }

    public ChessPosition diagonalDownRight(ChessPosition startPos){
        if (startPos.getRow() <= 1 || startPos.getColumn() >= 8){
            return startPos;
        }
        int tempR = startPos.getRow() - 1;
        int tempC = startPos.getColumn() + 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            return diagonalDownRight(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
            }
        }
        return newPos;
    }

    public ChessPosition diagonalDownLeft(ChessPosition startPos){
        if (startPos.getRow() <= 1 || startPos.getColumn() <= 1){
            return startPos;
        }
        int tempR = startPos.getRow() - 1;
        int tempC = startPos.getColumn() - 1;
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            return diagonalDownLeft(newPos);
        }
        else if (board.getPiece(newPos) != null){
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
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
