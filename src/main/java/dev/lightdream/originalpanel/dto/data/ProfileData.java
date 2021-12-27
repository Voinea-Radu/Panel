package dev.lightdream.originalpanel.dto.data;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ProfileData {

    public String cookie;

    @SuppressWarnings("unused")
    public String extractUsername() {
        return new Gson().fromJson(this.cookie, LoginData.class).username;
    }

}
