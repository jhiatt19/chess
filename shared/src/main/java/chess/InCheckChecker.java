package chess;

import java.util.*;


public class InCheckChecker {
    private ChessBoard board;
    private ArrayList<ChessPosition> opp;
    private ArrayList<ChessMove> moves = new ArrayList<>();
    private ChessGame.TeamColor color;
    public InCheckChecker(ChessBoard board, ArrayList<ChessPosition> opp, ChessGame.TeamColor color){
        this.board = board;
        this.opp = opp;
        this.color = color;
    }

    public List<ChessPosition> find() {
        //System.out.println("Number of opponent pieces: " +opp.size());
        for (ChessPosition pos : opp){
            //System.out.println(board.getPiece(pos).getTeamColor()+ " " + board.getPiece(pos).getPieceType());
            moves.addAll(new ChessPiece(board.getPiece(pos).getTeamColor(), board.getPiece(pos).getPieceType()).pieceMoves(board,pos));
        }
        //System.out.println("Opponent moves: "+ moves);
        List<ChessPosition> endPositions = moves.stream().map(ChessMove::getEndPosition).toList();
        return endPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InCheckChecker that = (InCheckChecker) o;
        return Objects.equals(board, that.board) && Objects.equals(opp, that.opp) && Objects.equals(moves, that.moves) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, opp, moves, color);
    }
}
