package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

public class Eq implements ICommand {
    private String var;
    private String value;
    private Environment env;

    public Eq(String var, String value, Environment env) {
        this.var = var;
        this.value = value;
        this.env = env;
    }


    @Override
    public void exec(String[] input) {
        env.set(var, value);
    }
}
