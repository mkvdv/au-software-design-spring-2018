package ru.spbau.mit.sd.hw01.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global program logger
 */
public class Log {
    private static Logger log = Logger.getAnonymousLogger();
    private static boolean isLogging = false;

    private Log() { // private constructor
    }

    /**
     * Write string as log with level INFO
     *
     * @param s
     */
    public static void info(String s) {
        if (isLogging) {
            log.log(Level.INFO, s);
        }
    }

    /**
     * Write string as log with level WARNING
     *
     * @param s
     */
    public static void warning(String s) {
        if (isLogging) {
            log.log(Level.WARNING, s);
        }
    }

    public static boolean isIsLogging() {
        return isLogging;
    }

    public static void setIsLogging(final boolean isLogging) {
        Log.isLogging = isLogging;
    }
}
