package ru.spbau.mit.sd.hw01.exceptions;

/**
 * Exception for catching errors in lexical analise.
 */
public class LexicalException extends Exception {
    public LexicalException(String s) {
        super("#ERR: Lexical error: " + s);
    }
}


