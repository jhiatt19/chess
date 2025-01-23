package chess;

import java.util.*;

public class PawnsMoveCalculator implements ChessMovesCalculator {
    private ChessBoard board;
    private final ChessPosition position;
    private final String pawnMoves[] = {"atckl", "atckr", "mv1", "mv2"};
    private final String pawnPromote[] = {"pmQ", "pmK", "pmB", "pmR"};
    private Hashtable<String, ChessMove> legalMoves = new Hashtable<>();
    private Hashtable<String, ChessMove> noMoves = new Hashtable<>();

    public PawnsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        for (String mv : pawnMoves) {
            if (mv.equals("atckl")) { //Black atckl
                if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getColumn() < 8) {
                    int tempR = position.getRow() - 1;
                    int tempC = position.getColumn() + 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                        if (endPos.getRow() == 1) {
                            pawnPromotion(endPos);
                        }
                    }
                } else if (position.getColumn() > 1) { //White atckl
                    int tempR = position.getRow() + 1;
                    int tempC = position.getColumn() - 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                    }
                    if (endPos.getRow() == 8) {
                        pawnPromotion(endPos);
                    }
                }
            }
            if (mv.equals("atckr")) {
                if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getColumn() > 1) {//Black atckr
                    int tempR = position.getRow() - 1;
                    int tempC = position.getColumn() - 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                        if (endPos.getRow() == 1) {
                            pawnPromotion(endPos);
                        }
                    }
                } else if (position.getColumn() < 8) {
                    int tempR = position.getRow() + 1;
                    int tempC = position.getColumn() + 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        if (endPos.getRow() == 8) {
                            pawnPromotion(endPos);
                        }
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                    }
                }
            }
            if (mv.equals("mv1") && position.getColumn() < 9) {
                pawnMove1(mv);
            }

            if (mv.equals("mv2") && (position.getRow() == 2 || position.getRow() == 7)) {
                    pawnMove2(mv);
                }
            }
            return legalMoves.values();
        }

    public void pawnPromotion(ChessPosition endPos){
        for (String pm : pawnPromote) {
            if (pm.equals("pmQ")) {
                legalMoves.put(pm, new ChessMove(position, endPos, ChessPiece.PieceType.QUEEN));
            } else if (pm.equals("pmR")) {
                legalMoves.put(pm, new ChessMove(position, endPos, ChessPiece.PieceType.ROOK));
            } else if (pm.equals("pmK")) {
                legalMoves.put(pm, new ChessMove(position, endPos, ChessPiece.PieceType.KNIGHT));
            } else if (pm.equals("pmB")) {
                legalMoves.put(pm, new ChessMove(position, endPos, ChessPiece.PieceType.BISHOP));
            }
        }
    }

    public boolean pawnMove1(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null) {
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos);
                }
                return true;
            }
        }
        else {
            int tempR = position.getRow() + 1;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null) {
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                if (endPos.getRow() == 8){
                    pawnPromotion(endPos);
                }
                return true;
            }
        }
        return false;
    }

    public void pawnMove2(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            int tempR = position.getRow() - 2;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null && pawnMove1("mv1")) {
                legalMoves.put(mv, new ChessMove(position, endPos, null));
            }
        } else if (position.getRow() == 2) {
            int tempR = position.getRow() + 2;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null && pawnMove1("mv1")) {
                legalMoves.put(mv, new ChessMove(position, endPos, null));
            }
        }
    }

    //public pawnAttack(){}


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PawnsMoveCalculator that = (PawnsMoveCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.deepEquals(pawnMoves, that.pawnMoves) && Objects.deepEquals(pawnPromote, that.pawnPromote) && Objects.equals(legalMoves, that.legalMoves) && Objects.equals(noMoves, that.noMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, Arrays.hashCode(pawnMoves), Arrays.hashCode(pawnPromote), legalMoves, noMoves);
    }
}
