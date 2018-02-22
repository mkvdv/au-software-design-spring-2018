package ru.spbau.mit.sd.hw01.exceptions;

public class LexicalException extends Exception {
    private String mesg;

    public LexicalException(String s) {
        mesg = s;
    }

    @Override
    public String getMessage() {
        return mesg;
    }
}


