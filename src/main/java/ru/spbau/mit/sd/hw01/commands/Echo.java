package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
     */
    @Override
    public InputStream exec(InputStream stdin) {
        Log.info("echo with " + Arrays.toString(args));
        ByteArrayInputStream bs = null;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < args.length; i++) {

            sb.append(args[i]);
            if (i != args.length - 1) {
                sb.append(' ');
            }
        }
        sb.append('\n');

        bs = new ByteArrayInputStream(sb.toString().getBytes());

        return bs;
    }
}
