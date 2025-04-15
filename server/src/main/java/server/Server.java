package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import server.websocket.WebSocketHandler;
import services.AuthService;
import services.GameService;
import services.UserService;
import spark.*;
import websocket.commands.UserGameCommand;

import java.sql.SQLException;
import java.util.*;

public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final WebSocketHandler webSocketHandler;

    public Server()  {
        GameDAO gameDAO = new GameSqlDataAccess();
        AuthDAO authDAO = new AuthSqlDataAccess();
        UserDAO userDAO = new UserSqlDataAccess();
        this.authService = new AuthService(authDAO);
        this.gameService = new GameService(gameDAO);
        this.userService = new UserService(userDAO);
        webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort){
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::login);
        Spark.delete("/session",this::deleteAuth);
        Spark.get("/game",this::listGame);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);
        Spark.put("/game/:gameid",this::updateGame);
        Spark.get("/game/:gameid",this::getGame);
        Spark.delete("/db",this::clear);
        Spark.exception(ResponseException.class,this::exceptionHandler);
        Spark.exception(DataAccessException.class,this::dataExceptionHandler);
        //This line initializes the server and can be removed once you have a functioning endpoint

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public int port() {
        return Spark.port();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    private void dataExceptionHandler(DataAccessException ex,Request req, Response res){
        res.status(500);
        res.body(ex.getMessage());
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res){
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }
    private Object createUser(Request req, Response res) throws ResponseException, DataAccessException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var madeUser = userService.createUser(user);
        var authUser = DataTransformation.transform(madeUser,generateToken());
        authService.setAuth(authUser);
        res.status(200);
        res.type("application/json");
        return new Gson().toJson(authUser);
    }

    private Object login(Request req, Response res) throws ResponseException, DataAccessException, SQLException {
        var user = new Gson().fromJson(req.body(),UserData.class);
        var loginUser = userService.checkUser(user);
        var authUser = DataTransformation.transform(loginUser,generateToken());
        authUser = authService.setAuth(authUser);
        res.status(200);
        res.type("application/json");
        return new Gson().toJson(authUser);
    }

    private Object deleteAuth(Request req, Response res) throws ResponseException, SQLException, DataAccessException {
        var token = req.headers("authorization");
        authService.deleteAuth(token);
        res.status(200);
        return new Gson().toJson(Map.of());
    }

    private Object listGame(Request req, Response res) throws ResponseException, DataAccessException {
        var token = req.headers("authorization");
        if (authService.checkAuth(token) != null){
            List<GameData> gameList = gameService.listGame();
            List<String> newList = new ArrayList<>();
            if (req.headers("test") != null) {
                for (GameData game : gameList) {
                    GameData showGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
                    var jsonGame = new Gson().toJson(showGame);
                    newList.add(jsonGame);
                }
                return new Gson().toJson(newList);
            }
            else {
                return new Gson().toJson(Map.of("games",gameList));
            }
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object createGame(Request req, Response res) throws ResponseException, DataAccessException {
        var token = req.headers("authorization");
        var authorized = authService.checkAuth(token);
        if (authorized != null) {
            var game = new Gson().fromJson(req.body(),GameData.class);
            if (game == null){
                throw new ResponseException(400, "Error: bad request");
            }
            var responseMap = gameService.createGame(game.gameName());
            return new Gson().toJson(Map.of("gameID", responseMap));
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object joinGame(Request req, Response res) throws ResponseException, DataAccessException, SQLException {
        var auth = req.headers("authorization");
        var checkedAuth = authService.checkAuth(auth);
        if (checkedAuth != null) {
            var game = new Gson().fromJson(req.body(), JoinGameData.class); //player color and gameID
            if (game.playerColor() != null){
                if (game.playerColor().equals("BLACK") || game.playerColor().equals("WHITE") || game.playerColor().equals("WHITE/BLACK")){
                    gameService.joinGame(game, checkedAuth.username());
                    res.status(200);
                    return new Gson().toJson(Map.of());
                }
                throw new ResponseException(400,"Error: bad request");
            } else {
                throw new ResponseException(400, "Error: bad request");
            }
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object updateGame(Request req, Response res) throws ResponseException, DataAccessException {
        var game = req.params("gameID");
        int gameInt = Integer.parseInt(game);
        var color = new Gson().fromJson(req.body(), JoinGameData.class);
        return new Gson().toJson(gameService.updateGame(gameInt, color.playerColor()));
    }

    private Object getGame(Request req, Response res) throws ResponseException, DataAccessException, SQLException {
        var auth = req.headers("authorization");
        var checkedAuth = authService.checkAuth(auth);
        if (checkedAuth != null) {
            var game = req.params("gameID");
            int gameInt = Integer.parseInt(game);
            var specGame = gameService.getGame(gameInt);
            if (specGame == null) {
                throw new ResponseException(401, "Error: Invalid Game ID");
            }
            return new Gson().toJson(specGame);
        } else {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    private Object clear(Request req, Response res) throws DataAccessException, SQLException {
        userService.clear();
        gameService.clear();
        authService.clear();
        res.status(200);
        return new Gson().toJson(Map.of());
    }
}
