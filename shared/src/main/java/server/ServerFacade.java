package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;
import java.util.*;

public class ServerFacade {

    private final String serverUrl;
    List<GameData> gameData = new ArrayList<>();
    HashMap<String,Object> request = new HashMap<>();


    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthUser createUser(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthUser.class,null);
    }

    public AuthUser loginUser(UserData user) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthUser.class,null);
    }

    public void logoutUser(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, AuthData.class, authToken);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, AuthUser.class, null);
    }

    public GameData createGame(GameData game, String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, game, GameData.class, authToken);
    }

    public List<GameData> listGames(String authToken) throws ResponseException {
        var path = "/game";
        var body = this.makeRequest("GET", path, null, ArrayList.class,authToken);
        for (Object x : body){
            GameData y = new Gson().fromJson((String) x,GameData.class);
            gameData.add(y);
        }
        List<GameData> sendToConsole = new ArrayList<>(gameData);
        gameData.clear();
        return sendToConsole;
    }

    public GameData joinGame(String authToken, String color, int gameID) throws ResponseException {
        var path = "/game";
        request.put("playerColor", color);
        request.put("gameID", gameID);
        this.makeRequest("PUT",path,request, Map.class,authToken);
        path = "/game/" + gameID;
        var game = this.makeRequest("GET",path,null, GameData.class,authToken);
        request.clear();
        return game;
    }

    public GameData observe(String authToken, String gameID) throws ResponseException {
        var path = "/game/" + gameID;
        return this.makeRequest("GET",path,null,GameData.class,authToken);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            //System.out.print(url + "\n");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            http.addRequestProperty("Content-Type", "application/json");
            http.addRequestProperty("authorization", authToken);
            http.addRequestProperty("test","false");

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http,responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
