package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import services.AuthService;
import services.GameService;
import services.UserService;
import spark.*;

import java.util.HashMap;
import java.util.UUID;

public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;

    public Server(){
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();

    }
    public Server(AuthService auth, GameService game, UserService user){
        this.authService = auth;
        this.gameService = game;
        this.userService = user;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::createUser);
        Spark.post("/session", this::getAuth);
        Spark.delete("/session",this::deleteAuth);
        Spark.get("/game",this::listGame);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::updateGame);
        Spark.delete("/db",this::clear);
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object createUser(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserData.class);
        var responseMap = userService.createUser(user);
        return new Gson().toJson(responseMap);
    }

    private Object getAuth(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(),UserData.class);
        var responseMap = userService.getAuth(user);
        return new Gson().toJson(responseMap);
    }

    private Object deleteAuth(Request req, Response res) throws ResponseException{
        var auth = new Gson().fromJson(req.body(), AuthData.class);
        var responseMap = authService.deleteAuth(auth);
        return new Gson().toJson(responseMap);
    }

    private Object listGame(Request req, Response res) throws ResponseException {
        var game = new Gson().fromJson(req.body(), GameData.class);
        var responseMap = gameService.listGame(game);
        return new Gson().toJson(responseMap);
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        var game = new Gson().fromJson(req.body(),GameData.class);
        var responseMap = gameService.createGame(game);
        return new Gson().toJson(responseMap);
    }

    private Object updateGame(Request req, Response res) throws ResponseException {
        var game = new Gson().fromJson(req.body(),GameData.class);
        var responseMap = gameService.updateGame(game);
        return new Gson().toJson(responseMap);
    }

    private Object clear(Request req, Response res) throws ResponseException {
        var user = userService.clear();
        var game = gameService.clear();
        var auth = authService.clear();
    }
}
