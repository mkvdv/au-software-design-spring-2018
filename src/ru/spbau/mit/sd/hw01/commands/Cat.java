package ru.spbau.mit.sd.hw01.commands;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Cat implements ICommand {
    private String filepath;

    public Cat(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public void exec(String[] input) {
        Path path = Paths.get(filepath);
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
