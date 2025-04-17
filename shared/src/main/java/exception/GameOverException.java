package exception;

import chess.ChessGame;

public class GameOverException extends Exception{
    private ChessGame game;
    public GameOverException(String message, ChessGame game){
        super(message);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }
}
