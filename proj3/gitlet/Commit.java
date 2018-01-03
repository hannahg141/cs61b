package gitlet;

import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.time.ZonedDateTime;

/**Commit class.
 * @author hannahgrossman
 */

public class Commit implements Serializable {

    /** commit message.*/
    private String _msg;

    /** commit time.*/
    private String _date;

    /**SHA1 of a commit.*/
    private String _sha1;

    /**second parent of a commit.*/
    private String _second;

    /** time of a commit.*/
    private ZonedDateTime _time;

    /**parent commit SHA-1.*/
    private String _prev;

    /**all the blobs for a commit.*/
    private HashMap<String, String> _blobs = new HashMap<>();



    /**print in correct format.*/
    public void print() {
        System.out.println("===");
        System.out.println("commit " + _sha1);
        if (_second != null) {
            String one = _prev.substring(0, 7);
            String two = _second.substring(0, 7);
            System.out.println("Merge: " + one + " " + two);

        }
        System.out.println(_date);
        System.out.println(_msg);
        System.out.println();

    }


    /**Constructor for a commit.
     * @param prev SHA-1 parent commit.
     * @param msg commit message
     * @param time of commit.*/
    public Commit(String prev, String msg, ZonedDateTime time) {
        _msg = msg;
        _prev = prev;
        _time = time;
        _second = null;
        _date = (DateTimeFormatter.ofPattern("'Date: 'E "
                + "MMM d HH:mm:ss yyyy Z").format(time));

    }

    /**Constructor if coming from a merge.
     * @param prev commit.
     * @param msg message.
     * @param time time.
     * @param par2 second parent.*/
    public Commit(String prev, String msg, ZonedDateTime time, String par2) {
        _msg = msg;
        _prev = prev;
        _time = time;
        _second = par2;
        _date = (DateTimeFormatter.ofPattern("'Date: 'E "
                + "MMM d HH:mm:ss yyyy Z").format(time));

    }

    /**Serialize the commit.
     * @param commit the commit.
     * @param name of the commit.*/
    public static void serial(Commit commit, String name) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(
                    new FileOutputStream(Gitlet.GITLET_COMMIT + "/" + name));
            objectOut.writeObject(commit);
            objectOut.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**Deseraialize the commit.
     * @param name of commit.
     * @return the commit.*/
    public static Commit deserial(String name) {
        Commit commit = null;
        try {
            ObjectInputStream objectIn = new ObjectInputStream(
                    new FileInputStream(Gitlet.GITLET_COMMIT + "/" + name));
            commit = (Commit) objectIn.readObject();
            objectIn.close();
        } catch (ClassNotFoundException | IOException e) {
            System.out.print("");
            commit = null;
        }
        return commit;

    }

    /**get the sha1 ID from.
     * @param commit Commit
     * @return sha1 ID.*/
    public static String getSHA1(Commit commit) {
        String sha1;
        byte[] byteArray = Utils.serialize(commit);
        sha1 = Utils.sha1(byteArray);
        return sha1;
    }


    /** @return COMMIT from given SHA1*/
    public static Commit getHeadCommitSha(String sha1) {
        return deserial(sha1);
    }

    /**@return given commit MSG.*/
    public String getMsg() {
        return _msg;
    }

    /**@return PREV commit.*/
    public String getPrev() {
        return _prev;
    }

    /** @return BLOBS for given commit.*/
    public HashMap<String, String> getBlobs() {
        return _blobs;
    }

    /** @return SHA1 for given commit.*/
    public String getSha() {
        return _sha1;
    }

    /**@param sha1 to be new SHA1 of a COMMIT.*/
    public void setSha(String sha1) {
        _sha1 = sha1;
    }


}




