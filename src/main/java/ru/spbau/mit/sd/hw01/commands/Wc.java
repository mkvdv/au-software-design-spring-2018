package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Class for execution commands like: wc file1
 * Or wc from stdin
 */
public class Wc extends AbstractCommand {
    public Wc(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Do stuff like wc, if there any args - do it from file.
     *
     * @param stdin is like stdin for wc application in unix
     * @return stream with result
     * @throws CommandExecuteException
     */
    @Override
    public InputStream exec(InputStream stdin) throws CommandExecuteException {
        Log.info("wc with " + Arrays.toString(args));

        ByteArrayInputStream bs = null;

        int nl = 0;
        int nBytes = 0;
        int nWords = 0;

        if (args.length > 0) {
            // read from file
            assert (args.length == 1);

            Path path = Paths.get(args[0]);
            byte[] bytes;
            try {
                bytes = Files.readAllBytes(path);
                nBytes = bytes.length;
                String s = new String(bytes);
                nWords = countWords(s);
                nl = countNL(s);

            } catch (java.nio.file.NoSuchFileException e) {
                throw new CommandExecuteException("wc " + args[0] + "; no such file");
            } catch (IOException e) {
                throw new CommandExecuteException("wc " + args[0] + "; err message: " + e.getMessage());
            }
        } else {
            // read from stdin arg
            if (stdin == null) {
                return null;
            }

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

            nBytes = buf.length;
            String s = new String(buf);
            nWords = countWords(s);
            nl = countNL(s);
        }


        bs = new ByteArrayInputStream((nl + "\t" + nWords + '\t' + nBytes + '\n').getBytes());

        return bs;
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
