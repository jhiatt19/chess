package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class InCheckChecker {
    private ChessBoard board;
    private ArrayList<ChessPosition> opponentPieces;
    private ArrayList<Collection<ChessMove>> allMoves;
    private ChessGame.TeamColor color;
    public InCheckChecker(ChessBoard board, ArrayList<ChessPosition> opponentPieces, ChessGame.TeamColor color){
        this.board = board;
        this.opponentPieces = opponentPieces;
        this.color = color;
    }

    public ArrayList<Collection<ChessMove>>find() {
        for (ChessPosition pos : opponentPieces){
            allMoves.add(new ChessPiece(color, board.getPiece(pos).getPieceType()).pieceMoves(board,pos));
        }
        return allMoves;
    }

}
