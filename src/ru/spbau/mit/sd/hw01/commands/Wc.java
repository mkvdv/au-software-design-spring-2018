package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Wc extends AbstractCommand {
    public Wc(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Like in gnu coreutils - ignore stdin, if has args
     *
     * @param stdin is like stdin for wc application in unix
     */
    @Override
    public PipedInputStream exec(InputStream stdin) {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        int nl = 0;
        int bytes = 0;
        int words = 0;

        if (args.length > 0) {
            // read from file
            assert (args.length == 1);

            Path path = Paths.get(args[0]);
            List<String> lines;
            try {
                lines = Files.readAllLines(path);
                for (String line : lines) {
                    bytes += line.getBytes().length;
                    words += countWords(line);
                }
                nl = Math.max(0, lines.size() - 1);
            } catch (IOException e) {
                System.err.println("ERROR: Incorrect file path");
                return null;
            }
        } else {
            // read from stdin arg
            Scanner sc = new Scanner(stdin);
            while (sc.hasNext()) {
                String line = sc.nextLine();
                bytes += line.getBytes().length;
                words += countWords(line);
                nl++;
            }
            nl = Math.max(0, nl - 1);
        }

        try {
            pis.connect(pos);
            pos.write((nl + "\t" + words + '\t' + bytes + '\n').getBytes());
            pos.flush();
            pos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return pis;
    }

    private int countWords(String line) {
        if (line == null || line.isEmpty()) {
            return 0;
        }
        return line.split("\\s+").length;
    }


}
