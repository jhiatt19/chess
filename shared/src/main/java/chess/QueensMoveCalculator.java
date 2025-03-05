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

    public ChessPosition queenMove(int tempR, int tempC) {
        ChessPosition newPos = new ChessPosition(tempR, tempC);
        if (board.getPiece(newPos) == null) {
            legalMoves.add(new ChessMove(position, newPos, null));
            return newPos;
        }
        else {
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())) {
                legalMoves.add(new ChessMove(position, newPos, null));
            }
            return null;
        }
    }

    public ChessPosition queenVerticalUp(ChessPosition startPos) {
        if (startPos != null && startPos.getRow() < 8) {
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = queenMove(tempR, tempC);
            return queenVerticalUp(newPos);
        }
        return null;
    }

    public ChessPosition queenVerticalDown(ChessPosition startPos) {
        if (startPos != null && startPos.getRow() > 1) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = queenMove(tempR, tempC);
            return queenVerticalDown(newPos);
        }
        return null;
    }

    public ChessPosition queenHorizontalRight(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() < 8){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() + 1;
            ChessPosition newPos = queenMove(tempR, tempC);
            return queenHorizontalRight(newPos);
        }
        return null;
    }

    public ChessPosition queenHorizontalLeft(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() > 1){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() - 1;
            ChessPosition newPos = queenMove(tempR, tempC);
            return queenHorizontalLeft(newPos);
        }
        return null;

    }

    public ChessPosition queenDiagonalUpRight(ChessPosition startPos){
        if (startPos != null && (startPos.getRow() < 8 && startPos.getColumn() < 8)){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() + 1;
            startPos = queenMove(tempR,tempC);
            return queenDiagonalUpRight(startPos);
        }
        return null;
    }
    public ChessPosition queenDiagonalUpLeft(ChessPosition startPos){
        if (startPos != null && startPos.getRow() < 8 && startPos.getColumn() > 1){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() - 1;
            startPos = queenMove(tempR,tempC);
            return queenDiagonalUpLeft(startPos);
        }
        return null;
    }

    public ChessPosition queenDiagonalDownRight(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1 && startPos.getColumn() < 8){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() + 1;
            startPos = queenMove(tempR,tempC);
            return queenDiagonalDownRight(startPos);
        }
        return null;
    }

    public ChessPosition queenDiagonalDownLeft(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1 && startPos.getColumn() > 1){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() - 1;
            startPos = queenMove(tempR,tempC);
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
