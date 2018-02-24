package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.commands.AbstractCommand;
import ru.spbau.mit.sd.hw01.exceptions.IncorrectCommandException;
import ru.spbau.mit.sd.hw01.exceptions.LexicalException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.util.ArrayList;
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

    private InputStream execute_command(String raw_cmd, Environment env)
            throws LexicalException, IncorrectCommandException {
        PipedInputStream stdin = null;
        ArrayList<String> preprocessed_cmd = Preprocessor.preprocess(raw_cmd, env);

        boolean env_RO = false;
        if (preprocessed_cmd.contains("|")) {
            env.setReadOnly(true);
            env_RO = true;
        }

        ArrayList<Integer> pipeIndexes = new ArrayList<Integer>(); // indices of pipe symbols in arraylist
        for (int i = 0; i < preprocessed_cmd.size(); i++) {
            if ("|".equals(preprocessed_cmd.get(i))) {
                pipeIndexes.add(i);
            }
        }


        if (!pipeIndexes.isEmpty()) {
            int beg = 0;
            int end = pipeIndexes.get(0);
            final int commandsNumber = pipeIndexes.size() + 1;
            for (int cmd_no = 1; cmd_no <= commandsNumber; cmd_no++) {
                AbstractCommand cmd_obj =
                        CommandFactory.generate(preprocessed_cmd.subList(beg, end).toArray(new String[0]), env);
                stdin = cmd_obj.exec(stdin); // it updates


                beg = end + 1;
                if (cmd_no < pipeIndexes.size()) {
                    end = pipeIndexes.get(cmd_no);
                } else {
                    end = preprocessed_cmd.size();
                }
            }

        } else {
            AbstractCommand cmd_obj = CommandFactory.generate(preprocessed_cmd.toArray(new String[0]), env);
            stdin = cmd_obj.exec(null);
        }

        // restore env status
        if (env_RO) {
            env.setReadOnly(false);
        }

        // print stdin
        return stdin;
    }
}
