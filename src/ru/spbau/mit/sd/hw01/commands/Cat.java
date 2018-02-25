package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Cat extends AbstractCommand {
    public Cat(String[] args, Environment env) {
        super(args, env);
    }

    // в случае ошибки цепочка pipe-ов прерывается
    @Override
    public PipedInputStream exec(InputStream stdin) throws CommandExecuteException {
        assert (args.length == 1); // here just 1 file
        Log.info("cat with " + Arrays.toString(args));
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        Path path = Paths.get(args[0]);
        byte[] bytes;

        try {
            pis.connect(pos);
            bytes = Files.readAllBytes(path);
            pos.write(bytes);
            pos.flush();
            pos.close();
        } catch (IOException e) {
            throw new CommandExecuteException("cat " + args[0]);
        }
        return pis;
    }
}
