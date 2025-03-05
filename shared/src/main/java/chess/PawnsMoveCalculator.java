package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition pos;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    public PawnsMoveCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPos){
        if (board.getPiece(startPos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            move(startPos,1,8,2);
            attack(startPos,1,8);
        }
        else {
            move(startPos,-1,1,7);
            attack(startPos,-1,1);
        }
        return legalMoves;
    }

    public void promotion(ChessPosition tempPos, int endInt){
            legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.QUEEN));
            legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.KNIGHT));
            legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.BISHOP));
            legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.ROOK));
    }

    public boolean mover(int tempR, int tempC, int endInt) {
        ChessPosition tempPos = new ChessPosition(tempR, tempC);
        if (board.getPiece(tempPos) == null) {
            if (tempR == endInt) {
                promotion(tempPos, endInt);
            } else {
                legalMoves.add(new ChessMove(pos, tempPos, null));
            }
            return true;
        }
        return false;
    }

    public void move(ChessPosition startPos, int rowDirection, int endInt, int startInt) {
        int tempR = startPos.getRow() + rowDirection;
        int tempC = startPos.getColumn();
        if (mover(tempR, tempC, endInt) && startPos.getRow() == startInt) {
            tempR = tempR + rowDirection;
            mover(tempR, tempC, endInt);
        }
    }

    public void attacker(int tempR, int tempC, int endInt) {
        ChessPosition tempPos = new ChessPosition(tempR, tempC);
        if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(pos).getTeamColor())) {
            if (tempPos.getRow() != 8 && tempPos.getRow() != 1) {
                legalMoves.add(new ChessMove(pos, tempPos, null));
            } else {
                promotion(tempPos, endInt);
            }
        }
    }

    public void attack(ChessPosition startPos, int direction, int endInt) {
        if (startPos.getColumn() != 8 && direction == 1) {
            int tempR = startPos.getRow() + direction;
            int tempC = startPos.getColumn() + direction;
            attacker(tempR,tempC,endInt);
            }
        if (startPos.getColumn() != 1 && direction == 1) {
           int tempR = startPos.getRow() + direction;
           int tempC = startPos.getColumn() - direction;
           attacker(tempR,tempC,endInt);
        }
        if (startPos.getColumn() != 8 && direction == -1) {
            int tempR = startPos.getRow() + direction;
            int tempC = startPos.getColumn() - direction;
            attacker(tempR,tempC,endInt);
        }
        if (startPos.getColumn() != 1 && direction == -1) {
            int tempR = startPos.getRow() + direction;
            int tempC = startPos.getColumn() + direction;
            attacker(tempR,tempC,endInt);
        }
}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PawnsMoveCalculator pawnCalc = (PawnsMoveCalculator) o;
        return Objects.equals(board, pawnCalc.board) && Objects.equals(pos, pawnCalc.pos) && Objects.equals(legalMoves, pawnCalc.legalMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, pos, legalMoves);
    }
}

