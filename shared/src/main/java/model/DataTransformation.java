package model;

public class DataTransformation {
    public static AuthUser transform(UserData userData, String authToken){
        return new AuthUser(authToken, userData.username());
    }

}
