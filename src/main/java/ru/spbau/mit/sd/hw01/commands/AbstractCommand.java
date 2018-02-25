package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;

import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Base abstract class for all command classes.
 */
public abstract class AbstractCommand {
    protected String[] args;
    protected Environment env;

    public AbstractCommand(String[] args, Environment env) {
        this.args = args;
        this.env = env;
    }


    /**
     * Do some work - depends on command implementation
     *
     * @param stdin is input stream of command (often Piped Stream)
     * @return input stream for next command (result)
     * @throws CommandExecuteException if something goes wrong
     */
    public abstract PipedInputStream exec(InputStream stdin) throws CommandExecuteException;
}
