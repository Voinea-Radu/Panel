package dev.lightdream.originalpanel.utils;


import dev.lightdream.originalpanel.Main;

public class Debugger {

    public static boolean enabled = false;
    private static Main main;

    public static void info(Object object) {
        if (object == null) {
            info("null");
            return;
        }
        info(object.toString());
    }

    public static void info(String message) {
        if (main == null) {
            System.out.println("The debugger has not been initialized.");
            return;
        }
        if (!main.debug()) {
            return;
        }
        main.log(message);
    }

    public static void init(Main main) {
        Debugger.main = main;
        Debugger.enabled = true;
    }

}
