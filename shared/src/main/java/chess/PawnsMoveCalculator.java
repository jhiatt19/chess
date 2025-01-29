package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class PawnsMoveCalculator implements ChessMovesCalculator{
    private final ChessBoard board;
    private final ChessPosition position;
    private final ArrayList<ChessMove> legalMoves = new ArrayList<>();
    public PawnsMoveCalculator(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition startPos){
        if (board.getPiece(startPos).getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            moveForward(startPos);
            //System.out.println("White Attack Moves: " + legalMoves.size());
            whiteAttack(startPos);
            //System.out.println("White Forward Moves: " + legalMoves.size());
        }
        else {
            moveBackward(startPos);
            //System.out.println("Black Forward Moves: " + legalMoves.size());
            blackAttack(startPos);
            //System.out.println("Black Attack Moves: " + legalMoves.size());
        }
        return legalMoves;
    }

    public void whiteAttack(ChessPosition startPos){
        if (startPos.getColumn() < 8) {
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() + 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())) {
                if (tempPos.getRow() != 8) {
                    legalMoves.add(new ChessMove(position, tempPos, null));
                }
                else {
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.QUEEN));
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.BISHOP));
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.ROOK));
                }
            }
        }
        if (startPos.getColumn() > 1){
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn() - 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())) {
                if (tempPos.getRow() != 8) {
                    legalMoves.add(new ChessMove(position, tempPos, null));
                }
                else {
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.QUEEN));
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.BISHOP));
                    legalMoves.add(new ChessMove(position,tempPos, ChessPiece.PieceType.ROOK));
                }
            }
        }
    }

    public void blackAttack(ChessPosition startPos) {
        if (startPos.getColumn() < 8) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() + 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())) {
                if (tempPos.getRow() != 1) {
                    legalMoves.add(new ChessMove(position, tempPos, null));
                } else {
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.QUEEN));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.BISHOP));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.ROOK));
                }
            }
        }
        if (startPos.getColumn() > 1) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn() - 1;
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) != null && !board.getPiece(tempPos).getTeamColor().equals(board.getPiece(position).getTeamColor())) {
                if (tempPos.getRow() != 1) {
                    legalMoves.add(new ChessMove(position, tempPos, null));
                } else {
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.QUEEN));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.BISHOP));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.ROOK));
                }
            }
        }
    }

    public void moveBackward(ChessPosition startPos) {
        if (startPos.getRow() > 1) {
            int tempR = startPos.getRow() - 1;
            int tempC = startPos.getColumn();
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                if (tempPos.getRow() == 1) {
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.QUEEN));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.BISHOP));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.ROOK));
                }
                else{
                    legalMoves.add(new ChessMove(position, tempPos, null));
                }
                if (startPos.getRow() == 7) {
                    int tempR2 = startPos.getRow() - 2;
                    int tempC2 = startPos.getColumn();
                    ChessPosition tempPos2 = new ChessPosition(tempR2, tempC2);
                    if (board.getPiece(tempPos2) == null) {
                        legalMoves.add(new ChessMove(position, tempPos2, null));
                    }
                }
            }
        }
    }

    public void moveForward(ChessPosition startPos) {
        if (startPos.getRow() < 8) {
            int tempR = startPos.getRow() + 1;
            int tempC = startPos.getColumn();
            ChessPosition tempPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(tempPos) == null) {
                if (tempPos.getRow() == 8) {
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.QUEEN));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.KNIGHT));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.BISHOP));
                    legalMoves.add(new ChessMove(position, tempPos, ChessPiece.PieceType.ROOK));
                } else {
                    legalMoves.add(new ChessMove(position, tempPos, null));
                }
                if (startPos.getRow() == 2) {
                    int tempR2 = startPos.getRow() + 2;
                    int tempC2 = startPos.getColumn();
                    ChessPosition tempPos2 = new ChessPosition(tempR2, tempC2);
                    if (board.getPiece(tempPos2) == null) {
                        legalMoves.add(new ChessMove(position, tempPos2, null));
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
        return Objects.equals(board, pawnCalc.board) && Objects.equals(position, pawnCalc.position) && Objects.equals(legalMoves, pawnCalc.legalMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, legalMoves);
    }
}

