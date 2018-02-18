package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

public class Echo extends AbstractCommand {

    Echo(String[] args, Environment env) {
        super(args, env);
    }

    @Override
    public void exec(String[] input) {
        for (String s : args) {
            System.out.print(s);
            System.out.print(" ");
        }
        System.out.println();
    }
}
