package ru.spbau.mit.sd.hw01;

import java.nio.file.Paths;
import java.util.HashMap;

public class Environment {
    private HashMap<String, String> env;

    public Environment() {
        this.env = new HashMap<>();
    }

    public String getCurrentDir() {
        return Paths.get("").toAbsolutePath().toString();
    }

    public void set(String key, String val) {
        env.put(key, val);
    }

    public String get(String key) {
        return env.get(key);
    }
}
