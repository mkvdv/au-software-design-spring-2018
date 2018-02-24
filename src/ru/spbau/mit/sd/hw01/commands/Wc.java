package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
        Log.info("wc with " + Arrays.toString(args));

        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        int nl = 0;
        int n_bytes = 0;
        int n_words = 0;

        if (args.length > 0) {
            // read from file
            assert (args.length == 1);

            Path path = Paths.get(args[0]);
            byte[] bytes;
            try {
                bytes = Files.readAllBytes(path);
                n_bytes = bytes.length;
                String s = new String(bytes);
                n_words = countWords(s);
                nl = countNL(s);

            } catch (IOException e) {
                System.err.println("ERROR: Incorrect file path");
                return null;
            }
        } else {
            // read from stdin arg
            int len = 0;
            final int size = 1024;
            byte[] buf = new byte[size];

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                while ((len = stdin.read(buf, 0, size)) != -1)
                    bos.write(buf, 0, len);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            buf = bos.toByteArray();

            n_bytes = buf.length;
            String s = new String(buf);
            n_words = countWords(s);
            nl = countNL(s);
        }


        try {
            pis.connect(pos);
            pos.write((nl + "\t" + n_words + '\t' + n_bytes + '\n').getBytes());
            pos.flush();
            pos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return pis;
    }

    private int countNL(String s) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\n') {
                counter++;
            }
        }
        return counter;
    }

    private int countWords(String line) {
        if (line == null || line.isEmpty()) {
            return 0;
        }
        return line.split("\\s+|\\n").length;
    }


}
