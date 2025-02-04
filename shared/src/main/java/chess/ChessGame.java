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
    public ChessGame() {
        color = TeamColor.WHITE;
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
        if (board.getPiece(startPosition) == null){
            return null;
        }
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves.addAll(new ChessPiece(getTeamTurn(),board.getPiece(startPosition).getPieceType()).pieceMoves(board,startPosition));
        List<ChessPosition> endpos = new ArrayList<>(allMoves.stream().map(ChessMove::getEndPosition).toList());
        var illegalMoves = potentialMoves();
        //System.out.println(illegalMoves);
        endpos.removeIf(illegalMoves::contains);
        Collection<ChessMove> filteredMoves = allMoves.stream().filter(move->!endpos.contains(move.getEndPosition())).toList();
        System.out.println(filteredMoves);
        return filteredMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //Collection<ChessMove> allMoves = new InCheckChecker(board).find(color);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        var inCheck = potentialMoves();
        if (inCheck.contains(findKing())){
            return true;
        }
        return false;
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
        board.resetBoard();
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public ChessPosition findKing(){
        return board.findKing(getTeamTurn());
    }

    public List<ChessPosition> potentialMoves(){
        return new InCheckChecker(board,board.find(color),getTeamTurn()).find();
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
