package dev.lightdream.originalpanel.utils;

import com.google.gson.Gson;
import dev.lightdream.originalpanel.dto.data.LoginData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String millisecondsToDate(Long milliseconds) {
        DateFormat simple = new SimpleDateFormat("dd MMM yyyy");

        Date result = new Date(milliseconds);

        return simple.format(result);
    }

    public static String millisecondsToHours(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return hours + "h " + minutes + "m " + seconds + "s";
    }

    public static String getUsernameFromCookie(String cookie) {
        LoginData loginData;

        try {
            loginData = new Gson().fromJson(cookie, LoginData.class);
        } catch (Throwable t) {
            return "";
        }
        return loginData.username;
    }

}
