package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.*;
import java.nio.file.StandardCopyOption;

public class Exec extends AbstractCommand {
    public Exec(String[] args, Environment env) {
        super(args, env);
    }

    /**
     * Run command with arguments - bot from args[]
     * redirect stdin stream to it
     *
     * @param stdin is input stream of command (often Piped Stream)
     * @return input stream for next command (result)
     */
    @Override
    public PipedInputStream exec(InputStream stdin) {
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();

        try {
            pis.connect(pos);

            StringBuilder cmd = new StringBuilder();
            for (String s : args) {
                cmd.append(s);
                cmd.append(" ");
            }

            ProcessBuilder pb = new ProcessBuilder(cmd.toString());
            File stdinFile = File.createTempFile("au_sd2018", "simple_cli.tmp",
                    new File("/tmp"));

            java.nio.file.Files.copy(
                    stdin,
                    stdinFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            pb.redirectInput(stdinFile);

            Process ps = pb.start();
            ps.waitFor();

            InputStream psOutput = ps.getInputStream();
            int outputSize = psOutput.available();
            byte[] buffer = new byte[outputSize];
            int readedSize = psOutput.read(buffer);
            pos.write(buffer, 0, readedSize);

            stdinFile.delete();
            pos.flush();
            pos.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }


        return pis;
    }

}
