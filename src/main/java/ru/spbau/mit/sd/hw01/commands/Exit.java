package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.InputStream;
import java.io.PipedInputStream;

public class Exit extends AbstractCommand {
    public Exit(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public PipedInputStream exec(InputStream stdin) {
        System.exit(0); // dirty
        return null;
    }
}
