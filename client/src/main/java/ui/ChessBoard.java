package ui;

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
    public static void main(String[] args) {
        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
        if (args.length == 2) {
            if (args[1].equals("WHITE")) {
                topBottomEdges(out, WHITE_EDGE);
                drawWhiteChessBoard(out);
                topBottomEdges(out, WHITE_EDGE);
            } else {

                topBottomEdges(out, BLACK_EDGE);
                drawBlackChessBoard(out);
                topBottomEdges(out, BLACK_EDGE);
            }
        } else {
            topBottomEdges(out, WHITE_EDGE);
            drawWhiteChessBoard(out);
            topBottomEdges(out, WHITE_EDGE);

            out.println();

            topBottomEdges(out, BLACK_EDGE);
            drawBlackChessBoard(out);
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
    private static void drawBlackChessBoard(PrintStream out) {
        for (int boardRow = 1; boardRow <= BOARD_SIZE_IN_SQUARES; boardRow +=2){
            // black perspective
            drawRowOfSquaresBlack(out,boardRow);
            drawRowOfSquaresWhite(out,boardRow+1);
        }
    }
    private static void drawWhiteChessBoard(PrintStream out) {
        for (int boardRow = 8; boardRow >= 1; boardRow -= 2){
            //white perspective
            drawRowOfSquaresWhite(out,boardRow);
            drawRowOfSquaresBlack(out,boardRow-1);
        }
    }

    private static void drawRowOfSquaresWhite(PrintStream out, int rowNum){
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow){
            out.print(BLANK);
            setEdges(out);
            out.print(" " + rowNum + " ");
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if (boardCol % 2 == 0) {
                    setLightGrey(out);
                    printPieces(out,rowNum,boardCol);
                } else {
                    setDarkGrey(out);
                    printPieces(out,rowNum,boardCol);
                }

                }
                //resetBoard(out);
            }
            out.print(RESET);
            setEdges(out);
            out.print(" " + rowNum + " ");
            out.print(RESET);
            System.out.println();
        }


    private static void printPieces(PrintStream out,int rowNum, int boardCol) {
        setDarkGrey(out);
        if (rowNum == 1 || rowNum == 8) {
            if (rowNum == 8) {
                setTextBlack(out);
            } else {
                setTextWhite(out);
            }
            out.print(PIECES[boardCol]);
        } else if (rowNum == 2 || rowNum == 7) {
            if (rowNum == 7) {
                setTextBlack(out);
            } else {
                setTextWhite(out);
            }
            out.print(" P ");
        } else {
            out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
        }
    }

    private static void drawRowOfSquaresBlack(PrintStream out, int rowNum) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(BLANK);
            setEdges(out);
            out.print(" " + rowNum + " ");
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if (boardCol % 2 == 0) {
                    setDarkGrey(out);
                    printPieces(out,rowNum,boardCol);
                } else {
                    setLightGrey(out);
                    printPieces(out,rowNum,boardCol);
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
