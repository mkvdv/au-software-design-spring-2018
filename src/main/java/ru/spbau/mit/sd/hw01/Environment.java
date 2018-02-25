package ru.spbau.mit.sd.hw01;

import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Store data for current shell session.
 */
public class Environment {
    private HashMap<String, String> env;
    private boolean readOnly = false; // bash don't modify env, if there any pipes

    public Environment() {
        this.env = new HashMap<>();
    }

    /**
     * @return absolute path to current directory
     */
    public String getCurrentDir() {
        return Paths.get("").toAbsolutePath().toString();
    }

    /**
     * Set new value val for key
     *
     * @param key
     * @param val
     */
    public void set(String key, String val) {
        if (!readOnly)
            env.put(key, val);
    }

    /**
     * Return value for key
     *
     * @param key
     * @return return value for key, or null if there no such key
     */
    public String get(String key) {
        return env.get(key);
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(final boolean newReadOnly) {
        readOnly = newReadOnly;
    }
}
