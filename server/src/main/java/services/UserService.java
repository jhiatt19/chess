package services;

import dataaccess.UserDAO;
import model.UserData;

public class UserService {

    private final UserDAO userData;

    public UserService(UserDAO userData){
        this.userData = userData;
    }

    public UserData createUser(UserData user) {
        if (user.username)
    }
}
