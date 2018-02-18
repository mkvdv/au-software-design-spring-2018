package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

public class Eq extends AbstractCommand {
    Eq(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public void exec(String[] input) {
        assert (args.length == 2);

        env.set(args[0], args[1]);
    }
}
