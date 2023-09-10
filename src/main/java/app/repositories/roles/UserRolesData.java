package app.repositories.roles;

public class UserRolesData {
    int role_id;
    String role_name;

    public UserRolesData(int role_id, String role_name) {
        this.role_id = role_id;
        this.role_name = role_name;
    }

    public int getRole_id() {
        return role_id;
    }

    public String getRole_name() {
        return role_name;
    }
}
