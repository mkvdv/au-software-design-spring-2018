package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;

/**
 * Class for execution command, that shell doesn't support.
 */
public class Exec extends AbstractCommand {
    public Exec(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Run command with arguments - bot from args[]
     * redirect stdin stream to it
     * Use temporary file (kostil') for redirecting input.
     *
     * @param stdin is input stream of command (often Piped Stream)
     * @return input stream for next command (result)
     */
    @Override
    public InputStream exec(InputStream stdin) throws CommandExecuteException {
        InputStream output = null;
        File stdinFile = null;

        try {
            ProcessBuilder pb = new ProcessBuilder(args);

            // use tmp file for redirecting input - bad design
            if (stdin != null) {
                stdinFile = File.createTempFile("au_sd2018", "simple_cli.tmp",
                        new File("/tmp"));
                java.nio.file.Files.copy(
                        stdin,
                        stdinFile.toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
                pb.redirectInput(stdinFile);
            }

            Process ps = pb.start();
            ps.waitFor();

            output = ps.getInputStream();

            if (stdinFile != null) {
                stdinFile.delete();
            }

        } catch (IOException | InterruptedException e) {
            throw new CommandExecuteException(e.getMessage());
        }

        return output;
    }

}
