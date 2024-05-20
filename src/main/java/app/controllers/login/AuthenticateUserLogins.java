package app.controllers.login;

import app.models.MainModel;
import app.repositories.users.UsersData;

public class AuthenticateUserLogins extends MainModel {
    private static String username, password, role;
    private byte is_active;

    public AuthenticateUserLogins(String username, String password) {
        AuthenticateUserLogins.username = username;
        AuthenticateUserLogins.password = password;
    }

    public static boolean checkUserLogins(String username, String password) {
        boolean isValid = false;

        return isValid;
    }
}
