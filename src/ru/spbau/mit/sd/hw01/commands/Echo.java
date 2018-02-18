package ru.spbau.mit.sd.hw01.commands;

public class Echo implements ICommand {
    private String[] args;

    public Echo(String[] args) {
        this.args = args;
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
