package ru.spbau.mit.sd.hw01.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {
    private static Logger log = Logger.getAnonymousLogger();

    private Log() { // private constructor
    }

    public static void info(String s) {
        log.log(Level.INFO, s);
    }

    public static void warning(String s) {
        log.log(Level.WARNING, s);
    }
}
