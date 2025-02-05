package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor color;
    private ChessBoard board;
    private ArrayList<ChessMove> empty = new ArrayList<>();
    public ChessGame() {
        this.color = TeamColor.WHITE;
        this.board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return color;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.color = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ArrayList<ChessMove> goodMoves = new ArrayList<>();
        System.out.println("Inside validMoves");
         var teamColor = board.getPiece(startPosition).getTeamColor();
        if (teamColor != null) {
            System.out.println(board.getPiece(startPosition).toString());
            Collection<ChessMove> allMoves = new ArrayList<>();
            allMoves.addAll(new ChessPiece(teamColor, board.getPiece(startPosition).getPieceType()).pieceMoves(board, startPosition));
            System.out.println(allMoves.size());
            for (ChessMove move : allMoves) {
                ChessBoard tempBoard = new ChessBoard(board);
                testMove(move);
                if (!isInCheck(teamColor)) {
                    goodMoves.add(move);
                }
                setBoard(tempBoard);

                //System.out.println(allMoves.size());
//                List<ChessPosition> endpos = new ArrayList<>(allMoves.stream().map(ChessMove::getEndPosition).toList());
//                System.out.println("Endpos list:" + endpos);
//                var illegalMoves = potentialMoves();
//                //System.out.println(illegalMoves);
//                List<ChessPosition> removedBadMoves = endpos.stream().filter(move -> !illegalMoves.contains(move)).toList();
//                System.out.println("filtered 1 list: " + removedBadMoves);
            }
            //Collection<ChessMove> filteredMoves = allMoves.stream().filter(move->!endpos.contains(move.getEndPosition())).toList();
            System.out.println("Number of moves: " + goodMoves.size());
            return goodMoves;
        }
        System.out.println("No piece at this position");
        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

    }
    public void testMove(ChessMove move){
        var start = move.getStartPosition();
        var end = move.getEndPosition();
        if (board.getPiece(end) != null){
            board.removePiece(end);
        }
        board.movePiece(board.getPiece(start),end);
        board.removePiece(start);
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (teamColor.equals(TeamColor.BLACK)){
            var inCheck = potentialMoves(TeamColor.WHITE);
            return inCheck.contains(board.findKing(teamColor));
        }
        else {
            var inCheck = potentialMoves(TeamColor.BLACK);
            return inCheck.contains(board.findKing(teamColor));
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            //Collection<ChessMove> kingMoves = validMoves(findKing());
            //return kingMoves.isEmpty();
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            //Collection<ChessMove> kingMoves = validMoves(findKing());
            //return kingMoves.isEmpty();
        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard(){
        return board;
    }

    public ChessPosition findKing(TeamColor teamColor){
        return board.findKing(teamColor);
    }

    public List<ChessPosition> potentialMoves(TeamColor teamColor){
        return new InCheckChecker(board,board.find(teamColor),teamColor).find();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return color == chessGame.color && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, board);
    }

}
