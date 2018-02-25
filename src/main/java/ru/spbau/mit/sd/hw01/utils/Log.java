package ru.spbau.mit.sd.hw01.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global programm logger
 */
public class Log {
    private static Logger log = Logger.getAnonymousLogger();
    private static boolean isLoging = false;

    private Log() { // private constructor
    }

    public static void info(String s) {
        if (isLoging)
            log.log(Level.INFO, s);
    }

    public static void warning(String s) {
        if (isLoging)
            log.log(Level.WARNING, s);
    }

    public static boolean isIsLoging() {
        return isLoging;
    }

    public static void setIsLoging(boolean isLoging) {
        Log.isLoging = isLoging;
    }
}
