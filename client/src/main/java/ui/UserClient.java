package ui;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.State.SIGNEDOUT;

public class UserClient {

    private String username = null;
    private String token = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = SIGNEDOUT;

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
                case "listgames" -> listGames();
                case "playgame" -> joinGame(params);
                case "watchgame" -> observe(params);
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
        state = SIGNEDOUT;
        username = null;
        token = null;
        return "Goodbye!";

    }

    public String clear() throws ResponseException{
        assertSignedIn();
        server.clear();
        state = SIGNEDOUT;
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

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(token);
        StringBuffer gamesList = new StringBuffer();
        for (GameData game : games){
            String g = "\nID: " + game.gameID() + " white: " + game.whiteUsername() + " black: " + game.blackUsername() + " gameName: " + game.gameName() + "\n";
            gamesList.append(g);
        }
        return gamesList.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            var check = server.joinGame(token, params[1].toUpperCase(), Integer.parseInt(params[0]));
            if (check.equals("Joining game")){
                    ChessBoard.main(params);
                    return check + " as " + params[1];
            }
        }
        throw new ResponseException(400, "Expected: <GameID> [BLACK|WHITE]");
    }

    public String observe(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var game = server.observe(token,params[0]);
            ChessBoard.main(params);
            return game + " " +  params[0];
        }
        throw new ResponseException(400, "Expected: <GameID>");
    }

    public String help() {
        if (state == SIGNEDOUT){
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
                - createGame <GameName>
                - listGames
                - playGame <GameID> <BLACK/WHITE>
                - watchGame <GameID>
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

}
