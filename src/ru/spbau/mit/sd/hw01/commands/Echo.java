package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

public class Echo extends AbstractCommand {

    public Echo(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public PipedInputStream exec(InputStream stdin) {
        Log.info("echo with " + Arrays.toString(args));
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        try {
            pis.connect(pos);

            for (String s : args) {
                pos.write(s.getBytes());
                pos.write(" ".getBytes());
            }
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
