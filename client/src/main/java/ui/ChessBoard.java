package ui;

import chess.ChessGame;
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
        if (args.length == 2) {
            if (args[1].equals("WHITE")) {
                topBottomEdges(out, WHITE_EDGE);
                drawWhiteChessBoard(out,game);
                topBottomEdges(out, WHITE_EDGE);
            } else {

                topBottomEdges(out, BLACK_EDGE);
                drawBlackChessBoard(out,game);
                topBottomEdges(out, BLACK_EDGE);
            }
        } else {
            topBottomEdges(out, WHITE_EDGE);
            drawWhiteChessBoard(out,game);
            topBottomEdges(out, WHITE_EDGE);

            out.println();

            topBottomEdges(out, BLACK_EDGE);
            drawBlackChessBoard(out,game);
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
    private static void drawBlackChessBoard(PrintStream out,ChessGame game) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; boardRow +=2){
            // black perspective
            drawRowOfSquaresBlack(out,boardRow,game);
            drawRowOfSquaresWhite(out,boardRow+1,game);
        }
    }
    private static void drawWhiteChessBoard(PrintStream out,ChessGame game) {
        for (int boardRow = 8; boardRow >= 1; boardRow -= 2){
            //white perspective
            drawRowOfSquaresWhite(out,boardRow, game);
            drawRowOfSquaresBlack(out,boardRow-1,game);
        }
    }

    private static void drawRowOfSquaresWhite(PrintStream out, int rowNum, ChessGame game) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(BLANK);
            setEdges(out);
            out.print(" " + rowNum + " ");
            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES+1; ++boardCol) {
                var square = game.getBoard().getPiece(new ChessPosition(rowNum,boardCol));
                if (boardCol % 2 == 0) {
                    setLightGrey(out);
                    printPieces(out,square);
                } else {
                    setDarkGrey(out);
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

    private static void drawRowOfSquaresBlack(PrintStream out, int rowNum, ChessGame game) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(BLANK);
            setEdges(out);
            out.print(" " + rowNum + " ");
            for (int boardCol = 1; boardCol < BOARD_SIZE_IN_SQUARES+1; ++boardCol) {
                var square = game.getBoard().getPiece(new ChessPosition(rowNum,boardCol));
                if (boardCol % 2 == 0) {
                    setDarkGrey(out);
                    printPieces(out,square);
                } else {
                    setLightGrey(out);
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
}
