package ru.spbau.mit.sd.hw01.exceptions;

public class LexicalException extends Exception {
    public LexicalException(String s) {
        super("#ERR: Lexical error: " + s);
    }
}


