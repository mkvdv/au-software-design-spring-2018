package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Cat extends AbstractCommand {
    Cat(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public void exec(final String[] input) {
        assert (args.length == 1); // here just 1 file

        Path path = Paths.get(args[0]);
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String element : lines) {
                System.out.println(element);
            }
        } catch (IOException e) {
            System.out.println("ERROR: Incorrect file path");
        }
    }
}
