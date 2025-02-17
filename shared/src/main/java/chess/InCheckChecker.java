package chess;

import java.util.*;


public class InCheckChecker {
    private ChessBoard board;
    private ArrayList<ChessPosition> opponentPieces;
    private ArrayList<ChessMove> allMoves = new ArrayList<>();
    private ChessGame.TeamColor color;
    public InCheckChecker(ChessBoard board, ArrayList<ChessPosition> opponentPieces, ChessGame.TeamColor color){
        this.board = board;
        this.opponentPieces = opponentPieces;
        this.color = color;
    }

    public List<ChessPosition> find() {
        //System.out.println("Number of opponent pieces: " +opponentPieces.size());
        for (ChessPosition pos : opponentPieces){
            //System.out.println(board.getPiece(pos).getTeamColor()+ " " + board.getPiece(pos).getPieceType());
            allMoves.addAll(new ChessPiece(board.getPiece(pos).getTeamColor(), board.getPiece(pos).getPieceType()).pieceMoves(board,pos));
        }
        //System.out.println("Opponent moves: "+ allMoves);
        List<ChessPosition> endPositions = allMoves.stream().map(ChessMove::getEndPosition).toList();
        return endPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InCheckChecker that = (InCheckChecker) o;
        return Objects.equals(board, that.board) && Objects.equals(opponentPieces, that.opponentPieces) && Objects.equals(allMoves, that.allMoves) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, opponentPieces, allMoves, color);
    }
}
