package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class UserClient {

    private String username = null;
    private String token = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public UserClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public State getState(){
        return state;
    }
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "quit" -> quit();
                case "login" -> login(params);
                case "logout" -> logout();
                case "clear" -> clear();
                case "creategame" -> createGame(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            UserData user = new UserData(params[0],params[1],params[2]);
            var auth = server.createUser(user);
            state = State.SIGNEDIN;
            username = auth.username();
            token = auth.authToken();
            return String.format("You successfully registered as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            UserData user = new UserData(params[0],params[1],null);
            var auth = server.loginUser(user);
            state = State.SIGNEDIN;
            username = auth.username();
            token = auth.authToken();
            return String.format("Welcome %s", username);
        }
        throw new ResponseException(400,"Expected: <username> <password>");
    }

    public String quit() {
        return String.format("quit");
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logoutUser(token);
        state = State.SIGNEDOUT;
        username = null;
        token = null;
        return "Goodbye!";

    }

    public String clear() throws ResponseException{
        assertSignedIn();
        server.clear();
        state = State.SIGNEDOUT;
        username = null;
        token = null;
        return "Database cleared";
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        var game = new GameData(0,null,null,params[0],new ChessGame());
        var newGame = server.createGame(game,token);
        return String.format("Created game: " + game.gameName() + ", with ID number: " + newGame.gameID());
    }

    public String help() {
        if (state == State.SIGNEDOUT){
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - help
                    - quit
                    """;
        }
        return """
                - help
                - logout
                - createGame
                - listGames
                - playGame
                - watchGame
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

}
