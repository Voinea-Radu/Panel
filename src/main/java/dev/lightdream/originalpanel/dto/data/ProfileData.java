package dev.lightdream.originalpanel.dto.data;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ProfileData {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileDataRequest{
        public String cookie;

        public ProfileDataResponse respond(String response) {
            return new ProfileDataResponse(cookie, response);
        }

        public String extractUsername(){
            return new Gson().fromJson(this.cookie,LoginData.LoginDataAuth.class).username;
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileDataResponse{
        public String cookie;
        public String response;

    }


}
