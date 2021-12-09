package dev.lightdream.originalpanel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.Gson;
import dev.lightdream.originalpanel.dto.data.ComplainData;
import dev.lightdream.originalpanel.dto.data.LoginData;
import dev.lightdream.originalpanel.utils.Debugger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController("")
public class RestEndPoints {

    public RestEndPoints(){

    }

    @PostMapping("/api/login/v2")
    public @ResponseBody
    LoginData.LoginDataResponse login(@RequestBody String dataStream) {
        LoginData.LoginDataAuth data;

        try {
            data = new Gson().fromJson(dataStream, LoginData.LoginDataAuth.class);
        } catch (Throwable t) {
            return new LoginData.LoginDataResponse("", "", "401 Bad Credentials");
        }

        if (data == null) {
            return new LoginData.LoginDataResponse("", "", "401 Bad Credentials");
        }

        if (!checkPassword(data)) {
            return new LoginData.LoginDataResponse("", "", "401 Bad Credentials");
        }

        data.password = Main.instance.databaseManager.getAuthMePassword(data.username);

        return data.respond("200 OK");
    }

    @PostMapping("/api/login/validate")
    public @ResponseBody
    LoginData.LoginDataResponse validateCookie(@RequestBody String dataStream) {
        Debugger.info(dataStream);
        LoginData.LoginDataAuth data;
        try {
            data = new Gson().fromJson(dataStream, LoginData.LoginDataAuth.class);
        } catch (Throwable t) {
            Debugger.info(1);
            return new LoginData.LoginDataResponse("", "", "401 Bad Credentials");
        }

        Debugger.info(data);

        if (data == null) {
            Debugger.info(2);
            return new LoginData.LoginDataResponse("", "", "401 Bad Credentials");
        }

        if (!data.password.equals(Main.instance.databaseManager.getAuthMePassword(data.username))) {
            Debugger.info(3);
            return new LoginData.LoginDataResponse("", "", "401 Bad Credentials");
        }

        return data.respond("200 OK");
    }

    @PostMapping("/api/form/complain")
    public @ResponseBody
    ComplainData.ComplainDataResponse complain(@RequestBody ComplainData.ComplainDataRequest data) {

        Debugger.info("Received complain");

        if (!validateCookie(data.cookie).response.equals("200 OK")) {
            return ComplainData.ComplainDataResponse.error("401 Ban Credentials");
        }

        if (!Main.instance.databaseManager.validateUser(data.target)) {
            return ComplainData.ComplainDataResponse.error("422 Invalid entry");
        }

        data.status = ComplainData.ComplainStatus.OPENED_AWAITING_TARGET_RESPONSE;
        data.targetResponse = "";
        data.timestamp = System.currentTimeMillis();

        Main.instance.databaseManager.saveComplain(data);
        return ComplainData.ComplainDataResponse.error("200 OK");
    }


    public boolean checkPassword(String username, String password) {
        return BCrypt.verifyer().verify(
                password.getBytes(StandardCharsets.UTF_8),
                Main.instance.databaseManager.getAuthMePassword(username).getBytes(StandardCharsets.UTF_8)
        ).verified;
    }

    public boolean checkPassword(LoginData.LoginDataAuth data) {
        return checkPassword(data.username, data.password);
    }


}
