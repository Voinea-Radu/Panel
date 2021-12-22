package dev.lightdream.originalpanel.utils;

import dev.lightdream.originalpanel.Main;
import org.fusesource.jansi.Ansi;

public class Logger {

    public static boolean enabled = false;
    private static Main main;

    @SuppressWarnings("unused")
    public static void info(Object object) {
        if (object == null) {
            info("null");
            return;
        }
        info(object.toString());
    }

    public static void info(String message) {
        if (main == null) {
            System.out.println(Ansi.ansi().fg(Ansi.Color.RED).boldOff() + "The logger has not been initialized."
                    + Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff());
            return;
        }
        main.log(message);
    }

    public static void error(Object object) {
        if (object == null) {
            error("null");
            return;
        }
        error(object.toString());
    }

    public static void error(String message) {
        if (main == null) {
            info(message);
            return;
        }
        info(Ansi.ansi().fg(Ansi.Color.RED).boldOff() + message +
                Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff());
    }

    public static void good(Object object) {
        if (object == null) {
            error("null");
            return;
        }
        error(object.toString());
    }

    public static void good(String message) {
        if (main == null) {
            info(message);
            return;
        }
        info(Ansi.ansi().fg(Ansi.Color.GREEN).boldOff() + message +
                Ansi.ansi().fg(Ansi.Color.DEFAULT).boldOff());
    }

    public static void init(Main main) {
        Logger.main = main;
        Logger.enabled = true;
    }

}
