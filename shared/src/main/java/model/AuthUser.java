package model;

public record AuthUser(String authToken, String username) {
    public AuthUser(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthtoken(){
        return authToken;
    }

    public String getUsername(){
        return username;
    }
}
