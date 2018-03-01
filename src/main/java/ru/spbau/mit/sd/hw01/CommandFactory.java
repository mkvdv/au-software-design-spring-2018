package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.commands.*;
import ru.spbau.mit.sd.hw01.exceptions.IncorrectCommandException;

import java.util.Arrays;

/**
 * Provide static function for generating command object from array of strings.
 */
public class CommandFactory {
    /**
     * Generating command object from array of strings.
     *
     * @param cmd array of command names and arguments
     * @param env current shell environment
     * @return command object
     * @throws IncorrectCommandException if command format is wrong
     */
    public static AbstractCommand generate(String[] cmd, Environment env) throws IncorrectCommandException {
        if (cmd.length == 3 && cmd[1].equals("=")) {
            String[] args = {cmd[0], cmd[2]};
            return new Assign(args, env);

        } else {
            String[] args = Arrays.copyOfRange(cmd, 1, cmd.length);
            switch (cmd[0]) {
                case "cat":
                    if (args.length > 1) { // can read from stdin if 0 args
                        throw new IncorrectCommandException(cmd[0]);
                    }
                    return new Cat(args, env);

                case "echo":
                    return new Echo(args, env);

                case "exit":
                    if (args.length != 0) {
                        throw new IncorrectCommandException(cmd[0]);
                    }
                    return new Exit(args, env);

                case "pwd":
                    if (args.length != 0) {
                        throw new IncorrectCommandException(cmd[0]);
                    }
                    return new Pwd(args, env);

                case "wc":
                    if (args.length > 1) {
                        throw new IncorrectCommandException(cmd[0]);
                    }
                    return new Wc(args, env);

                default:
                    return new Exec(cmd, env);
            }
        }
    }
}
