package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameData;
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
        Spark.post("/session", this::login);
        Spark.delete("/session",this::deleteAuth);
        Spark.get("/game",this::listGame);
        Spark.post("/game",this::createGame);
        Spark.put("/game",this::joinGame);
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
        authService.setAuth((AuthData) responseMap.get("authData"));
        return new Gson().toJson(responseMap);
    }

    private Object login(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(),UserData.class);
        var responseMap = userService.getAuth(user);
        authService.setAuth((AuthData) responseMap.get("authData"));
        return new Gson().toJson(responseMap);
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
            return new Gson().toJson(responseMap);
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        var token = req.headers("authorization");
        var authorized = authService.checkAuth(token);
        if (authorized != null) {
            var game = req.body();
            if (game == null){
                throw new ResponseException(400, "Error: bad request");
            }
            var responseMap = gameService.createGame(authorized, game);
            return new Gson().toJson(responseMap);
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
        var auth = req.headers("authorization");
        var checkedAuth = authService.checkAuth(auth);
        if (auth != null) {
            var game = new Gson().fromJson(req.body(), JoinGameData.class); //player color and gameID
            gameService.joinGame(game,checkedAuth.username());
            throw new ResponseException(200,null);
        }
        else {
            throw new ResponseException(401,"Error: unauthorized");
        }
    }

    private Object clear(Request req, Response res) throws ResponseException {
        userService.clear();
        gameService.clear();
        authService.clear();
        return true;
    }
}
