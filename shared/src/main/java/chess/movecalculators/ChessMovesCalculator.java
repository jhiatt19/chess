package chess.movecalculators;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface ChessMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

}
