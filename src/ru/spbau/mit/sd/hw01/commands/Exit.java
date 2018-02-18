package ru.spbau.mit.sd.hw01.commands;

public class Exit implements ICommand {
    @Override
    public void exec(String[] input) {
        System.exit(0); // dirty todo redo?
    }
}
