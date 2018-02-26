package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.InputStream;
import java.util.Arrays;

/**
 * Class for execution commands like FILE=test.txt
 */
public class Assign extends AbstractCommand {
    public Assign(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Save new key-value pair in env.
     *
     * @param stdin ignored
     * @return null
     */
    @Override
    public InputStream exec(InputStream stdin) {
        Log.info("assign with " + Arrays.toString(args));
        assert (args.length == 2);

        env.set(args[0], args[1]);
        return null;
    }
}
