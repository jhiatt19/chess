package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition pos;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    private final ArrayList<int[]> possibleMoves = new ArrayList<>();
    public PawnsMoveCalculator(ChessBoard board, ChessPosition pos){
        this.board = board;
        this.pos = pos;
        possibleMoves.add(new int[]{1, 0});
        possibleMoves.add(new int[]{2, 0});
        possibleMoves.add(new int[]{1, 1});
        possibleMoves.add(new int[]{1,-1});
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPos){
        if (board.getPiece(startPos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            move(startPos,1,8,1);
            attack(startPos,1,8);
        }
        else {
            move(startPos,-1,1,8);
            attack(startPos,-1,1);
        }
        return legalMoves;
    }

    public void promotion(ChessPosition tempPos, int endInt){
            if (tempPos.getRow() == endInt) {
                legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.QUEEN));
                legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.KNIGHT));
                legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.BISHOP));
                legalMoves.add(new ChessMove(pos, tempPos, ChessPiece.PieceType.ROOK));
            }
    }

    public void mover(int tempR, int tempC, int endInt){
        ChessPosition tempPos = new ChessPosition(tempR, tempC);
        if (board.getPiece(tempPos) == null) {
            promotion(tempPos,endInt);
        }
        else {
            legalMoves.add(new ChessMove(pos, tempPos, null));
        }
    }

    public void move(ChessPosition startPos, int rowDirection, int endInt, int startInt) {
        int tempR = startPos.getRow() + rowDirection;
        int tempC = startPos.getColumn();
        mover(tempR, tempC, endInt);
        if (startPos.getRow() == startInt) {
            tempR = tempR + rowDirection;
            mover(tempR, tempC, endInt);
        }
    }

    public void attack(ChessPosition startPos, int direction, int endInt) {
        if (startPos.getColumn() < 8) {
            int tempR = startPos.getRow() + direction;
            int tempC = startPos.getColumn() + direction;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(pos).getTeamColor())) {
                if (tempPos.getRow() != 8) {
                    legalMoves.add(new ChessMove(pos, tempPos, null));
                } else {
                    promotion(tempPos,endInt);
                }
            }
        if (startPos.getColumn() > 1) {
           tempR = startPos.getRow() + direction;
           tempC = startPos.getColumn() - direction;
           tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(pos).getTeamColor())) {
                if (tempPos.getRow() != 8) {
                    legalMoves.add(new ChessMove(pos, tempPos, null));
                } else {
                    promotion(tempPos,endInt);
                }
            }
        }
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

