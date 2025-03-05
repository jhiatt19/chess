package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;

public class RecursiveCalls implements RecursiveCallsInterface {
    ChessBoard board;
    ChessPosition position;

    public RecursiveCalls(ChessBoard board, ChessPosition position){
        this.board = board;
        this.position = position;
    }

    public ChessPosition move(int tempR, int tempC, ArrayList<ChessMove> legalMoves){
        ChessPosition newPos = new ChessPosition(tempR,tempC);
        if (board.getPiece(newPos) == null){
            legalMoves.add(new ChessMove(position,newPos,null));
            return  newPos;
        }
        else {
            if (!board.getPiece(newPos).getTeamColor().equals(board.getPiece(position).getTeamColor())){
                legalMoves.add(new ChessMove(position, newPos, null));
            }
            return null;
        }
    }
}
