package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

public class Exit extends AbstractCommand {
    Exit(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public void exec(String[] input) {
        System.exit(0); // dirty todo redo?
    }
}
