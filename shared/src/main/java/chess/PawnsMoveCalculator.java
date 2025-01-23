package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

public class PawnsMoveCalculator {
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
            if (mv.equals("atckl") && position.getColumn() > 1) {
                if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                    int tempR = position.getRow() - 1;
                    int tempC = position.getColumn() - 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) == null) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                        if (endPos.getRow() == 1){
                        pawnPromotion(endPos);
                    }
                }
                } else {
                    int tempR = position.getRow() + 1;
                    int tempC = position.getColumn() - 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) != null && board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                        if (endPos.getRow() == 8) {
                            pawnPromotion(endPos);
                        }
                    }
                }
            }
            if (mv.equals("atckr") && position.getColumn() < 8) {
                if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                    int tempR = position.getRow() - 1;
                    int tempC = position.getColumn();
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) == null && endPos.getRow() != 1) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                    } else {
                        legalMoves.put(mv, new ChessMove(position, endPos, ChessPiece.PieceType.QUEEN));
                    }
                } else {
                    int tempR = position.getRow() - 1;
                    int tempC = position.getColumn() + 1;
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) == null) {
                        noMoves.put(mv, new ChessMove(position, endPos, null));
                    } else if (board.getPiece(endPos).getTeamColor() != board.getPiece(position).getTeamColor() && endPos.getRow() != 8) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                    } else {
                        pawnPromotion(endPos);
                    }
                }
            }
            if (mv.equals("mv1") && position.getColumn() < 9) {
                if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                    int tempR = position.getRow() - 1;
                    int tempC = position.getColumn();
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) == null) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                        if (endPos.getRow() == 1) {
                            pawnPromotion(endPos);
                        }
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
                    }
                }
            }
            if (mv.equals("mv2") && (position.getRow() == 2 || position.getRow() == 7)) {
                if (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                    int tempR = position.getRow() - 2;
                    int tempC = position.getColumn();
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) == null) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                    }
                } else if (position.getRow() == 2) {
                    int tempR = position.getRow() + 2;
                    int tempC = position.getColumn();
                    ChessPosition endPos = new ChessPosition(tempR, tempC);
                    if (board.getPiece(endPos) == null) {
                        legalMoves.put(mv, new ChessMove(position, endPos, null));
                    }
                }
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

    //public pawnMove1(){}

    //public pawnMove2(){}

    //public pawnAttack(){}
}
