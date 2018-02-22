package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.InputStream;
import java.io.PipedInputStream;

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
     */
    public abstract PipedInputStream exec(InputStream stdin);
}
