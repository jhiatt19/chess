package ui;

import exception.ResponseException;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

public class UserClient {

    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public UserClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            UserData user = new UserData(params[0],params[1],params[2]);
            var auth = server.createUser(user);
            state = State.SIGNEDIN;
            return String.format("You successfully registered as %s.", username);
        }
        throw new ResponseException(400, "Expected: <username>");
    }

    public String help() {
        if (state == State.SIGNEDOUT){
            return """
                    - login
                    - register
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
