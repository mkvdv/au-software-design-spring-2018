package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.commands.AbstractCommand;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.exceptions.CommandExitException;
import ru.spbau.mit.sd.hw01.exceptions.IncorrectCommandException;
import ru.spbau.mit.sd.hw01.exceptions.LexicalException;
import ru.spbau.mit.sd.hw01.utils.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class for reading and execution commands.
 */
public class Shell {
    /**
     * Run shell session, return only if exit command typed.
     */
    public void run() {
        Log.info("Shell Started");
        Scanner sc = new Scanner(System.in);
        Environment env = new Environment();

        // return using 'exit' shell command
        while (true) {
            System.out.print("> ");
            String line = null;
            try {
                line = sc.nextLine();
            } catch (NoSuchElementException e) {
                return;
            }
            if (line != null && line.isEmpty()) {
                continue;
            }

            InputStream res = null;
            try {
                res = executeCommand(line, env);
            } catch (LexicalException | IncorrectCommandException | CommandExecuteException e) {
                System.out.println(e.getMessage());
            } catch (CommandExitException e) {
                return;
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

    /**
     * Execute raw command, return stream, which contain output of command.
     *
     * @param rawCmd input string from console
     * @param env    environment, store variables
     * @return InputStream with output of command in it
     * @throws LexicalException          if there lexical error
     * @throws IncorrectCommandException if command format is wrong
     * @throws CommandExecuteException   if some error during execution of command
     */
    public InputStream executeCommand(String rawCmd, Environment env)
            throws LexicalException, IncorrectCommandException, CommandExecuteException, CommandExitException {
        InputStream stdin = null;
        List<String> preprocessedCmd = Preprocessor.preprocess(rawCmd, env);

        boolean envRO = false;
        if (preprocessedCmd.contains("|")) {
            env.setReadOnly(true);
            envRO = true;
        }

        ArrayList<Integer> pipeIndexes = new ArrayList<>(); // indices of pipe symbols in array list
        for (int i = 0; i < preprocessedCmd.size(); i++) {
            if ("|".equals(preprocessedCmd.get(i))) {
                pipeIndexes.add(i);
            }
        }

        if (!pipeIndexes.isEmpty()) {
            int beg = 0;
            int end = pipeIndexes.get(0);
            final int commandsNumber = pipeIndexes.size() + 1;
            for (int cmdNo = 1; cmdNo <= commandsNumber; cmdNo++) {
                AbstractCommand cmdObj =
                        CommandFactory.generate(preprocessedCmd.subList(beg, end).toArray(new String[0]), env);
                stdin = cmdObj.exec(stdin); // it updates

                beg = end + 1;
                if (cmdNo < pipeIndexes.size()) {
                    end = pipeIndexes.get(cmdNo);
                } else {
                    end = preprocessedCmd.size();
                }
            }
        } else {
            AbstractCommand cmdObj = CommandFactory.generate(preprocessedCmd.toArray(new String[0]), env);
            stdin = cmdObj.exec(null);
        }

        // restore env status
        if (envRO) {
            env.setReadOnly(false);
        }

        // print stdin
        return stdin;
    }
}
