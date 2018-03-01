package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Class for execution commands like: pwd
 */
public class Pwd extends AbstractCommand {
    public Pwd(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Get absolute path to current dir.
     *
     * @param stdin is input stream for command
     * @return stream with result of execution in it.
     */
    @Override
    public InputStream exec(InputStream stdin) {
        return new ByteArrayInputStream((env.getCurrentDir() + '\n').getBytes());
    }
}
