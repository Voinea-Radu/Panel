package dev.lightdream.originalpanel.dto.data;

public class LoginData {

    public String username;
    public String password;

    @Override
    public String toString() {
        return "LoginData{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
