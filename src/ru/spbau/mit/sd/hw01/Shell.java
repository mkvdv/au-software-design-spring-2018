package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.commands.AbstractCommand;
import ru.spbau.mit.sd.hw01.exceptions.IncorrectCommandException;
import ru.spbau.mit.sd.hw01.exceptions.LexicalException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.Scanner;


public class Shell {
    public void run() {
        Log.info("Shell Started");
        Scanner sc = new Scanner(System.in);
        Environment env = new Environment();

        while (true) {
            System.out.print("> ");
            if (sc.hasNext()) {
                String line = sc.nextLine();

                InputStream res = null;
                try {
                    res = execute_command(line, env);
                } catch (LexicalException e) {
                    System.out.println(e.getMessage());
                } catch (IncorrectCommandException e) {
                    e.printStackTrace(); // todo fix
                }

                // todo move to utils
                if (res != null) {
                    byte[] buf = new byte[0];
                    try {
                        buf = new byte[res.available()];
                        int size = res.read(buf);
                        System.out.write(buf, 0, size);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private InputStream execute_command(String s, Environment env)
            throws LexicalException, IncorrectCommandException {
        String[] commands = s.split("\\|");

        PipedInputStream stdin = null;

        for (String raw_cmd : commands) {
            String[] preprocessed_cmd = Preprocessor.preprocess(raw_cmd, env).toArray(new String[0]);
            AbstractCommand cmd_obj = CommandFactory.generate(preprocessed_cmd, env);
            stdin = cmd_obj.exec(stdin); // it updates
        }

        // print stdin
        return stdin;
    }
}
