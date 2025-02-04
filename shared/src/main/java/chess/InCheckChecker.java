package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        for (ChessPosition pos : opponentPieces){
            allMoves.addAll(new ChessPiece(color, board.getPiece(pos).getPieceType()).pieceMoves(board,pos));
        }
        List<ChessPosition> endPositions = allMoves.stream().map(ChessMove::getEndPosition).toList();
        return endPositions;
    }

}
