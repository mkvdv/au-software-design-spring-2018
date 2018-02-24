package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class Pwd extends AbstractCommand {
    public Pwd(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public PipedInputStream exec(InputStream stdin) {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        try {
            pis.connect(pos);
            pos.write(env.getCurrentDir().getBytes());
            pos.write('\n');
            pos.flush();
            pos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return pis;
    }
}
