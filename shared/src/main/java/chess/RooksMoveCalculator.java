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
        verticalUp(position);
        verticalDown(position);
        horizontalLeft(position);
        horizontalRight(position);

        return legalMoves;
    }
    public ChessPosition move(int tempR, int tempC){
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            //.out.println(newPos.getRow() + " " + newPos.getColumn());
            return  newPos;
        }
        else {
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
                //.out.println("DURx " + newPos.getRow() + " " + newPos.getColumn());
            }
            return null;
        }
    }
    public ChessPosition verticalUp(ChessPosition startPos){
        if (startPos != null && startPos.getRow() < 8){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = move(tempR,tempC);
            return verticalUp(newPos);
        }
        return null;
    }

    public ChessPosition verticalDown(ChessPosition startPos){
        if (startPos != null && startPos.getRow() > 1){
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn();
            ChessPosition newPos = move(tempR,tempC);
            return verticalDown(newPos);
        }
        return null;
    }

    public ChessPosition horizontalRight(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() < 8){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() + 1;
            ChessPosition newPos = move(tempR,tempC);
            return horizontalRight(newPos);
        }
        return null;
    }

    public ChessPosition horizontalLeft(ChessPosition startPos){
        if (startPos != null && startPos.getColumn() > 1){
            int tempR = startPos.getRow();
            int tempC = startPos.getColumn() - 1;
            ChessPosition newPos = move(tempR,tempC);
            return horizontalLeft(newPos);
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
