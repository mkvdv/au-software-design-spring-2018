package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Arrays;

/**
 * Class for execution commands like: echo arg1 arg2 $x
 */
public class Echo extends AbstractCommand {

    public Echo(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Print args to output stream, return stream.
     *
     * @param stdin is input stream of command (often Piped Stream)
     * @return stream with printed result
     * @throws CommandExecuteException
     */
    @Override
    public PipedInputStream exec(InputStream stdin) throws CommandExecuteException {
        Log.info("echo with " + Arrays.toString(args));
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        try {
            pis.connect(pos);

            for (int i = 0; i < args.length; i++) {
                pos.write(args[i].getBytes());
                if (i != args.length - 1) {
                    pos.write(" ".getBytes());
                }
            }
            pos.write('\n');

            pos.flush();
            pos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommandExecuteException(e.getMessage());
        }

        return pis;
    }
}
