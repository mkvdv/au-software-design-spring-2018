package ru.spbau.mit.sd.hw01.exceptions;

public class CommandExecuteException extends Exception {
    public CommandExecuteException(String s) {
        super("#ERR: Error during command execution: " + s);
    }
}
