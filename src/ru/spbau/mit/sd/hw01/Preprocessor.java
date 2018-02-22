package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.util.ArrayList;
import java.util.Arrays;


public class Preprocessor {

    /**
     * @param cmd raw command between pipes, not preprocessed
     * @return array
     */
    public static ArrayList<String> preprocess(String cmd, Environment env) throws LexicalException {
        if (cmd == null)
            return null;

        ArrayList<String> splitted = Preprocessor.tokenize(cmd);

        for (int i = 0; i < splitted.size(); i++) {
            String[] words = splitted.get(i).split("\\w");
            Boolean replaced = false;
            for (int j = 0; j < words.length; j++) {
                if (words[j].startsWith("$")) {
                    String replacement = env.get(words[j].substring(1));
                    if (replacement != null) {
                        words[j] = "$" + replacement;
                        replaced = true;
                    }
                }
            }
            if (replaced) {
                splitted.set(i, Arrays.toString(words));
            }
        }

        return splitted;
    }

    /**
     * Extract all "text" and 'text' from strings,
     * split using '=' symbol, so it looks like list of
     * shell arguments
     *
     * @param s string command without pipe symbols
     * @return
     */
    private static ArrayList<String> tokenize(String s) throws LexicalException {
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
                    int end_ix = extract_str(s, ix);
                    arr.add(s.substring(ix, end_ix));
                    ix = end_ix;
                }
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
        while (ix < s.length() && !Character.isWhitespace(s.charAt(ix))) {
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

        while (ix < s.length() && !Character.isWhitespace(s.charAt(ix))) {
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
