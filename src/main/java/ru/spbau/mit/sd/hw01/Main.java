package ru.spbau.mit.sd.hw01;

import java.util.Arrays;

public class Main {

    public static void main(String[] args_) {
//        Shell sh = new Shell();
//        sh.run();

        String s = "\"cat$dog in cat\\$dog kitchen near \\\"$dog\\\"";
        String regex = "(?=\\\\\\$|\\s|\\$|\\\\\'|\\\\\")";
        System.out.println(Arrays.toString(s.split(regex)));

        StringBuilder sb = new StringBuilder();
        for (String str : s.split(regex)) {
            sb.append(str);
        }
        System.out.println(s);
        System.out.println(sb.toString());

    }
}
