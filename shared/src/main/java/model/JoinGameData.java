package model;

public record JoinGameData(int gameID, String playerColor) {
    public JoinGameData(int gameID, String playerColor){
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

}
