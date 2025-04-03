package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;
import java.util.HashSet;

public class ServerFacade {

    private final String serverUrl;


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

    public HashSet<GameData> listGames(String authToken) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, HashSet<GameData>,authToken);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, authToken, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http,responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, String authToken, HttpURLConnection http) throws IOException {
        http.addRequestProperty("Content-Type", "application/json");
        http.addRequestProperty("authorization", authToken);
        String reqData = new Gson().toJson(request);
        try (OutputStream reqBody = http.getOutputStream()) {
            reqBody.write(reqData.getBytes());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader,responseClass);
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
