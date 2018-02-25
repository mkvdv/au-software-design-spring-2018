package ru.spbau.mit.sd.hw01;


import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.sd.hw01.commands.Echo;
import ru.spbau.mit.sd.hw01.commands.Pwd;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Scanner;

public class Tests {
    @Test
    public void testPwd() {
        Environment env = new Environment();
        String absolutePath = Paths.get("").toAbsolutePath().toString();
        InputStream out = null;

        Pwd pwd = new Pwd(null, env);
        try {
            out = pwd.exec(null);
        } catch (CommandExecuteException e) {
            System.err.println(e.getMessage());
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(absolutePath, sc.nextLine());
        }
    }

    @Test
    public void testEcho1() {
        Environment env = new Environment();
        InputStream out = null;
        String[] args = {"1", "hello", "\"dog\""};

        Echo echo = new Echo(args, env);
        try {
            out = echo.exec(null);
        } catch (CommandExecuteException e) {
            System.err.println(e.getMessage());
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals("1 hello \"dog\"", sc.nextLine());
        }
    }

    @Test
    public void testEchoAndPreprocessor() {
        Environment env = new Environment();
        env.set("dog", "cat");

        String args = "$dog '$dog' \"$dog\" \\$dog \\\"$dog\\\" \\'$dog\\' \\\"\\$dog\\\" \\'\\$dog\\' $duck";
        String expected = "cat $dog cat $dog \"cat\" 'cat' \"$dog\" '$dog' ";
        String[] args_preprocessed = new String[0];
        try {
            args_preprocessed = Preprocessor.preprocess(args, env).toArray(new String[0]);
        } catch (LexicalException e) {
            e.printStackTrace();
        }
        InputStream out = null;

        Echo echo = new Echo(args_preprocessed, env);
        try {
            out = echo.exec(null);
        } catch (CommandExecuteException e) {
            System.err.println(e.getMessage());
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        }
    }

    @Test
    public void testAssign() {
        Environment env = new Environment();
        String cmd = "dog=cat";

        Shell sh = new Shell();
        try {
            sh.execute_command(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals("cat", env.get("dog"));
    }

    @Test
    public void testCat() {
        Environment env = new Environment();
        String cmd = "cat src/test/resources/test04";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.execute_command(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
        }
    }

    @Test
    public void testCatWcAndPipe() {
        Environment env = new Environment();
        String cmd = "cat src/test/resources/test04 | wc";
        String expected = "5\t6\t29";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.execute_command(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        }
    }

    @Test
    public void testExecCommand() {
        Environment env = new Environment();
        String cmd = "ls -lh";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.execute_command(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
        }
    }

}
