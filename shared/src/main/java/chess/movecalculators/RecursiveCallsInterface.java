package chess.movecalculators;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public interface RecursiveCallsInterface {
    ChessPosition move(int tempR, int tempC, ArrayList<ChessMove> moves);
}
