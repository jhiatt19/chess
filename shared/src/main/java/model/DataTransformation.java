package model;

public class DataTransformation {
    public static AuthData transform(UserData userData, String authToken) {
        return new AuthData(authToken, userData.username());
    };

    public static AuthUser transformToAuthUser(UserData userData, String authToken){
        return new AuthUser(authToken, userData.username());
    };

}
