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
    private Collection<ChessMove> moveHolder = new ArrayList<>();
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
        var teamColor = board.getPiece(startPosition).getTeamColor();
        if (teamColor != null) {
            Collection<ChessMove> allMoves = new ArrayList<>();
            allMoves.addAll(new ChessPiece(teamColor, board.getPiece(startPosition).getPieceType()).pieceMoves(board, startPosition));
            for (ChessMove move : allMoves) {
                ChessBoard tempBoard = new ChessBoard(board);
                testMove(move);
                if (!isInCheck(teamColor)) {
                    goodMoves.add(move);
                }
                setBoard(tempBoard);
            }
        }
        return goodMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (move == null){
            throw new InvalidMoveException();
        }
        var start = move.getStartPosition();
        var end = move.getEndPosition();
        if (board.getPiece(start) == null){
            throw new InvalidMoveException();
        }
        var calor = board.getPiece(start).getTeamColor();
        if (!calor.equals(getTeamTurn())){
            throw new InvalidMoveException();
        }
        moveHolder = validMoves(start);
        if (!moveHolder.contains(move)){
            throw new InvalidMoveException();
        }
        if (board.getPiece(end) != null){
            board.removePiece(end);
        }
        if (move.getPromotionPiece() != null) {
            if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
                board.addPiece(end, new ChessPiece(calor, ChessPiece.PieceType.QUEEN));
                board.removePiece(start);
            }
            else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
                board.addPiece(end, new ChessPiece(calor, ChessPiece.PieceType.ROOK));
                board.removePiece(start);
            }
            else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
                board.addPiece(end, new ChessPiece(calor, ChessPiece.PieceType.BISHOP));
                board.removePiece(start);
            }
            else {
                board.addPiece(end, new ChessPiece(calor, ChessPiece.PieceType.KNIGHT));
                board.removePiece(start);
            }

        }
        else {
            board.movePiece(board.getPiece(start), end);
            board.removePiece(start);
        }
        if(getTeamTurn().equals(TeamColor.WHITE)) {
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
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
            var allPieceMoves = findAll(teamColor);
            for (ChessPosition move : allPieceMoves){
                moveHolder = validMoves(move);
            }
            return moveHolder.isEmpty();
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
            var allPieceMoves = findAll(teamColor);
            for (ChessPosition move : allPieceMoves) {
                moveHolder = validMoves(move);
            }
            return moveHolder.isEmpty();
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

    public ArrayList<ChessPosition> findAll(TeamColor teamColor){
        return board.find(teamColor);
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
