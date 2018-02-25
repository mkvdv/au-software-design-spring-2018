package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Class for execution commands like: pwd
 */
public class Pwd extends AbstractCommand {
    public Pwd(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Execute external program.
     *
     * @param stdin is input stream for command (often Piped Stream)
     * @return stream with result of execution in it.
     * @throws CommandExecuteException if can't write to stream.
     */
    @Override
    public PipedInputStream exec(InputStream stdin) throws CommandExecuteException {
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();

        try {
            pis.connect(pos);
            pos.write(env.getCurrentDir().getBytes());
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
