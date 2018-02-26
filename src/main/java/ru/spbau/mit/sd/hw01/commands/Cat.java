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
        } catch (java.nio.file.NoSuchFileException e) {
            throw new CommandExecuteException("cat " + args[0] + "; no such file");
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new CommandExecuteException("cat " + args[0]);
        }
        return pis;
    }
}
