package server;

import com.google.gson.Gson;
import model.UserData;
import services.AuthService;
import services.GameService;
import services.UserService;
import spark.*;

public class Server {
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;

    public Server(AuthService auth, GameService game, UserService user){
        this.authService = auth;
        this.gameService = game;
        this.userService = user;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/userService", this::createUser);
        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object createUser(Request req, Response res) {
        var user = new Gson().fromJSON(req.body(), UserData.class);
        user = userService.createUser(user);
        return new Gson().toJSon(user);
    }
}
