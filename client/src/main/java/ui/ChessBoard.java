package ui;

import javax.swing.plaf.BorderUIResource;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    //private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;
    private final static String[] pieces = {" R ", " N ", " B ", " Q ", " K ", " B ", " N ", " R "};
    //private final static String[] whitePieces = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
    private static final String[] whiteEdge = {" A ", " B ", " C ", " D ", " E ", " F ", " G ", " H "};
    private static final String[] blackEdge = {" H ", " G ", " F ", " E ", " D ", " C ", " B ", " A "};
    public static void main(String[] args) {
        var out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
        out.println();
        out.print("White Board:");
        out.println();

        topBottomEdges(out,whiteEdge);
        drawWhiteChessBoard(out);
        topBottomEdges(out,whiteEdge);

        out.println();
        out.print("Black Board:");
        out.println();

        topBottomEdges(out,blackEdge);
        drawBlackChessBoard(out);
        topBottomEdges(out,blackEdge);

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
                        if (rowNum == 1 || rowNum == 8) {
                            if (rowNum == 8){
                                setTextBlack(out);
                            } else {
                                setTextWhite(out);
                            }
                            out.print(pieces[boardCol]);
                        } else if (rowNum == 2 || rowNum == 7) {
                            if (rowNum == 7){
                                setTextBlack(out);
                            } else {
                                setTextWhite(out);
                            }
                            out.print(" P ");
                        }
                        else {
                            out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                        }
                } else {
                    setDarkGrey(out);
                        if (rowNum == 1 || rowNum == 8){
                            if (rowNum == 8){
                                setTextBlack(out);
                            } else {
                                setTextWhite(out);
                            }
                            out.print(pieces[boardCol]);
                        } else if (rowNum == 2 || rowNum == 7) {
                            if (rowNum == 7){
                                setTextBlack(out);
                            } else {
                                setTextWhite(out);
                            }
                            out.print(" P ");
                        }
                        else {
                            out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                        }
                }
                //resetBoard(out);
            }
            setEdges(out);
            out.print(" " + rowNum + " ");
            out.print(RESET);
            System.out.println();
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
                    if (rowNum == 1 || rowNum == 8) {
                        if (rowNum == 8){
                            setTextBlack(out);
                        } else {
                            setTextWhite(out);
                        }
                        out.print(pieces[boardCol]);
                    } else if (rowNum == 2 || rowNum == 7) {
                        if (rowNum == 7){
                            setTextBlack(out);
                        } else {
                            setTextWhite(out);
                        }
                        out.print(" P ");
                    } else {
                        out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                    }
                } else {
                    setLightGrey(out);
                    if (rowNum == 1 || rowNum == 8) {
                        if (rowNum == 8){
                            setTextBlack(out);
                        } else {
                            setTextWhite(out);
                        }
                        out.print(pieces[boardCol]);
                    } else if (rowNum == 2 || rowNum == 7) {
                        if (rowNum == 7){
                            setTextBlack(out);
                        } else {
                            setTextWhite(out);
                        }
                        out.print(" P ");
                    } else {
                        out.print(BLANK.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                    }
                }
            }
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
