package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.commands.*;
import ru.spbau.mit.sd.hw01.exceptions.IncorrectCommandException;

import java.util.Arrays;


public class CommandFactory {
    public static AbstractCommand generate(String[] cmd, Environment env) throws IncorrectCommandException {
        if (cmd.length == 3 && cmd[1].equals("=")) {
            String[] args = {cmd[0], cmd[2]};
            return new Assign(args, env);

        } else {
            String[] args = Arrays.copyOfRange(cmd, 1, cmd.length);
            switch (cmd[0]) {
                case "cat":
                    if (args.length != 1) // exactly one!
                        throw new IncorrectCommandException();
                    return new Cat(args, env);

                case "echo":
                    return new Echo(args, env);

                case "exit":
                    if (args.length != 0) {
                        throw new IncorrectCommandException();
                    }
                    return new Exit(args, env);

                case "pwd":
                    if (args.length != 0) {
                        throw new IncorrectCommandException();
                    }
                    return new Pwd(args, env);

                case "wc":
                    if (args.length > 1) {
                        throw new IncorrectCommandException();
                    }
                    return new Wc(args, env);

                default:
                    return new Exec(cmd, env);
            }
        }
    }
}
