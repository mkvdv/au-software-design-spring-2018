package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.util.ArrayList;


public class Preprocessor {
    /**
     * @param cmd raw command between pipes, not preprocessed
     * @return array
     */
    public static ArrayList<String> preprocess(String cmd, Environment env) throws LexicalException {
        if (cmd == null)
            return null;

        ArrayList<String> tokenized = Lexer.tokenize(cmd);

        for (int i = 0; i < tokenized.size(); i++) {
            if (tokenized.get(i).charAt(0) == '\''
                    && tokenized.get(i).charAt(tokenized.get(i).length() - 1) == '\'') {
                // don't substitute
                tokenized.set(i, removeQuotations(tokenized.get(i)));
                continue;
            }

            if (tokenized.get(i).charAt(0) == '\"'
                    && tokenized.get(i).charAt(tokenized.get(i).length() - 1) == '\"') {
                tokenized.set(i, removeQuotations(tokenized.get(i)));
            }

            String regex = "(?=\\\\\\$|\\s|\\$|\\\\\'|\\\\\")";
            String[] words = tokenized.get(i).split(regex);
            Boolean replaced = false;
            for (int j = 0; j < words.length; j++) {
                if (words[j].startsWith("$")
                        && (j == 0 || !words[j - 1].equals("\\"))) { // \$ is $
                    String replacement = env.get(words[j].substring(1));
                    if (replacement != null) {
                        words[j] = replacement;
                    } else {
                        words[j] = "";
                    }
                    replaced = true;
                }
            }
            if (replaced) {
                StringBuilder builder = new StringBuilder();
                for (String s : words) {
                    builder.append(s);
                }
                tokenized.set(i, builder.toString());
            }

            tokenized.set(i, tokenized.get(i).replaceAll("\\\\", ""));
        }

        if (tokenized.get(tokenized.size() - 1).equals("|")) {
            throw new LexicalException("Nothing after pipe command");
        }

        return tokenized;
    }

    private static String removeQuotations(String s) {
        assert s.length() >= 2;
        return s.substring(1, s.length() - 1);
    }

    /**
     * Substitute all occurrences like $key int string, using dict from Environment
     *
     * @param s
     * @param env
     * @return
     */
    private static String substitute(String s, Environment env) {
        StringBuilder substituted = new StringBuilder(s.length());

        int beg = 0;
        int ix = s.indexOf('$');

        while (ix >= 0) {
            substituted.append(s.substring(beg, ix));
            beg = ix + 1;
            ix = s.indexOf("\\s");
            String key = s.substring(beg, ix);
            String replacement = env.get(key);
            if (replacement != null) {
                substituted.append(replacement);
            }

            beg = ix;
            ix = s.indexOf('$', ix);
        }

        return substituted.toString();
    }

}
