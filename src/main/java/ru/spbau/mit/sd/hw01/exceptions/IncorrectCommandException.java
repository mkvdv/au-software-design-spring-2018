package ru.spbau.mit.sd.hw01.exceptions;

/**
 * Exception for catching errors in command parsing.
 */
public class IncorrectCommandException extends Exception {
    public IncorrectCommandException(String commandName) {
        super("#ERR: Incorrect command: " + commandName);
    }
}
