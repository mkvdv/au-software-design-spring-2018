package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;

import java.io.*;
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
    public PipedInputStream exec(InputStream stdin) throws CommandExecuteException {
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();
        File stdinFile = null;

        try {
            pis.connect(pos);
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

            InputStream psOutput = ps.getInputStream();
            int outputSize = psOutput.available();
            byte[] buffer = new byte[outputSize];
            int readedSize = psOutput.read(buffer);
            pos.write(buffer, 0, readedSize);

            if (stdinFile != null) {
                stdinFile.delete();
            }
            pos.flush();
            pos.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new CommandExecuteException(e.getMessage());
        }

        return pis;
    }

}
