package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.InputStream;

/**
 * Class for execution commands like: exit
 * Command will kill the whole shell.
 */
public class Exit extends AbstractCommand {
    public Exit(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Shut down shell and the whole program
     *
     * @param stdin ignored
     * @return not return
     */
    @Override
    public InputStream exec(InputStream stdin) {
        System.out.println("Shell closed!");
        System.exit(0); // dirty
        return null;
    }
}
