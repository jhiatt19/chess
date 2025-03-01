package dataaccess;

//import exception.ResponseException;
import model.GameData;

public interface GameDAO {
    GameData createGame(); //implement throw

    GameData setGame();

    GameData listGame();

    GameData updateGame();

    void clear();

}
