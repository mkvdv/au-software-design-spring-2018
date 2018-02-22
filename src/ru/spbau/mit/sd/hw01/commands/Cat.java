package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Cat extends AbstractCommand {
    public Cat(String[] args, Environment env) {
        super(args, env);
    }

    // в случае ошибки цепочка pipe-ов прерывается
    @Override
    public PipedInputStream exec(InputStream stdin) {
        assert (args.length == 1); // here just 1 file
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();
        Path path = Paths.get(args[0]);
        List<String> lines;

        try {
            pis.connect(pos);
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String element : lines) {
                pos.write(element.getBytes());
            }
            pos.flush();
            pos.close();
        } catch (IOException e) {
            System.out.println("ERROR: Incorrect file path");
            return null;
        }
        return pis;
    }
}
