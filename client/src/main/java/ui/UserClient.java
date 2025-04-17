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
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static chess.ChessGame.TeamColor.WHITE;
import static ui.State.GAMEPLAY;
import static ui.State.SIGNEDOUT;

public class UserClient {
    private WebSocketFacade ws;
    private String username = null;
    private String token = null;
    private final ServerFacade server;
    private final String serverUrl;
    private State state = SIGNEDOUT;
    private Integer currGameID;
    private String color = "WHITE";
    private GameData game;
    HashMap<String, Integer> alphaConversionWhite = new HashMap<>();
    HashMap<String, Integer> alphaConversionBlack = new HashMap<>();
    private final NotificationHandler notificationHandler;

    public UserClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = null;

        alphaConversionWhite.put("a",1);
        alphaConversionWhite.put("b",2);
        alphaConversionWhite.put("c",3);
        alphaConversionWhite.put("d",4);
        alphaConversionWhite.put("e",5);
        alphaConversionWhite.put("f",6);
        alphaConversionWhite.put("g",7);
        alphaConversionWhite.put("h",8);

        alphaConversionBlack.put("h",1);
        alphaConversionBlack.put("g",2);
        alphaConversionBlack.put("f",3);
        alphaConversionBlack.put("e",4);
        alphaConversionBlack.put("d",5);
        alphaConversionBlack.put("c",6);
        alphaConversionBlack.put("b",7);
        alphaConversionBlack.put("a",8);
    }
    public UserClient(String serverUrl, NotificationHandler notificationHandler){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;

        alphaConversionWhite.put("a",1);
        alphaConversionWhite.put("b",2);
        alphaConversionWhite.put("c",3);
        alphaConversionWhite.put("d",4);
        alphaConversionWhite.put("e",5);
        alphaConversionWhite.put("f",6);
        alphaConversionWhite.put("g",7);
        alphaConversionWhite.put("h",8);

        alphaConversionBlack.put("h",1);
        alphaConversionBlack.put("g",2);
        alphaConversionBlack.put("f",3);
        alphaConversionBlack.put("e",4);
        alphaConversionBlack.put("d",5);
        alphaConversionBlack.put("c",6);
        alphaConversionBlack.put("b",7);
        alphaConversionBlack.put("a",8);
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
                case "resign" -> resign();
                case "highlight" -> highlightMoves(params);
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
            String g;
            if (game.game().getGameCompleted()){
                g = "\nID: " + game.gameID() + ", gameName: " + game.gameName() + " has been completed.\n";
            } else {
                g = "\nID: " + game.gameID() + " white: " + game.whiteUsername()
                        + " black: " + game.blackUsername() + " gameName: " + game.gameName() + "\n";
            }
            gamesList.append(g);
        }
        return gamesList.toString();
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        if (params.length == 1) {
            this.currGameID = Integer.parseInt(params[0]);
            this.game = server.observe(token, currGameID.toString());
            if (game.whiteUsername().equals(username)) {
                this.color = "WHITE";
                state = GAMEPLAY;
                String[] args = new String[]{currGameID.toString(), color};
                ChessBoard.main(args, game.game());
                ws.connect(token,currGameID);
                return "";
            } else if (game.blackUsername().equals(username)) {
                this.color = "BLACK";
                state = GAMEPLAY;
                String[] args = new String[]{currGameID.toString(), color};
                ChessBoard.main(args, game.game());
                ws.connect(token,currGameID);
                return "";
            } else {
                return "Please choose color or find an open game.";
            }
        }
        else if (params.length == 2) {
            this.game = server.joinGame(token, params[1].toUpperCase(), Integer.parseInt(params[0]));
            if (game != null){
                ChessBoard.main(params,game.game());
                currGameID = Integer.parseInt(params[0]);
                ws.connect(token,currGameID);
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
        server.update(token,color,this.game);
        ws.leave(token,currGameID);
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
            int startPosInt;
            int endPosInt;
            if (color.equals("WHITE")) {
                startPosInt = alphaConversionWhite.get(str);
                endPosInt = alphaConversionWhite.get(str1);
            } else {
                startPosInt = alphaConversionBlack.get(str);
                endPosInt = alphaConversionBlack.get(str1);
            }
            ChessPosition startPos = new ChessPosition(Integer.parseInt(String.valueOf(startPosRaw[1])),startPosInt);
            ChessPosition endPos = new ChessPosition(Integer.parseInt(String.valueOf(endPosRaw[1])),endPosInt);
            ChessMove userMove = new ChessMove(startPos,endPos,null);
            ws.makeMove(token,currGameID,userMove);
            server.update(token,"MOVE",game);
            ChessBoard.main(args,game.game());
            return "";
        } else {
            return "Please enter a game. Expected: <Start Position> <End Position>";
        }
    }

    public String resign() throws ResponseException {
        assertSignedIn();
        assertInGame();
        server.update(token, "RESIGN", game);
        return "";
    }

    public String highlightMoves(String...params) throws ResponseException {
        assertSignedIn();
        assertInGame();
        String[] args = new String[]{currGameID.toString(),color,"HIGHLIGHT"};
        var startPosRaw = params[0].toCharArray();
        String str = String.valueOf(startPosRaw[0]);
        int startPosInt;
        if (color.equals("WHITE")) {
            startPosInt = alphaConversionWhite.get(str);
        } else {
            startPosInt = alphaConversionBlack.get(str);
        }
        var currPos = new ChessPosition(Integer.parseInt(String.valueOf(startPosRaw[1])), startPosInt);
        game.game().validMoves(currPos);
        ChessBoard.main(args,game.game());
        return "";
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

    private void assertInGame() throws ResponseException {
        if (state != GAMEPLAY){
            throw new ResponseException(400, "You must enter a game.");
        }
    }
}
