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
 * Class for execution commands like: cat file1
 */
public class Cat extends AbstractCommand {
    public Cat(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Print file to returned stream.
     *
     * @param stdin is input stream of command (often Piped Stream)
     * @return stream, contain text of file.
     * @throws CommandExecuteException
     */
    @Override
    public InputStream exec(InputStream stdin) throws CommandExecuteException {
        Log.info("cat with " + Arrays.toString(args));
        ByteArrayInputStream bs = null;


        if (args.length == 1) {
            // read from file
            try {
                Path path = Paths.get(args[0]);
                byte[] bytes;
                bytes = Files.readAllBytes(path);
                bs = new ByteArrayInputStream(bytes);
            } catch (java.nio.file.NoSuchFileException e) {
                throw new CommandExecuteException("cat " + args[0] + "; no such file");
            } catch (IOException e) {
                throw new CommandExecuteException("cat " + args[0] + "; mesg: " + e.getMessage());
            }
        } else {
            int len = 0;
            final int size = 1024;
            byte[] buf = new byte[size];

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                while ((len = stdin.read(buf, 0, size)) != -1)
                    bos.write(buf, 0, len);
            } catch (IOException e) {
                throw new CommandExecuteException("cat, IO exception: " + e.getMessage());
            }
            bs = new ByteArrayInputStream(bos.toByteArray());
        }
        return bs;
    }
}
