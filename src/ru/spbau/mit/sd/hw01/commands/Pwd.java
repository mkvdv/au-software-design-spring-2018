package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

public class Pwd extends AbstractCommand {
    Pwd(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public void exec(String[] input) {
        System.out.print(env.getCurrentDir());
    }
}
