package ru.spbau.mit.sd.hw01.exceptions;

public class IncorrectCommandException extends Exception {
    public IncorrectCommandException(String commandName) {
        super("#ERR: Incorrect command: " + commandName);
    }
}
