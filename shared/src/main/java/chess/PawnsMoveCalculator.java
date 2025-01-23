package chess;

import java.util.*;

public class PawnsMoveCalculator implements ChessMovesCalculator {
    private ChessBoard board;
    private final ChessPosition position;
    private final String pawnMoves[] = {"atckl", "atckr", "mv1", "mv2"};
    private final String pawnPromote[] = {"pmQ", "pmK", "pmB", "pmR"};
    //private final Integer initiliazerList[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    //private final ArrayList<ChessMove> promoteArray = new ArrayList<>();
    private ArrayList<ChessMove> legalMoves = new ArrayList<>();
    //private Hashtable<String, Hashtable<String,ChessMove>> pmOut = new Hashtable<>();


    public PawnsMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        for (String mv : pawnMoves) {
            if (mv.equals("atckl")) { //Black atckl
                pawnAttackLeft(mv);
                System.out.println(mv + " " + legalMoves);
            }
            if (mv.equals("atckr")) {
                pawnAttackRight(mv);
               System.out.println(mv + " " + legalMoves);
            }
            // if (mv.equals("mv2") && (position.getRow() == 2 || position.getRow() == 7)) {
            //     pawnMove2(mv);
            //     }
            if (mv.equals("mv1") && position.getColumn() < 9) {
                pawnMove1(mv);
                System.out.println(mv + " " + legalMoves);
            }

            }
            System.out.println("All: " + legalMoves);
            return legalMoves;
        }

    public void pawnPromotion(ChessPosition endPos) {
            for (String pm2 : pawnPromote) {
                if (pm2.equals("pmQ")) {
                    legalMoves.add((new ChessMove(position, endPos, ChessPiece.PieceType.QUEEN)));
                } else if (pm2.equals("pmR")) {
                    legalMoves.add(new ChessMove(position, endPos, ChessPiece.PieceType.ROOK));
                } else if (pm2.equals("pmK")) {
                    legalMoves.add(new ChessMove(position, endPos, ChessPiece.PieceType.KNIGHT));
                } else if (pm2.equals("pmB")) {
                    legalMoves.add(new ChessMove(position, endPos, ChessPiece.PieceType.BISHOP));
                }
        }
    }
    public void pawnMove1(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null) {
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos);
                }
                else { 
                    legalMoves.add(new ChessMove(position, endPos, null));
                }
                if (position.getRow() == 7){
                    int tempR2 = position.getRow() - 2;
                    int tempC2 = position.getColumn();
                    ChessPosition endPos2 = new ChessPosition(tempR2, tempC2);
                    if (board.getPiece(endPos2) == null){
                        legalMoves.add(new ChessMove(position, endPos2, null));
                    }
                }
            }
        }
        else {
            int tempR = position.getRow() + 1;
            int tempC = position.getColumn();
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) == null) {
                if (endPos.getRow() == 8){
                    pawnPromotion(endPos);
                }
                else {
                    legalMoves.add(new ChessMove(position, endPos, null));
                }
                if (position.getRow() == 2){
                    int tempR2 = position.getRow() + 2;
                    int tempC2 = position.getColumn();
                    ChessPosition endPos2 = new ChessPosition(tempR2, tempC2);
                    if (board.getPiece(endPos2) == null){
                        legalMoves.add(new ChessMove(position, endPos2, null));
                    }
            }
        }
    }

    // public void pawnMove2(String mv){
    //     if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getRow() == 7) {
    //         int tempR = position.getRow() - 2;
    //         int tempC = position.getColumn();
    //         ChessPosition endPos = new ChessPosition(tempR, tempC);
    //         if (board.getPiece(endPos) == null && pawnMove1("mv1")) {
    //             legalMoves.add(new ChessMove(position, endPos, null));
    //         }
    //     } else if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.WHITE) && position.getRow() == 2) {
    //         int tempR = position.getRow() + 2;
    //         int tempC = position.getColumn();
    //         ChessPosition endPos = new ChessPosition(tempR, tempC);
    //         if (board.getPiece(endPos) == null && pawnMove1("mv1")) {
    //             legalMoves.add(new ChessMove(position, endPos, null));
    //         }
    //     }
    }

    public void pawnAttackLeft(String mv) {
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getColumn() < 8) {
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn() + 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos);
                }
                else {
                    legalMoves.add(new ChessMove(position, endPos, null));
                }
            }
        } else if (position.getColumn() > 1) { //White atckl
            int tempR = position.getRow() + 1;
            int tempC = position.getColumn() - 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 8) {
                    pawnPromotion(endPos);
                }
                else {
                    legalMoves.add(new ChessMove(position, endPos, null));
                }
            }
        }
    }

    public void pawnAttackRight(String mv){
        if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getColumn() > 1) {//Black atckr
            int tempR = position.getRow() - 1;
            int tempC = position.getColumn() - 1;
            ChessPosition endPos = new ChessPosition(tempR, tempC);
            if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                if (endPos.getRow() == 1) {
                    pawnPromotion(endPos);
                }
                else {
                    legalMoves.add(new ChessMove(position, endPos, null));
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
                else {
                    legalMoves.add(new ChessMove(position, endPos, null));
                }
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PawnsMoveCalculator that = (PawnsMoveCalculator) o;
        return Objects.equals(board, that.board) && Objects.equals(position, that.position) && Objects.deepEquals(pawnMoves, that.pawnMoves) && Objects.deepEquals(pawnPromote, that.pawnPromote) && Objects.equals(legalMoves, that.legalMoves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, position, Arrays.hashCode(pawnMoves), Arrays.hashCode(pawnPromote), legalMoves);
    }
}
