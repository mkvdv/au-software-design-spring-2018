package ru.spbau.mit.sd.hw01.commands;

import ru.spbau.mit.sd.hw01.Environment;

public class Pwd implements ICommand {
    @Override
    public void exec(String[] input) {
        System.out.print(Environment.getCurrentDir());
    }
}
