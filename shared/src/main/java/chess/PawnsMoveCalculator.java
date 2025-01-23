package chess;

import java.util.*;

public class PawnsMoveCalculator implements ChessMovesCalculator {
    private ChessBoard board;
    private final ChessPosition position;
    private final String pawnMoves[] = {"atckl", "atckr", "mv1", "mv2"};
    private final String pawnPromote[] = {"pmQ", "pmK", "pmB", "pmR"};
    private Hashtable<String, ChessMove> legalMoves = new Hashtable<>();
    private Hashtable<String, Hashtable<String,ChessMove>> pmOut = new Hashtable<>();


    public PawnsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        for (String mv : pawnMoves) {
            if (mv.equals("atckl")) { //Black atckl
                pawnAttackLeft(mv);
                System.out.println("atckl: " + pmOut.values());// + pmOut.values());
            }
            if (mv.equals("atckr")) {
                pawnAttackRight(mv);
               System.out.println("atckr: " + pmOut.values());
            }
            if (mv.equals("mv1") && position.getColumn() < 9) {
                pawnMove1(mv);
                System.out.println("mv1: " + pmOut.values());
            }

            if (mv.equals("mv2") && (position.getRow() == 2 || position.getRow() == 7)) {
                pawnMove2(mv);
                System.out.println("mv2: " + pmOut.size() + " " + pmOut.values());
                }
            }
            //System.out.println("All: " + legalMoves.values());
            return legalMoves.values();
        }

    public void pawnPromotion(ChessPosition endPos, String pm) {
        Hashtable<String, ChessMove> pmInner = new Hashtable<>();
            for (String pm2 : pawnPromote) {
                if (pm2.equals("pmQ")) {
                    pmInner.put(pm2, new ChessMove(position, endPos, ChessPiece.PieceType.QUEEN));
                } else if (pm2.equals("pmR")) {
                    pmInner.put(pm2, new ChessMove(position, endPos, ChessPiece.PieceType.ROOK));
                } else if (pm2.equals("pmK")) {
                    pmInner.put(pm2, new ChessMove(position, endPos, ChessPiece.PieceType.KNIGHT));
                } else if (pm2.equals("pmB")) {
                    pmInner.put(pm2, new ChessMove(position, endPos, ChessPiece.PieceType.BISHOP));
                }
            pmOut.put(pm, pmInner);
        }
            System.out.println(pmInner.size());
        //for (String tempVal : pawnMoves) {
            //for (String tempVal2 : pawnPromote) {
                //ChessMove intermediary = pmOut.get(tempVal).get(tempVal2);
                //System.out.println(tempVal + " " + tempVal2);
                //System.out.println(intermediary.getPromotionPiece());
            //}
        //}
    }
    public boolean pawnMove1(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null) {
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos, mv);
                    return true;
                }
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                return true;
            }
        }
        else {
            int tempR = position.getRow() + 1;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null) {
                if (endPos.getRow() == 8){
                    pawnPromotion(endPos, mv);
                    return true;
                }
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                return true;
            }
        }
        return false;
    }

    public void pawnMove2(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getRow() == 7) {
            int tempR = position.getRow() - 2;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null && pawnMove1("mv1")) {
                legalMoves.put(mv, new ChessMove(position, endPos, null));
            }
        } else if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.WHITE) && position.getRow() == 2) {
            int tempR = position.getRow() + 2;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null && pawnMove1("mv1")) {
                legalMoves.put(mv, new ChessMove(position, endPos, null));
            }
        }
    }

    public boolean pawnAttackLeft(String mv) {
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getColumn() < 8) {
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn() + 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos, mv);
                    return true;
                }
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                return true;
            }
        } else if (position.getColumn() > 1) { //White atckl
            int tempR = position.getRow() + 1;
            int tempC = position.getColumn() - 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 8) {
                    pawnPromotion(endPos, mv);
                    return true;
                }
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                return true;
            }
        }
        return false;
    }

    public boolean pawnAttackRight(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getColumn() > 1) {//Black atckr
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn() - 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos, mv);
                    return true;
                }
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                return true;
            }
        } else if (position.getColumn() < 8) {
            int tempR = position.getRow() + 1;
            int tempC = position.getColumn() + 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 8) {
                    pawnPromotion(endPos, mv);
                    return true;
                }
                legalMoves.put(mv, new ChessMove(position, endPos, null));
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PawnsMoveCalculator that = (PawnsMoveCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.deepEquals(pawnMoves, that.pawnMoves) && Objects.deepEquals(pawnPromote, that.pawnPromote) && Objects.equals(legalMoves, that.legalMoves) && Objects.equals(pmOut, that.pmOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, Arrays.hashCode(pawnMoves), Arrays.hashCode(pawnPromote), legalMoves, pmOut);
    }
}
