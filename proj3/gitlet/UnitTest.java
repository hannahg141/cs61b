package gitlet;

import ucb.junit.textui;
import org.junit.Test;
import java.io.IOException;
import java.io.File;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author
 */
public class UnitTest {

    /**
     * Run the JUnit tests in the loa package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
    }


    @Test
    public void mergeNoConflicts() throws IOException {
        String[] args = {"init"};
        Main.main(args);
        File f = new File(System.getProperty("user.dir") + "/f.txt");

        f.createNewFile();
        Utils.writeContents(f, "This is a wug.".getBytes());
        String[] args3 = {"add", "f.txt"};
        Main.main(args3);

        File g = new File(System.getProperty("user.dir") + "/g.txt");
        g.createNewFile();
        Utils.writeContents(g, "This is not a wug.".getBytes());

        String[] args4 = {"add", "g.txt"};
        Main.main(args4);
        String[] args5 = {"commit", "Two files"};
        Main.main(args5);

        String[] args6 = {"branch", "other"};
        Main.main(args6);
        File h = new File(System.getProperty("user.dir") + "/h.txt");
        h.createNewFile();
        Utils.writeContents(h, "This is 2 wugs.".getBytes());
        String[] args7 = {"add", "h.txt"};
        Main.main(args7);
        String[] args8 = {"rm", "g.txt"};
        Main.main(args8);
        String[] args9 = {"commit", "add h and remove g"};
        Main.main(args9);
        String[] args10 = {"checkout", "other"};
        Main.main(args10);
        String[] args12 = {"rm", "f.txt"};
        Main.main(args12);
        File k = new File(System.getProperty("user.dir") + "/k.txt");
        k.createNewFile();
        Utils.writeContents(k, "This is 3 wugs.".getBytes());
        String[] args11 = {"add", "k.txt"};
        Main.main(args11);
        String[] args13 = {"commit", "add k and remove f"};
        Main.main(args13);
        String[] args14 = {"checkout", "master"};
        Main.main(args14);
        String[] args15 = {"merge", "other"};
        Main.main(args15);
        String[] args16 = {"status"};
        Main.main(args16);

    }

    @Test
    public void mergeConflict() throws IOException {
        String[] args = {"init"};
        Main.main(args);
        File f = new File(System.getProperty("user.dir") + "/f.txt");
        f.createNewFile();
        Utils.writeContents(f, "This is a wug.".getBytes());
        String[] args3 = {"add", "f.txt"};
        Main.main(args3);
        File g = new File(System.getProperty("user.dir") + "/g.txt");
        g.createNewFile();
        Utils.writeContents(g, "This is not a wug.".getBytes());
        String[] args4 = {"add", "g.txt"};
        Main.main(args4);
        String[] args5 = {"commit", "Two files"};
        Main.main(args5);
        String[] args6 = {"branch", "other"};
        Main.main(args6);
        File h = new File(System.getProperty("user.dir") + "/h.txt");
        h.createNewFile();
        Utils.writeContents(h, "This is 2 wugs.".getBytes());
        String[] args7 = {"add", "h.txt"};
        Main.main(args7);
        String[] args8 = {"rm", "g.txt"};
        Main.main(args8);
        File f1 = new File(System.getProperty("user.dir") + "/f.txt");
        f1.createNewFile();
        Utils.writeContents(f1, "This is 2 wugs.".getBytes());
        String[] args9 = {"add", "f.txt"};
        Main.main(args9);

        String[] args10 = {"commit", "add h, remove g, change f"};
        Main.main(args10);
        String[] args11 = {"checkout", "other"};
        Main.main(args11);
        File f2 = new File(System.getProperty("user.dir") + "/f.txt");
        f2.createNewFile();
        Utils.writeContents(f2, "This is not a wug.".getBytes());
        String[] args12 = {"add", "f.txt"};
        Main.main(args12);
        File k = new File(System.getProperty("user.dir") + "/k.txt");
        k.createNewFile();
        Utils.writeContents(k, "This is 3 wugs.".getBytes());
        String[] args13 = {"add", "k.txt"};
        Main.main(args13);
        String[] args14 = {"commit", "add k and modify f"};
        Main.main(args14);
        String[] args15 = {"checkout", "master"};
        Main.main(args15);
        String[] args16 = {"log"};
        Main.main(args16);
        String[] args17 = {"merge", "other"};
        Main.main(args17);
        String[] args18 = {"log"};
        Main.main(args18);


    }
}


