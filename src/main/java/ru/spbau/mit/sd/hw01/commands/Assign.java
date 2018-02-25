package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.Arrays;

public class Assign extends AbstractCommand {


    public Assign(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public PipedInputStream exec(InputStream stdin) {
        Log.info("assign with " + Arrays.toString(args));
        assert (args.length == 2);

        env.set(args[0], args[1]);
        return null;
    }


}
