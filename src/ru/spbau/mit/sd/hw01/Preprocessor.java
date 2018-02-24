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
            String[] words = tokenized.get(i).split("\\s");
            Boolean replaced = false;
            for (int j = 0; j < words.length; j++) {
                if (words[j].startsWith("$")) {
                    String replacement = env.get(words[j].substring(1));
                    if (replacement != null) {
                        words[j] = replacement;
                        replaced = true;
                    }
                }
            }
            if (replaced) {
                StringBuilder builder = new StringBuilder();
                for (String s : words) {
                    builder.append(s);
                }
                tokenized.set(i, builder.toString());
            }
        }

        if (tokenized.get(tokenized.size() - 1).equals("|")) {
            throw new LexicalException("Nothing after pipe command");
        }

        return tokenized;
    }


}
