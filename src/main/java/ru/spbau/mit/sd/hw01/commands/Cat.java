package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.ByteArrayInputStream;
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
        assert (args.length == 1); // here just 1 file
        Log.info("cat with " + Arrays.toString(args));
        ByteArrayInputStream bs = null;

        Path path = Paths.get(args[0]);
        byte[] bytes;

        try {
            bytes = Files.readAllBytes(path);
            bs = new ByteArrayInputStream(bytes);
        } catch (java.nio.file.NoSuchFileException e) {
            throw new CommandExecuteException("cat " + args[0] + "; no such file");
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new CommandExecuteException("cat " + args[0]);
        }
        return bs;
    }
}
