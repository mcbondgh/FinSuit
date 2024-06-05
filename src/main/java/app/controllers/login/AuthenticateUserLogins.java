package app.controllers.login;

import app.config.encryptDecryp.EncryptDecrypt;
import app.models.MainModel;
import app.repositories.users.UsersData;

import java.util.HashMap;
import java.util.Map;

public class AuthenticateUserLogins extends MainModel {
    private String username, password, role;
    private byte is_active;
    private UsersData usersData = new UsersData();
    private Map<String, Object> data = new HashMap<>();
    public AuthenticateUserLogins(String username, String password) {
        this.username = username;
        this.password = password;
    }

    private boolean checkPassword() {
       boolean status;
       data = fetchUserDataByUserName(username);
       status = EncryptDecrypt.verifyHashValue(password, data.get("password").toString());
       return status;
    }

    public UsersData getDetails() {
        if(checkPassword()) {
            usersData.setUsername(data.get("username").toString());
            usersData.setRole(data.get("role").toString());
        }
        return usersData;
    }
}
