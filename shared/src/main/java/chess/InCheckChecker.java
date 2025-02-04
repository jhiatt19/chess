package chess;

import java.util.ArrayList;
import java.util.Collection;

public class InCheckChecker {
    private ChessBoard board;
    private ChessGame.TeamColor color;
    public InCheckChecker(ChessGame.TeamColor color, ChessBoard board){
        this.board = board;
        this.color = color;
    }

    public Collection<ChessMove> find(ChessGame.TeamColor color) {
        var allPieceMoves = new ArrayList<Collection<ChessMove>>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; ++j) {

                if (.equals(color)) {
                    var type = new ChessPosition(i, j);
                    var typePiece = board.getPiece(type).getPieceType();
                    allPieceMoves.add(new ChessPiece(color, typePiece).pieceMoves(board, type));
                }

            }
        }
    }
}
