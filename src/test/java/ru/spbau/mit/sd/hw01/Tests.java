package ru.spbau.mit.sd.hw01;

import org.junit.Assert;
import org.junit.Test;
import ru.spbau.mit.sd.hw01.commands.Echo;
import ru.spbau.mit.sd.hw01.commands.Pwd;
import ru.spbau.mit.sd.hw01.exceptions.CommandExecuteException;
import ru.spbau.mit.sd.hw01.exceptions.CommandExitException;
import ru.spbau.mit.sd.hw01.exceptions.IncorrectCommandException;
import ru.spbau.mit.sd.hw01.exceptions.LexicalException;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Basic test cases
 */
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
            Assert.fail();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(absolutePath, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testEcho1() {
        Environment env = new Environment();
        InputStream out = null;
        String[] args = {"1", "hello", "\"dog\""};

        Echo echo = new Echo(args, env);
        out = echo.exec(null);

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals("1 hello \"dog\"", sc.nextLine());
        } else {
            Assert.fail();
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
            Assert.fail();
        }
        InputStream out = null;

        Echo echo = new Echo(args_preprocessed, env);
        out = echo.exec(null);

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testAssign() {
        Environment env = new Environment();
        String cmd = "dog=cat";

        Shell sh = new Shell();
        try {
            sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
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
            out = sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            while (sc.hasNext()) {
                System.out.println(sc.nextLine());
            }
        } else {
            Assert.fail();
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
            out = sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testWcFromFile() {
        Environment env = new Environment();
        String cmd = "wc src/test/resources/test04";
        String expected = "5\t6\t29";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testExecCommand() {
        Environment env = new Environment();
        String cmd = "ls -lh";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.executeCommand(cmd, env);
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
    public void testSubstitution() {
        Environment env = new Environment();
        String cmd1 = "x=1";
        String cmd2 = "echo \"123$x\"";
        String expected = "1231";

        Shell sh = new Shell();
        InputStream out = null;

        try {
            sh.executeCommand(cmd1, env);
            out = sh.executeCommand(cmd2, env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testExitUsingException() {
        Environment env = new Environment();
        String cmd = "exit";
        Shell sh = new Shell();

        try {
            sh.executeCommand(cmd, env);
        } catch (CommandExitException e) {
            return; // OK
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("unreachable code");

        }
    }


    @Test
    public void testLexicalError() {
        Environment env = new Environment();
        String cmd = "echo \"123 43 | wc";
        Shell sh = new Shell();

        try {
            sh.executeCommand(cmd, env);
        } catch (LexicalException e) {
            return; // OK
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("unreachable code");

        }
    }


    @Test
    public void testIncorrectCommandError() {
        Environment env = new Environment();
        String cmd = "exit 42";
        Shell sh = new Shell();

        try {
            sh.executeCommand(cmd, env);
        } catch (IncorrectCommandException e) {
            return; // OK
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("unreachable code");

        }
    }

    @Test
    public void testCommandExecuteError() {
        Environment env = new Environment();
        String cmd = "cat nofile__42__";
        Shell sh = new Shell();

        try {
            sh.executeCommand(cmd, env);
        } catch (CommandExecuteException e) {
            return; // OK
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("unreachable code");

        }
    }

    @Test
    public void testAssignBlockedInPipe() {
        Environment env = new Environment();
        String expected = "not_modified";
        env.set("x", expected);
        String cmd = "echo 42 | x=256 | echo $x";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testExecCommandPiped() {
        Environment env = new Environment();
        String expected1 = " hello v ";
        String expected2 = " world 1 ";
        String cmd = "printf \"" + expected1 + "\\n" + expected2 + "\" | grep hello";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected1, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

    @Test
    public void testCatFromStdin() {
        Environment env = new Environment();
        String expected1 = "hello world";
        String cmd = "echo hello world | cat ";

        Shell sh = new Shell();
        InputStream out = null;
        try {
            out = sh.executeCommand(cmd, env);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }

        if (out != null) {
            Scanner sc = new Scanner(out);
            Assert.assertEquals(expected1, sc.nextLine());
        } else {
            Assert.fail();
        }
    }

}
