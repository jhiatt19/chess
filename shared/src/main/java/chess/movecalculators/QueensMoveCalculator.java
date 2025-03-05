package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class QueensMoveCalculator implements ChessMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    private final RecursiveCalls recursiveCalls;

    public QueensMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.recursiveCalls = new RecursiveCalls(board,position);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        queenDiagonalUpRight(position);
        queenDiagonalDownRight(position);
        queenDiagonalDownLeft(position);
        queenDiagonalUpLeft(position);
        queenVerticalUp(position);
        queenVerticalDown(position);
        queenHorizontalLeft(position);
        queenHorizontalRight(position);

        return legalMoves;
    }

    public ChessPosition queenVerticalUp(ChessPosition startPos) {
        if (startPos != null && startPos.getRow() < 8) {
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenVerticalUp(newPos);
        }
        return null;
    }

    public ChessPosition queenVerticalDown(ChessPosition startPos) {
        if (startPos != null && startPos.getRow() > 1) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenVerticalDown(newPos);
        }
        return null;
    }

    public ChessPosition queenHorizontalRight(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() < 8){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() + 1;
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenHorizontalRight(newPos);
        }
        return null;
    }

    public ChessPosition queenHorizontalLeft(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() > 1){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() - 1;
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenHorizontalLeft(newPos);
        }
        return null;

    }

    public ChessPosition queenDiagonalUpRight(ChessPosition startPos){
        if (startPos != null && (startPos.getRow() < 8 && startPos.getColumn() < 8)){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() + 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenDiagonalUpRight(startPos);
        }
        return null;
    }
    public ChessPosition queenDiagonalUpLeft(ChessPosition startPos){
        if (startPos != null && startPos.getRow() < 8 && startPos.getColumn() > 1){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() - 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenDiagonalUpLeft(startPos);
        }
        return null;
    }

    public ChessPosition queenDiagonalDownRight(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1 && startPos.getColumn() < 8){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() + 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenDiagonalDownRight(startPos);
        }
        return null;
    }

    public ChessPosition queenDiagonalDownLeft(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1 && startPos.getColumn() > 1){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() - 1;
            startPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return queenDiagonalDownLeft(startPos);
        }
        return null;
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
