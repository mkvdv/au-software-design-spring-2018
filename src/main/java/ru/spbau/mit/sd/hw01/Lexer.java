package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.util.ArrayList;
import java.util.List;

/**
 * Store some static functions for tokenizing strings.
 */
public class Lexer {
    /**
     * Extract all tokens, return list of command arguments (names and strings - tokens)
     *
     * @param s raw command string
     * @return array of tokens (command args and names)
     * @throws LexicalException if quotation not ending or other inner exception
     */
    public static List<String> tokenize(final String s) throws LexicalException {
        List<String> arr = new ArrayList<>();

        int ix = 0;
        while (ix < s.length()) {
            switch (s.charAt(ix)) {
                case ' ':
                case '\t':
                    ix++;
                    break;

                case '\"':
                case '\'': {
                    final int beginIndex = ix;
                    final int endIndex = extractStr(s, ix);
                    arr.add(s.substring(beginIndex, endIndex + 1));
                    ix = endIndex + 1;
                }
                break;

                case '=':
                    ix++;
                    arr.add("=");
                    break;

                case '|':
                    ix++;
                    arr.add("|");
                    break;

                case '$': {
                    int endIndex = extractName(s, ix);
                    arr.add(s.substring(ix, endIndex));
                    ix = endIndex;
                }
                break;


                default: {
                    int endIndex = extractToken(s, ix);
                    arr.add(s.substring(ix, endIndex));
                    ix = endIndex;
                }
                break;
            }
        }

        return arr;
    }

    /**
     * ix after ending of $name
     */
    private static int extractName(final String s, int ix) {
        ix++;
        while (ix < s.length() && !Character.isWhitespace(s.charAt(ix)) && s.charAt(ix) != '=') {
            ix++;
        }

        return ix;
    }

    /**
     * Return ix after any other token, not beginning with $ or ("|').
     *
     * @return first index after ending
     */
    private static int extractToken(final String s, int ix) {
        ix++;

        while (ix < s.length() && !Character.isWhitespace(s.charAt(ix)) && s.charAt(ix) != '=') {
            ix++;
        }

        return ix;
    }

    /**
     * Return index after string argument ends.
     *
     * @return return ix after ending ' or " symbol
     * @throws LexicalException if not ending quotation
     */
    private static int extractStr(final String s, int ix) throws LexicalException {
        char quotation = s.charAt(ix);
        ix++;
        while (ix < s.length() && s.charAt(ix) != quotation) {
            ix++;
        }
        if (ix == s.length() || s.charAt(ix) != quotation) {
            throw new LexicalException("not ending quotation");
        }

        return ix;
    }
}
