package gitlet;


import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Set;

/** CommitTree class for gitlet.
 * Stores the majority of information in terms of entire project.
 * @author hannahgrossman
 */
public class CommitTree implements  Serializable {

    /**my current branch.*/
    private String _currBranch;

    /**head commit.*/
    private String _head;

    /** a set with names of staged files.*/
    private Set<String> _staged = new HashSet<>();

    /**file names not to be staged / committed.*/
    private HashSet<String> _toRemove = new HashSet<>();

    /**removed files in given state.*/
    private HashSet<String> _removed = new HashSet<>();

    /**all the untracked files.*/
    private HashSet<String> _untracked = new HashSet<>();

    /**branches and corresponding head commit.*/
    private TreeMap<String, String> _branches = new TreeMap<>();



    /** seralize a committree.
     * @param tree to be seralized*/
    public static void serial(CommitTree tree) {
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(
                    new FileOutputStream(Gitlet.GITLET_DIR + "tree"));
            objectOut.writeObject(tree);
            objectOut.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }


    /**deseralize a commitTree.
     * @return committree*/
    public static CommitTree deserial() {
        CommitTree tree;
        try {
            ObjectInputStream objectIn = new ObjectInputStream(
                    new FileInputStream(Gitlet.GITLET_DIR + "tree"));
            tree = (CommitTree) objectIn.readObject();
            objectIn.close();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println(e);
            tree = null;
        }
        return tree;

    }

    /** @return the CURRBRANCH. */
    public String getCurrBranch() {
        return _currBranch;
    }

    /**@param currBranch as new CURRBRANCH.*/
    public void setCurrBranch(String currBranch) {
        _currBranch = currBranch;
    }

    /**set head commit.
     * @param head SHA1*/
    public void setHead(String head) {
        _head = head;
    }

    /**@return head commit SHA1.*/
    public String getHead() {
        return _head;
    }

    /**@return head commit.*/
    public Commit getHeadCom() {
        return Commit.deserial(_head);
    }

    /**@return files to be removed from staging area.*/
    public HashSet<String> getToRemove() {
        return _toRemove;
    }

    /** @param toRemove the new files for the staging area.*/
    public void setToRemove(HashSet<String> toRemove) {
        _toRemove = toRemove;
    }

    /** @return the staged files.*/
    public Set<String> getStaged() {
        return _staged;
    }

    /** @param filesStaged : the new files to be staged.*/
    public void setStaged(Set<String> filesStaged) {
        _staged = filesStaged;
    }

    /**@return removed files.*/
    public HashSet<String> getRemoved() {
        return _removed;
    }

    /**set removed files.
     * @param removed the files.*/
    public void setRemoved(HashSet<String> removed) {
        _removed = removed;
    }


    /**@return branches.*/
    public TreeMap<String, String> getBranches() {
        return _branches;
    }

    /**set branches.
     * @param branches new branches.*/
    public void setBranches(TreeMap<String, String> branches) {
        _branches = branches;
    }

    /** @return untracked files.*/
    public HashSet<String> getUntracked() {
        return _untracked;
    }

    /**set untracked.
     * @param untracked files.*/
    public void setUntracked(HashSet<String> untracked) {
        _untracked = untracked;
    }



}


