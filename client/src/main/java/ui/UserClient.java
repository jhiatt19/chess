package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import exception.ErrorResponse;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import server.ServerFacade;
import websocket.messages.ServerMessage;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.State.GAMEPLAY;
import static ui.State.SIGNEDOUT;

public class UserClient {
    private WebSocket ws;
    private String username = null;
    private String token = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = SIGNEDOUT;
    private Integer currGameID;
    private String color = "WHITE";
    private HashMap<String,Integer> alphaConversion = new HashMap<>();
    private GameData game;

    public UserClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        alphaConversion.put("a",1);
        alphaConversion.put("b",2);
        alphaConversion.put("c",3);
        alphaConversion.put("d",4);
        alphaConversion.put("e",5);
        alphaConversion.put("f",6);
        alphaConversion.put("g",7);
        alphaConversion.put("h",8);
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
                case "redraw" -> redrawBoard();
                case "leave" -> leave();
                case "move" -> makeMove(params);
                default -> help();
            };
        } catch (ResponseException | InvalidMoveException ex) {
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

    private String quit() {
        return "quit";
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
        if (params.length == 1) {
            this.game = new GameData(0, null, null, params[0], new ChessGame());
            var newGame = server.createGame(game, token);
            return String.format("Created game: " + game.gameName() + ", with ID number: " + newGame.gameID());
        }
        throw new ResponseException(400, "Expected: <gameName>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames(token);
        StringBuilder gamesList = new StringBuilder();
        for (GameData game : games){
            String g = "\nID: " + game.gameID() + " white: " + game.whiteUsername()
                    + " black: " + game.blackUsername() + " gameName: " + game.gameName() + "\n";
            gamesList.append(g);
        }
        return gamesList.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 2) {
            this.game = server.joinGame(token, params[1].toUpperCase(), Integer.parseInt(params[0]));
            if (game != null){
                //ws = new WebSocket()
                ChessBoard.main(params,game.game());
                currGameID = Integer.parseInt(params[0]);
                color = params[1].toUpperCase();
                state = GAMEPLAY;
                return "Join game " + params[1];
            }
        }
        throw new ResponseException(400, "Expected: <GameID> [BLACK|WHITE]");
    }

    public String observe(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            var game = server.observe(token,params[0]);
            ChessBoard.main(params,game.game());
            currGameID = Integer.parseInt(params[0]);
            return "Watching game " + params[0];
        }
        throw new ResponseException(400, "Expected: <GameID>");
    }

    public String redrawBoard() throws ResponseException {
        assertSignedIn();
        this.game = server.observe(token,currGameID.toString());
        String[] params = new String[]{currGameID.toString(),color};
        ChessBoard.main(params,game.game());
        return "";
    }

    public String leave() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDIN;
        server.update(token,currGameID.toString(),color,this.game);
        return "";
    }

    public String makeMove(String...params) throws ResponseException, InvalidMoveException {
        assertSignedIn();
        if (params.length == 2 && getState().equals(GAMEPLAY)){
            String[] args = new String[]{currGameID.toString(),color};
            var startPosRaw = params[0].toCharArray();
            var endPosRaw = params[1].toCharArray();
            String str = String.valueOf(startPosRaw[0]);
            String str1 = String.valueOf(endPosRaw[0]);
            System.out.println(str + str1);
            int startPosInt = alphaConversion.get(str);
            int endPosInt = alphaConversion.get(str1);
            System.out.println(startPosInt + " " + endPosInt);
            ChessPosition startPos = new ChessPosition(Integer.parseInt(String.valueOf(startPosRaw[1])),startPosInt);
            ChessPosition endPos = new ChessPosition(Integer.parseInt(String.valueOf(endPosRaw[1])),endPosInt);
            ChessMove userMove = new ChessMove(startPos,endPos,null);
            game.game().makeMove(userMove);
            server.update(token,currGameID.toString(),"MOVE",game);
            ChessBoard.main(args,game.game());
            return "";
        } else {
            return "Please enter a game. Expected: <Start Position> <End Position>";
        }
    }

    public String help() {
        if (state == SIGNEDOUT){
            return """
                    - login <username> <password>
                    - register <username> <password> <email>
                    - help
                    - quit
                    """;
        } else if (state == GAMEPLAY){
            return """
                    For commands in this section just type the first word.
                    - move <chessPiece coordinate start> <chessPiece coordinate end>
                    - highlight moves <chessPiece coordinate>
                    - redraw board
                    - resign
                    - leave
                    - help
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
