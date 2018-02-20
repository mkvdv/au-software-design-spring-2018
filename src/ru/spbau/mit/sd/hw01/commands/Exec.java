package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

import java.io.*;
import java.nio.file.StandardCopyOption;

public class Exec extends AbstractCommand {
    Exec(String[] args, Environment env) {
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
    PipedInputStream exec(InputStream stdin) {
        PipedInputStream pis = new PipedInputStream();
        PipedOutputStream pos = new PipedOutputStream();

        try {
            pis.connect(pos);

            StringBuilder cmd = new StringBuilder(10);
            for (String s : args) {
                cmd.append(s);
                cmd.append(" ");
            }

            ProcessBuilder pb = new ProcessBuilder(cmd.toString());
            File stdin_file = File.createTempFile("au_sd2018", "simple_cli.tmp", new File("/tmp"));

            java.nio.file.Files.copy(
                    stdin,
                    stdin_file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
            pb.redirectInput(stdin_file);

            Process ps = pb.start();
            ps.waitFor();

            InputStream ps_output = ps.getInputStream();
            int output_size = ps_output.available();
            byte[] buffer = new byte[output_size];
            int readed_size = ps_output.read(buffer);
            pos.write(buffer, 0, readed_size);

            stdin_file.delete();
            pos.flush();
            pos.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }


        return pis;
    }

}
