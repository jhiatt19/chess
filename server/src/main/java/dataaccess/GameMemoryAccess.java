package dataaccess;

import model.GameData;

import java.util.HashSet;

public class GameMemoryAccess implements GameDAO {
    HashSet<GameData> gameDB = new HashSet<>();
    public GameData createGame() {
        return null;
    };

    public GameData setGame() {
        return null;
    };

    public GameData listGame(){
        return null;
    };

    public GameData updateGame() {
        return null;
    };

    public void clear(){
        gameDB.clear();
    };
}
