package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

abstract class AbstractCommand {
    String[] args;
    Environment env;

    AbstractCommand(String[] args, Environment env) {
        this.args = args;
        this.env = env;
    }


    abstract void exec(String[] input);
}
