package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class RooksMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();

    public RooksMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        rookVerticalUp(position);
        rookVerticalDown(position);
        rookHorizontalLeft(position);
        rookHorizontalRight(position);

        return legalMoves;
    }
    public ChessPosition rookMove(int tempR, int tempC){
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
            legalMoves.add(new ChessMove(position, newPos, null));
            return null;
        }
        else {
            legalMoves.add(new ChessMove(position,newPos,null));
            return  newPos;
        }
    }
    public ChessPosition rookVerticalUp(ChessPosition startPos){
        if (startPos != null && startPos.getRow() < 8){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = rookMove(tempR,tempC);
            return rookVerticalUp(newPos);
        }
        return null;
    }

    public ChessPosition rookVerticalDown(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = rookMove(tempR,tempC);
            return rookVerticalDown(newPos);
        }
        return null;
    }

    public ChessPosition rookHorizontalRight(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() < 8){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() + 1;
            ChessPosition newPos = rookMove(tempR,tempC);
            return rookHorizontalRight(newPos);
        }
        return null;
    }

    public ChessPosition rookHorizontalLeft(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() > 1){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() - 1;
            ChessPosition newPos = rookMove(tempR,tempC);
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
