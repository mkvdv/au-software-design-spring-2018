package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.InputStream;
import java.io.PipedInputStream;

public class Eq extends AbstractCommand {
    Eq(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public PipedInputStream exec(InputStream stdin) {
        assert (args.length == 2);

        env.set(args[0], args[1]);
        return null;
    }
}
