package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.util.ArrayList;

public class Lexer {
    /**
     * Extract all "text" and 'text' from strings,
     * split using '=' symbol, so it looks like list of
     * shell arguments
     *
     * @param s string command without pipe symbols
     * @return
     */
    public static ArrayList<String> tokenize(String s) throws LexicalException {
        ArrayList<String> arr = new ArrayList<>();

        int ix = 0;
        while (ix < s.length()) {
            switch (s.charAt(ix)) {
                case ' ':
                case '\t':
                    ix++;
                    break;

                case '\"':
                case '\'': {
                    final int begin = ix;
                    final int end_ix = extract_str(s, ix);
                    arr.add(s.substring(begin, end_ix + 1));
                    ix = end_ix + 1;
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
                    int end_ix = extract_name(s, ix);
                    arr.add(s.substring(ix, end_ix));
                    ix = end_ix;
                }
                break;


                default: {
                    int end_ix = extract_token(s, ix);
                    arr.add(s.substring(ix, end_ix));
                    ix = end_ix;
                }
                break;
            }
        }

        return arr;
    }

    /**
     * @param s
     * @param ix
     * @return ix after ending of $name
     */
    private static int extract_name(String s, int ix) {
        ix++;
        while (ix < s.length() && !Character.isWhitespace(s.charAt(ix)) && s.charAt(ix) != '=') {
            ix++;
        }
        // todo corner cases ?

        return ix;
    }

    /**
     * Retrun ix after any other token, not beginning with $ or ("|')
     *
     * @param s
     * @param ix
     * @return first index after ending
     */
    private static int extract_token(String s, int ix) {
        ix++;

        while (ix < s.length() && !Character.isWhitespace(s.charAt(ix)) && s.charAt(ix) != '=') {
            ix++;
        }

        return ix;
    }

    /**
     * @param s
     * @param ix
     * @return return ix after ending ' or " symbol
     */
    private static int extract_str(String s, int ix) throws LexicalException {
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
