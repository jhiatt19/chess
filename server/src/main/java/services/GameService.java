package services;

import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;

import java.util.HashMap;
import java.util.UUID;

public class GameService {
    HashMap<String,String> returnVal = new HashMap<>();
    private final GameDAO gameData;

    public GameService() {
        this.gameData = new GameDAO() {
            @Override
            public GameData createGame() {
                return null;
            }

            @Override
            public GameData setGame() {
                return null;
            }

            @Override
            public GameData listGame() {
                return null;
            }

            @Override
            public GameData updateGame() {
                return null;
            }

            @Override
            public void clear() {

            }
        };
    }

    public GameService(GameDAO gameData){
        this.gameData = gameData;
    }
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
