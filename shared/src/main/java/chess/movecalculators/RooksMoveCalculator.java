package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RooksMoveCalculator implements ChessMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    private final RecursiveCalls recursiveCalls;

    public RooksMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
        this.recursiveCalls = new RecursiveCalls(board,position);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        rookVerticalUp(position);
        rookVerticalDown(position);
        rookHorizontalLeft(position);
        rookHorizontalRight(position);

        return legalMoves;
    }

    public ChessPosition rookVerticalUp(ChessPosition startPos){
        if (startPos != null && startPos.getRow() < 8){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);
            return rookVerticalUp(newPos);
        }
        return null;
    }

    public ChessPosition rookVerticalDown(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return rookVerticalDown(newPos);
        }
        return null;
    }

    public ChessPosition rookHorizontalRight(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() < 8){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() + 1;
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return rookHorizontalRight(newPos);
        }
        return null;
    }

    public ChessPosition rookHorizontalLeft(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() > 1){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() - 1;
            ChessPosition newPos = recursiveCalls.move(tempR, tempC, legalMoves);;
            return rookHorizontalLeft(newPos);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RooksMoveCalculator that = (RooksMoveCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.equals(legalMoves, that.legalMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, legalMoves);
    }
}
