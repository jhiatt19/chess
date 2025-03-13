package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import services.AuthService;
import services.GameService;
import services.UserService;
import spark.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public Server()  {
//        try {
            this.gameDAO = new GameMemoryAccess();
            this.authDAO = new AuthMemoryAccess();
            this.userDAO = new UserSqlDataAccess();
            this.authService = new AuthService(authDAO);
            this.gameService = new GameService(gameDAO);
            this.userService = new UserService(userDAO);
//        }
//        catch (DataAccessException ex) {
//            throw new DataAccessException(String.format("Problem creating server connection",ex.getMessage()));
//        }

    }

    public int run(int desiredPort){
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::login);
        Spark.delete("/session",this::deleteAuth);
        Spark.get("/game",this::listGame);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);
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
        authService.setAuth(authUser);
        res.status(200);
        res.type("application/json");
        return new Gson().toJson(authUser);
    }

    private Object deleteAuth(Request req, Response res) throws ResponseException{
        var token = req.headers("authorization");
        var responseMap = authService.deleteAuth(token);
        return new Gson().toJson(responseMap);
    }

    private Object listGame(Request req, Response res) throws ResponseException {
        var token = req.headers("authorization");
        if (authService.checkAuth(token) != null){
            var responseMap = gameService.listGame();
            for (GameData game : responseMap){
                System.out.println(game);
            }
            return new Gson().toJson(Map.of("games",responseMap));
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        var token = req.headers("authorization");
        var authorized = authService.checkAuth(token);
        if (authorized != null) {
            var game = new Gson().fromJson(req.body(),GameData.class);
            //System.out.println(game);
            if (game == null){
                throw new ResponseException(400, "Error: bad request");
            }
            var responseMap = gameService.createGame(game.gameName());
            //System.out.println(gameService.getGame(responseMap));
            return new Gson().toJson(Map.of("gameID", responseMap));
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
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

    private Object clear(Request req, Response res) throws DataAccessException, SQLException {
        userService.clear();
        gameService.clear();
        authService.clear();
        return "{}";
    }
}
