package model;

public class DataTransformation {
    public static AuthData transform(UserData userData, String authToken) {
        return new AuthData(authToken, userData.username());
    };


}
