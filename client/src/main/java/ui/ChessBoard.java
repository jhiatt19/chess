package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import javax.swing.plaf.BorderUIResource;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    //private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private final static String[] PIECES = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
    private static final String[] WHITE_EDGE = {" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
    private static final String[] BLACK_EDGE = {" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A "};
    public static void main(String[] args, ChessGame game) {
        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
        boolean highlight = false;
        if (args.length == 2 || args.length == 3) {
            if (args[1].toUpperCase().equals("WHITE")) {
                if (args.length == 3 && args[2].equals("HIGHLIGHT")){
                    highlight = true;
                }
                topBottomEdges(out, WHITE_EDGE);
                drawWhiteChessBoard(out,game,highlight);
                topBottomEdges(out, WHITE_EDGE);
            } else {
                if (args.length == 3 && args[2].equals("HIGHLIGHT")){
                    highlight = true;
                }
                topBottomEdges(out, BLACK_EDGE);
                drawBlackChessBoard(out,game,highlight);
                topBottomEdges(out, BLACK_EDGE);
            }
        }
        else {
            topBottomEdges(out, WHITE_EDGE);
            drawWhiteChessBoard(out,game,highlight);
            topBottomEdges(out, WHITE_EDGE);

            out.println();

            topBottomEdges(out, BLACK_EDGE);
            drawBlackChessBoard(out,game,highlight);
            topBottomEdges(out, BLACK_EDGE);
        }
            out.println();
    }

    private static void topBottomEdges(PrintStream out, String[] alphaChars) {
            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
                out.print(BLANK);
                setEdges(out);
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES + 2; ++boardCol) {
                    if (boardCol != 0 && boardCol != 9) {
                        out.print(alphaChars[boardCol-1]);
                    } else {
                        out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                    }
                }
                out.print(RESET);
                out.println();
            }
    }
    private static void drawBlackChessBoard(PrintStream out,ChessGame game, boolean highlight) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; boardRow +=2){
            // black perspective
            drawRowOfSquaresWhite(out,boardRow,game,highlight);
            drawRowOfSquaresBlack(out,boardRow+1,game,highlight);

        }
    }
    private static void drawWhiteChessBoard(PrintStream out,ChessGame game, boolean highlight) {
        for (int boardRow = 8; boardRow >= 1; boardRow -= 2){
            //white perspective
            drawRowOfSquaresBlack(out,boardRow,game,highlight);
            drawRowOfSquaresWhite(out,boardRow-1, game,highlight);

        }
    }

    private static void drawRowOfSquaresWhite(PrintStream out, int rowNum, ChessGame game,boolean highlight) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(BLANK);
            setEdges(out);
            out.print(" " + rowNum + " ");
            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES+1; ++boardCol) {
                var currPos = new ChessPosition(rowNum,boardCol);
                var square = game.getBoard().getPiece(currPos);
                boolean x = false;
                if (boardCol % 2 == 0) {
                    if (highlight) {
                        x = printLightHighlight(out, currPos, game);
                    }
                    if (!x){
                        setLightGrey(out);
                    }
                    printPieces(out,square);
                } else {
                    if (highlight){
                        x = printDarkHighlight(out,currPos,game);
                    }
                    if (!x) {
                        setDarkGrey(out);
                    }
                    printPieces(out,square);
                }
                //resetBoard(out);
            }
            out.print(RESET);
            setEdges(out);
            out.print(" " + rowNum + " ");
            out.print(RESET);
            System.out.println();
        }
    }

    private static void printPieces(PrintStream out, ChessPiece square) {
        if (square == null){
            out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }
        else if (square.getTeamColor().equals(ChessGame.TeamColor.WHITE)){
            setTextWhite(out);
            addPieces(out,square);
        }
        else if (square.getTeamColor().equals(ChessGame.TeamColor.BLACK)){
            setTextBlack(out);
            addPieces(out,square);
        }
    }

    private static boolean printLightHighlight(PrintStream out, ChessPosition currPos, ChessGame game){
        for (ChessMove move : game.getMoveHolder()){
            if (move.getEndPosition().equals(currPos)){
                setLightHighlight(out);
                return true;
            }
        }
        return false;
    }

    private static boolean printDarkHighlight(PrintStream out, ChessPosition currPos, ChessGame game){
        for (ChessMove move : game.getMoveHolder()){
            if (move.getEndPosition().equals(currPos)){
                setDarkHighlight(out);
                return true;
            }
        }
        return false;
    }

    private static void addPieces(PrintStream out, ChessPiece square){
        if (square.getPieceType().equals(ChessPiece.PieceType.ROOK)){
            out.print(" R ");
        } else if (square.getPieceType().equals(ChessPiece.PieceType.KNIGHT)){
            out.print(" N ");
        } else if (square.getPieceType().equals(ChessPiece.PieceType.BISHOP)){
            out.print(" B ");
        } else if (square.getPieceType().equals(ChessPiece.PieceType.QUEEN)){
            out.print(" Q ");
        } else if (square.getPieceType().equals(ChessPiece.PieceType.KING)){
            out.print(" K ");
        } else {
            out.print(" P ");
        }
    }

    private static void drawRowOfSquaresBlack(PrintStream out, int rowNum, ChessGame game, boolean highlight) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(BLANK);
            setEdges(out);
            out.print(" " + rowNum + " ");
            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES+1; ++boardCol) {
                var currPos = new ChessPosition(rowNum,boardCol);
                var square = game.getBoard().getPiece(currPos);
                boolean x = false;
                if (boardCol % 2 == 0) {
                    if (highlight){
                        x = printDarkHighlight(out,currPos,game);
                    }
                    if (!x){
                        setDarkGrey(out);
                    }
                    printPieces(out,square);
                } else {
                    if (highlight){
                        x = printLightHighlight(out,currPos,game);
                    }
                    if (!x){
                        setLightGrey(out);
                    }
                    printPieces(out,square);
                }
            }
            out.print(RESET);
            setEdges(out);
            out.print(" " + rowNum + " ");
            out.print(RESET);
            System.out.println();
        }
    }

    private static void setDarkGrey(PrintStream out){
        out.print(SET_BG_COLOR_DARK_GREY);
    }

    private static  void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
    }

    private static void setEdges(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

   private static void setTextBlack(PrintStream out){
       out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);

   }
   private static void setTextWhite(PrintStream out) {
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_WHITE);
   }

   private static void setDarkHighlight(PrintStream out){
        out.print(SET_BG_COLOR_DARK_GREEN);
   }

   private static void setLightHighlight(PrintStream out){
        out.print(SET_BG_COLOR_GREEN);
   }
}
