package server;

import org.eclipse.jetty.server.Authentication;
import services.AuthService;
import services.GameService;
import services.UserService;
import spark.*;

public class Server {
    private final AuthService auth;
    private final GameService game;
    private final UserService user;

    public Server(AuthService auth, GameService game, UserService user){
        this.auth = auth;
        this.game = game;
        this.user = user;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
