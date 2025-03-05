package chess;

import java.util.*;

public class BishopsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    private final RecursiveCalls recursiveCalls;

    public BishopsMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.recursiveCalls = new RecursiveCalls(board,position);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        bishopDiagonalUpRight(position);
        bishopDiagonalDownRight(position);
        bishopDiagonalDownLeft(position);
        bishopDiagonalUpLeft(position);
        //System.out.println(legalMoves.size());
        return legalMoves;
    }

    public ChessPosition bishopDiagonalUpRight(ChessPosition startPos){
        if (startPos != null && (startPos.getRow() < 8 && startPos.getColumn() < 8)){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() + 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);
            return bishopDiagonalUpRight(startPos);
        }
        return null;
    }
    public ChessPosition bishopDiagonalUpLeft(ChessPosition startPos){
        if (startPos != null && startPos.getRow() < 8 && startPos.getColumn() > 1){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() - 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);
            return bishopDiagonalUpLeft(startPos);
        }
        return null;
    }

    public ChessPosition bishopDiagonalDownRight(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1 && startPos.getColumn() < 8){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() + 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);
            return bishopDiagonalDownRight(startPos);
        }
        return null;
    }

    public ChessPosition bishopDiagonalDownLeft(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1 && startPos.getColumn() > 1){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() - 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);
            return bishopDiagonalDownLeft(startPos);
        }
        return null;
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
