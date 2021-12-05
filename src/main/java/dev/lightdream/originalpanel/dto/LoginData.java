package dev.lightdream.originalpanel.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class LoginData {


    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDataAuth{
        public String username;
        public String password;

        public LoginDataResponse respond(String response){
            return new LoginDataResponse(username, password, response);
        }

        @Override
        public String toString() {
            return "LoginDataAuth{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class LoginDataResponse{
        public String username;
        public String password;
        public String response;

        @Override
        public String toString() {
            return "LoginDataResponse{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", response='" + response + '\'' +
                    '}';
        }
    }



}
