package chess;

public class Castling {
    private boolean whiteKingSideCastle = true;
    private boolean whiteQueenSideCastle = true;
    private boolean blackKingSideCastle = true;
    private boolean blackQueenSideCastle = true;
    private ChessBoard board;
    public Castling(ChessBoard board){
        this.board = board;
    }
    public boolean canCastle(ChessGame.TeamColor teamColor) {
        if (teamColor.equals(ChessGame.TeamColor.WHITE)) {
            if (whiteKingSideCastle) {
                ChessPosition kingSideBishop = new ChessPosition(1, 6);
                ChessPosition kingSideKnight = new ChessPosition(1, 7);
                ChessPosition Queen = new ChessPosition(1, 4);
                ChessPosition QueenSideBishop = new ChessPosition(1, 3);
                ChessPosition QueenSideKnight = new ChessPosition(1, 2);
            } else {
                if (blackKingSideCastle) {
                    ChessPosition kingSideBishop = new ChessPosition(8, 6);
                    ChessPosition kingSideKnight = new ChessPosition(8, 7);
                    if (board.getPiece(kingSideBishop) == null &&
                            board.getPiece(kingSideKnight) == null) {

                    }
                } else if (blackQueenSideCastle) {
                    ChessPosition Queen = new ChessPosition(8, 4);
                    ChessPosition QueenSideBishop = new ChessPosition(8, 3);
                    ChessPosition QueenSideKnight = new ChessPosition(8, 2);
                    if (board.getPiece(Queen) == null &&
                            board.getPiece(QueenSideBishop) == null &&
                            board.getPiece(QueenSideBishop) == null) {
                    }
                }
            }
        }
        return false;
    }

    public boolean checkRook(ChessGame.TeamColor calor, ChessPosition end) {
        if (board.getPiece(end).getPieceType().equals(ChessPiece.PieceType.ROOK)) {
            if (calor == ChessGame.TeamColor.WHITE) {
                whiteKingSideCastle = false;
            } else {
                blackKingSideCastle = false;
            }
        }
        return false;
    }
}
