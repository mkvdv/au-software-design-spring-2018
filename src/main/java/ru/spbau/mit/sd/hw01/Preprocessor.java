package ru.spbau.mit.sd.hw01;

import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.util.List;


/**
 * Contain functions for preprocessing strings.
 */
public class Preprocessor {
    /**
     * Preprocess raw command, make substitution, split to tokens.
     *
     * @param cmd raw command
     * @param env - enviroment with variables
     * @return array of arguments
     * @throws LexicalException if command is incorrect
     */
    public static List<String> preprocess(final String cmd, final Environment env) throws LexicalException {
        if (cmd == null) {
            return null;
        }

        List<String> tokenized = Lexer.tokenize(cmd);

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

            tokenized.set(i, tokenized.get(i).replaceAll("\\\\\\$", "\\$"));
            tokenized.set(i, tokenized.get(i).replaceAll("\\\\\"", "\""));
            tokenized.set(i, tokenized.get(i).replaceAll("\\\\\'", "\'"));
        }

        if (tokenized.get(tokenized.size() - 1).equals("|")) {
            throw new LexicalException("Nothing after pipe command");
        }

        return tokenized;
    }

    private static String removeQuotations(final String s) {
        assert s.length() >= 2;
        return s.substring(1, s.length() - 1);
    }

}
