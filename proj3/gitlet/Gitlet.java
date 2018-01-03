package gitlet;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.TreeSet;
import java.nio.file.StandardCopyOption;
import java.time.ZonedDateTime;
import java.time.ZoneId;


/** Gitlet class.
 * All commands.
 * @author hannahgrossman
 */
public class Gitlet {

    /** working directory.*/
    static final String USER_DIR = System.getProperty("user.dir");
    /**the gitlet directory.*/
    static final String GITLET_DIR = ".gitlet/";
    /** staging directory.*/
    static final String GITLET_STAGING = ".gitlet/staging/";
    /** commit directory. */
    static final String GITLET_COMMIT = ".gitlet/commit/";
    /** blobs directory.*/
    static final String GITLET_BLOBS = ".gitlet/blobs/";
    /**my beautiful wonderful COMMITTREE.*/
    private static CommitTree currTree = null;
    /** the year of first commit.*/
    static final int YEAR = 1970;
    /** all the zeros.*/
    static final int ZERO = 00;
    /**all the ones.*/
    static final int ONE = 1;


    /**Initialize a Gitlet commit.*/
    public static void init() {
        File file = new File(USER_DIR + "/" + GITLET_DIR);
        if (!file.mkdir()) {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the current directory.");
            System.exit(0);
        } else {
            file.mkdir();
            Commit initialCommit = new Commit(null, "initial commit",
                    ZonedDateTime.of(YEAR, ONE, ONE, ZERO, ZERO,
                            ZERO, ZERO, ZoneId.of("America/Los_Angeles")));
            initialCommit.setSha(Commit.getSHA1(initialCommit));
            currTree = new CommitTree();
            File commits = new File(GITLET_COMMIT);
            File staging = new File(GITLET_STAGING);
            File blobs = new File(GITLET_BLOBS);
            commits.mkdir();
            staging.mkdir();
            blobs.mkdir();
            currTree.setHead(initialCommit.getSha());
            currTree.getBranches().put("master", currTree.getHead());
            currTree.setCurrBranch("master");
            Commit.serial(initialCommit, currTree.getHead());
        }

    }

    /** add files to be staged.
     * @param toAdd file to add to staged.
     * @param tree currtree*/
    public static void add(String toAdd, CommitTree tree) {
        currTree = tree;
        File file = new File(toAdd);
        if (!file.isFile()) {
            System.out.println("File does not exist.");
            System.exit(0);
        }
        if (changed(toAdd, file)) {
            if (currTree.getRemoved().contains(toAdd)) {
                currTree.getRemoved().remove(toAdd);
            }
            currTree.getStaged().add(toAdd);

            try {
                Path p1 = Paths.get(toAdd);
                Path p2 = Paths.get(GITLET_STAGING + toAdd);
                Files.copy(p1, p2, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                System.out.println(e);
            }
        } else if (currTree.getRemoved().contains(toAdd)) {
            currTree.getToRemove().remove(toAdd);
            currTree.getRemoved().remove(toAdd);
        }
    }


    /**@param name of file potentially changed.
     * @param file file
     * @return if the file is different or not.*/
    public static boolean changed(String name, File file) {
        Commit commit = currTree.getHeadCom();
        String sha1 = Utils.sha1(Utils.readContents(file), name);
        if (commit.getBlobs().containsKey(name)) {
            if (commit.getBlobs().get(name).equals(sha1)) {
                return false;
            } else {
                currTree.getToRemove().add(name);
                return true;
            }
        }
        return true;
    }

    /**displays information about all commits ever made.
     * @param tree  currtree*/
    public static void log(CommitTree tree) {
        currTree = tree;
        Commit commit = currTree.getHeadCom();
        String sha1 = currTree.getHead();
        while (commit.getPrev() != null) {
            commit.print();
            commit = Commit.getHeadCommitSha(commit.getPrev());
            sha1 = commit.getPrev();
        }
        commit.print();
    }

    /**print all commits.
     * * @param tree  currtree*/
    public static void globalLog(CommitTree tree) {
        currTree = tree;
        HashSet<String> done = new HashSet<>();
        File[] files = new File(GITLET_COMMIT).listFiles();
        for (File f : files) {
            String sha1 = f.getName();
            Commit commit = Commit.getHeadCommitSha(sha1);
            commit.print();

        }
    }


    /**add a new COMMMIT if valid MSG.
     * @param toCommit the commit message.
     * Stage the files in the commit, add modified files from REMOVED to blobs.
     * @param tree currtree*/
    public static void commit(String toCommit, CommitTree tree) {
        currTree = tree;
        if ((currTree.getToRemove().size() == 0
                && currTree.getStaged().size() == 0)) {
            System.out.println("No changes added to the commit");
            System.exit(0);

        }
        Commit commit = new Commit(currTree.getHead(),
                toCommit, ZonedDateTime.now());
        commit.setSha(Commit.getSHA1(commit));
        for (String snapshot : currTree.getStaged()) {
            File file = new File(GITLET_STAGING + snapshot);
            String sha1 = Utils.sha1(Utils.readContents(file), snapshot);
            commit.getBlobs().put(snapshot, sha1);
            File destination = new File(USER_DIR + "/" + GITLET_BLOBS + sha1);
            if (file.isFile()) {
                if (destination.exists()) {
                    destination.delete();
                }
                try {
                    Files.copy(Paths.get(GITLET_STAGING + snapshot),
                            Paths.get(USER_DIR + "/" + GITLET_BLOBS + sha1));
                } catch (IOException e) {
                    System.out.println(e);
                    return;
                }
            }
            try {
                Files.delete(Paths.get(GITLET_STAGING + snapshot));
            } catch (IOException e) {
                System.out.println(e);
                return;
            }
        }
        Commit oldhead = currTree.getHeadCom();
        currTree.setHead(commit.getSha());
        for (String code : oldhead.getBlobs().keySet()) {
            if (!currTree.getToRemove().contains(code)
                    || !currTree.getRemoved().contains(code)) {
                File file = new File(code);
                if (file.isFile()) {
                    String sha1 = Utils.sha1(Utils.readContents(file), code);
                    commit.getBlobs().put(code, sha1);
                }
            }
        }
        currTree.getBranches().put(currTree.getCurrBranch(),
                currTree.getHead());
        currTree.setStaged(new TreeSet<>());
        currTree.setRemoved(new HashSet<>());
        currTree.setToRemove(new HashSet<>());
        Commit.serial(commit, currTree.getHead());
    }

    /**update some stuff. method was too long.
     * @param dest location
     * @param snap file
     * @param sha file sha*/
    public static void destinations(File dest, String snap, String sha) {
        if (dest.exists()) {
            dest.delete();
        }
        try {
            Files.copy(Paths.get(GITLET_STAGING + snap),
                    Paths.get(USER_DIR + "/" + GITLET_BLOBS + sha));
        } catch (IOException e) {
            System.out.println(e);
            return;
        }

    }

    /**@param toCommit message.
     * @param tree currtree
     * @param secondPar the old head sha.
     * @param oldHead of curr branch
     * @param badHead of merge with
     * @param conflicts bad head sha.
     * @param currBranch the current branch*/
    public static void mergeCommit(String toCommit, CommitTree tree,
                                   String secondPar, Commit oldHead,
                                   Commit badHead, HashSet<String> conflicts,
                                   String currBranch) {
        currTree = tree;
        if ((currTree.getToRemove().size() == 0
                && currTree.getStaged().size() == 0)) {
            System.out.println("No changes added to the commit");
            System.exit(0);
        }
        Commit commit = new Commit(oldHead.getSha(),
                toCommit, ZonedDateTime.now(), secondPar);
        commit.setSha(Commit.getSHA1(commit));
        for (String snapshot : currTree.getStaged()) {
            File file = new File(GITLET_STAGING + snapshot);
            String sha1 = Utils.sha1(Utils.readContents(file), snapshot);
            commit.getBlobs().put(snapshot, sha1);
            File destination = new File(USER_DIR + "/" + GITLET_BLOBS + sha1);
            if (file.isFile()) {
                destinations(destination, snapshot, sha1);
            }
            try {
                Files.delete(Paths.get(GITLET_STAGING + snapshot));
            } catch (IOException e) {
                System.out.println(e);
                return;
            }
        }
        for (String code : oldHead.getBlobs().keySet()) {
            if (!currTree.getToRemove().contains(code)
                    && !conflicts.contains(code)) {
                File file = new File(GITLET_BLOBS
                        + oldHead.getBlobs().get(code));
                File destination = new File(USER_DIR + "/" + code);
                if (file.isFile()) {
                    if (destination.exists()) {
                        destination.delete();
                    }
                    Utils.writeContents(destination, Utils.readContents(file));
                    commit.getBlobs().put(code,
                            Utils.sha1(Utils.readContents(file), code));
                }
            }
        }
        for (String code : badHead.getBlobs().keySet()) {
            if (!oldHead.getBlobs().containsKey(code)
                    && !currTree.getStaged().contains(code)) {
                File file = new File(code);
                if (file.isFile()) {
                    File location = new File(USER_DIR + "/" + code);
                    if (location.exists()) {
                        location.delete();
                    }
                }
            }
        }
        update(currTree, currBranch, commit);
    }

    /** too long method. heres anothe helper.
     * @param tree curr tree
     * @param cBranch curr branch
     * @param c commit*/
    public static void update(CommitTree tree, String cBranch, Commit c) {
        currTree = tree;
        currTree.setCurrBranch(cBranch);
        currTree.setHead(c.getSha());
        currTree.getBranches().put(currTree.getCurrBranch(),
                currTree.getHead());
        currTree.setStaged(new TreeSet<>());
        currTree.setRemoved(new HashSet<>());
        currTree.setToRemove(new HashSet<>());
        Commit.serial(c, currTree.getHead());
    }

    /**help me checkout.
     * @param commit to checkout.
     * @param name of commit.*/
    public static void helper(Commit commit, String name) {
        if (commit.getBlobs().containsKey(name)) {
            try {
                Files.copy(Paths.get(GITLET_BLOBS
                                + commit.getBlobs().get(name)),
                        Paths.get(name), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("File does not exist in that commit");
            System.exit(0);
        }

    }

    /**reset head to commit with given ID.
     * @param tree currtree
     * @param id the commitID*/
    public static void reset(String id, CommitTree tree) {
        currTree = tree;
        Commit commit = null;
        String sha1 = null;
        Commit headCom = currTree.getHeadCom();
        String branch = null;
        boolean found = false;
        File[] files = new File(GITLET_COMMIT).listFiles();
        for (File f : files) {
            if (found) {
                break;
            }
            sha1 = f.getName();
            commit = Commit.getHeadCommitSha(sha1);
            while (true) {
                if (sha1.length() < id.length()) {
                    System.out.println("No commit with that id exists.");
                    System.exit(0);
                }
                if (sha1.equals(id) || sha1.substring(0,
                        id.length()).equals(id)) {
                    found = true;
                    break;
                }
                sha1 = commit.getPrev();
                if (sha1 == null) {
                    break;
                }
                commit = Commit.getHeadCommitSha(commit.getPrev());
            }
            if (sha1 != null) {
                branch = currTree.getCurrBranch();
                break;
            }
        }
        getUntracked();
        for (String file : commit.getBlobs().keySet()) {
            if (currTree.getUntracked().contains(file)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it or add it first.");
                System.exit(0);
            }
        }
        if (sha1 == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        currTree.setStaged(new HashSet<>());
        currTree.setRemoved(new HashSet<>());
        for (String name : commit.getBlobs().keySet()) {
            helper(commit, name);
        }
        for (String name : headCom.getBlobs().keySet()) {
            if (!commit.getBlobs().containsKey(name)) {
                commit.getBlobs().remove(name);
            }
        }
        currTree.getBranches().put(branch, sha1);
        currTree.setHead(sha1);
    }

    /**remove a file.
     * @param toRemove the file
     * @param tree currtree*/
    public static void rm(String toRemove, CommitTree tree) {
        currTree = tree;
        Commit currHead = currTree.getHeadCom();
        if (currHead.getBlobs().containsKey(toRemove)) {
            try {
                if (currTree.getStaged().contains(toRemove)) {
                    currTree.getStaged().remove(toRemove);
                    Files.delete(Paths.get(GITLET_STAGING + toRemove));
                }
                currTree.getToRemove().add(toRemove);
                currTree.getRemoved().add(toRemove);
                Files.delete(Paths.get(toRemove));

            } catch (IOException e) {
                currTree.getRemoved().add(toRemove);
            }
        } else if (currTree.getStaged().contains(toRemove)) {
            try {
                currTree.getStaged().remove(toRemove);
                Files.delete(Paths.get(GITLET_STAGING + toRemove));
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }

    }


    /**Finds and prints all commit ID's with a given msg.
     * @param msg the commit message
     * @param tree currtree*/
    public static void find(String msg, CommitTree tree) {
        currTree = tree;
        HashSet<String> current = new HashSet<>();
        HashSet<String> done = new HashSet<>();
        File[] files = new File(GITLET_COMMIT).listFiles();
        for (File f : files) {
            String sha1 = f.getName();
            Commit commit = Commit.getHeadCommitSha(sha1);
            while (commit != null && !current.contains(sha1)) {
                if (msg.equals(commit.getMsg())) {
                    System.out.println(sha1);
                    done.add(sha1);
                }
                current.add(sha1);
                sha1 = commit.getPrev();
                if (sha1 != null) {
                    commit = Commit.getHeadCommitSha(commit.getPrev());
                } else {
                    commit = null;
                }
            }
        }

        if (done.size() == 0) {
            System.out.println("Found no commit with that message.");

        }
    }


    /**find the split point of two branches.
     * @param branch1 1
     * @param branch2 2
     * @return split point Commit.*/
    public static Commit findSplit(String branch1, String branch2) {
        String b1Sha = currTree.getBranches().get(branch1);
        String b2sha = currTree.getBranches().get(branch2);
        Commit b1Commit, b2Commit;
        HashSet<String> seen = new HashSet<>();
        while (b1Sha != null) {
            b1Commit = Commit.getHeadCommitSha(b1Sha);
            seen.add(b1Sha);
            b1Sha = b1Commit.getPrev();
        }
        while (b2sha != null) {
            b2Commit = Commit.getHeadCommitSha(b2sha);
            if (seen.contains(b2sha)) {
                return b2Commit;
            } else {
                b2sha = b2Commit.getPrev();
            }
        }
        return null;
    }


    /**merge two branches.
     * @param branch that i want to merge with master.
     * @param tree currTree*/
    public static void merge(String branch, CommitTree tree) {
        currTree = tree;
        if (branch.equals(currTree.getCurrBranch())) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        } else if (!currTree.getBranches().containsKey(branch)) {
            System.out.println("A branch with that name does not exist. ");
            System.exit(0);
        }
        Commit split = findSplit(branch, currTree.getCurrBranch());
        String sha1 = split.getSha();
        String branchSha = currTree.getBranches().get(branch);
        Commit branchHead = Commit.getHeadCommitSha(branchSha);
        Commit head = currTree.getHeadCom();
        String headSha = currTree.getHead();
        String currBranch = currTree.getCurrBranch();
        if (currTree.getStaged().size() != 0
                || currTree.getRemoved().size() != 0) {
            System.out.println("You have uncommitted changes.");
            return;
        } else if (currTree.getBranches().get(branch).equals(sha1)) {
            System.out.println("Given branch is an ancestor "
                    + "of the current branch.");
            return;
        } else {
            getUntracked();
            for (String name : branchHead.getBlobs().keySet()) {
                if (currTree.getUntracked().contains(name)) {
                    System.out.println("There is an untracked file "
                            + "in the way; delete it or add it first.");
                    System.exit(0);
                }
            }
        }
        if (headSha.equals(sha1)) {
            checkout2(branch, currTree);
            currTree.setHead(headSha);
            currTree.setCurrBranch(currBranch);
            System.out.println("Current branch fast-forwarded. ");
            return;
        }
        HashSet<String> conflicts = getConflicts(head, branchHead,
                branchSha, split, branch, currTree);
        for (String conflict : conflicts) {
            merge(conflict, branchHead, head, currTree);
        }
        if (conflicts.size() > 0) {
            System.out.println("Encountered a merge conflict.");
        }
        mergeCommit("Merged " + branch + " into "
                + currBranch + ".", currTree, headSha, head,
                branchHead, conflicts, currBranch);
    }

    /**get conflicts.
     * @param head curr head.
     * @param tree currTree
     * @param branchHead branch head to merge.
     * @param branchSha branch's head's SHA-1.
     * @param split the split point commit.
     * @param givenBranch to merge with.
     * @return all conflicts between head's*/
    public static HashSet<String> getConflicts(Commit head,
                                               Commit branchHead,
                                               String branchSha,
                                               Commit split,
                                               String givenBranch,
                                               CommitTree tree) {
        currTree = tree;
        HashSet<String> conflicts = new HashSet<>();
        for (String id : branchHead.getBlobs().keySet()) {
            if (split.getBlobs().containsKey(id)) {
                if (!split.getBlobs().get(id).equals(branchHead.
                        getBlobs().get(id))
                        && (!head.getBlobs().containsKey(id)
                        || !head.getBlobs().get(id).equals(split.
                        getBlobs().get(id)))) {
                    conflicts.add(id);
                }
            } else if (!split.getBlobs().containsKey(id)) {
                if (!head.getBlobs().containsKey(id)) {
                    checkout2(givenBranch, currTree);
                    currTree.getStaged().add(id);
                    try {
                        Path p1 = Paths.get(id);
                        Path p2 = Paths.get(GITLET_STAGING + id);
                        Files.copy(p1, p2, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println("FUDGE");
                    }
                } else if (head.getBlobs().containsKey(id)) {
                    conflicts.add(id);
                }
            }
        }
        for (String id : head.getBlobs().keySet()) {
            if (split.getBlobs().containsKey(id)) {
                if (!branchHead.getBlobs().containsKey(id)) {
                    if (split.getBlobs().get(id).equals(head.
                            getBlobs().get(id))) {
                        currTree.getToRemove().add(id);
                    } else if (!split.getBlobs().get(id).equals(head.
                            getBlobs().get(id))) {
                        conflicts.add(id);
                    }
                } else if (branchHead.getBlobs().containsKey(id)) {
                    if (!split.getBlobs().get(id).equals(branchHead.
                            getBlobs().get(id))) {
                        conflicts.add(id);
                    }
                }

            } else if (!split.getBlobs().containsKey(id)
                    && branchHead.getBlobs().containsKey(id)
                    && !branchHead.getBlobs().get(id)
                    .equals(head.getBlobs().get(id))) {
                conflicts.add(id);
            }
        }

        return conflicts;
    }


    /**merge two heads.
     * @param conflict the conflicts.
     * @param branchHead branch head
     * @param head curr head
     * @param tree currtree*/
    public static void merge(String conflict, Commit branchHead,
                             Commit head, CommitTree tree) {
        currTree = tree;
        File conflictFile = new File(USER_DIR + "/" + conflict);
        File wanted = new File(GITLET_BLOBS
                + branchHead.getBlobs().get(conflict));
        File currFile = new File(GITLET_BLOBS
                + head.getBlobs().get(conflict));

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] headChange = "<<<<<<< HEAD\n".getBytes();
            out.write(headChange);
            if (currFile.exists()) {
                byte[] curr = Utils.readContents(currFile);
                out.write(curr);
            }
            byte[] middle = "=======\n".getBytes();
            out.write(middle);
            if (wanted.exists()) {
                byte[] want = Utils.readContents(wanted);
                out.write(want);
            }
            byte[] last = ">>>>>>>\n".getBytes();
            out.write(last);
            byte[] toChange = out.toByteArray();
            Utils.writeContents(conflictFile, toChange);
        } catch (IOException e) {
            System.out.println(e);
        }

    }


    /** remove a branch.
     * @param branchName to remove.
     * @param tree currTree.*/
    public static void rmBranch(String branchName, CommitTree tree) {
        currTree = tree;
        if (!currTree.getBranches().containsKey(branchName)) {
            System.out.println(" A branch with that name does not exist.");
            System.exit(0);
        }
        if (currTree.getCurrBranch().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        } else {
            currTree.getBranches().remove(branchName);
        }
    }

    /**add branch.
     * @param branchName to add.
     * @param tree currtree*/
    public static void branch(String branchName, CommitTree tree) {
        currTree = tree;
        if (currTree.getBranches().containsKey(branchName)) {
            System.out.println("branch with that name already exists.");
            System.exit(0);
        } else {
            currTree.getBranches().put(branchName, currTree.getHead());
        }


    }

    /**which checkout method.
     * @param args inputed.
     * @param tree committree*/
    public static void checkout(String[] args, CommitTree tree) {
        if (args.length == 3 && args[1].equals("--")) {
            Gitlet.checkout1(args[2], tree);
        } else if (args.length == 2) {
            Gitlet.checkout2(args[1], tree);
        } else if (args.length == 4 && args[2].equals("--")) {
            Gitlet.checkout3(args[1], args[3], tree);
        } else {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }

    /** puts FILENAME at head of working directory.
     * @param fileName of the file
     * @param tree  currTree*/
    public static void checkout1(String fileName, CommitTree tree) {
        currTree = tree;
        Commit commit = currTree.getHeadCom();
        helper(commit, fileName);

    }


    /**checkout BRANCHNAME.
     * @param branchName to be new head
     * @param tree currTree.*/
    public static void checkout2(String branchName, CommitTree tree) {
        currTree = tree;
        if (!currTree.getBranches().containsKey(branchName)) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        String branchSha1 = currTree.getBranches().get(branchName);
        Commit branchCommit = Commit.getHeadCommitSha(branchSha1);
        if (branchName.equals(currTree.getCurrBranch())) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        } else {
            getUntracked();
            for (String snapshot : branchCommit.getBlobs().keySet()) {
                if (currTree.getUntracked().contains(snapshot)) {
                    System.out.println("There is an untracked "
                            + "file in the way; delete it or add it first.");
                    System.exit(0);
                }
            }
        }
        currTree.setCurrBranch(branchName);
        Commit head = currTree.getHeadCom();
        for (String fileName : head.getBlobs().keySet()) {
            try {
                Files.delete(Paths.get(fileName));
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        currTree.setHead(branchSha1);
        for (String fileName : branchCommit.getBlobs().keySet()) {
            File destination = new File(GITLET_BLOBS
                    + branchCommit.getBlobs().get(fileName));
            if (destination.exists()) {
                try {

                    Files.copy(Paths.get(GITLET_BLOBS
                            + branchCommit.getBlobs().get(fileName)),
                            Paths.get(fileName));
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

        }
        currTree.setStaged(new HashSet<>());
    }

    /**checkout a specific file from given commit.
     * @param commitID we're looking for
     * @param fileName to checkout
     * @param tree the currTree*/
    public static void checkout3(String commitID,
                                 String fileName, CommitTree tree) {
        currTree = tree;
        HashSet<String> done = new HashSet<>();
        File toCheckout = new File(fileName);
        File[] commits = new File(GITLET_COMMIT).listFiles();
        String sha;
        Commit c = null;
        for (File com : commits) {
            sha = com.getName();
            if (sha.equals(commitID)
                    || sha.substring(0, commitID.length()).equals(commitID)) {
                c = Commit.getHeadCommitSha(sha);
                break;
            }
        }
        if (c == null) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }
        if (!c.getBlobs().keySet().contains(fileName)) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        if (c.getBlobs().keySet().contains(fileName)) {
            File[] files = new File(GITLET_BLOBS).listFiles();
            for (File f : files) {
                String name = f.getName();
                String want = c.getBlobs().get(fileName);
                if (name.equals(want)) {
                    try {
                        Files.copy(Paths.get(GITLET_BLOBS + want),
                                Paths.get(USER_DIR + "/" + fileName),
                                StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    return;
                }
            }
        }
    }


    /**display current branches and Files staged & untracked.
     * @param tree  currTree*/
    public static void status(CommitTree tree) {
        currTree = tree;
        System.out.println("=== Branches ===");
        for (String branch : currTree.getBranches().keySet()) {
            StringBuilder print = new StringBuilder("");
            if (branch.equals(currTree.getCurrBranch())) {
                print.append("*" + branch);
            } else {
                print.append(branch);
            }
            System.out.println(print);
        }
        System.out.println("\n=== Staged Files ===");
        for (String file : currTree.getStaged()) {
            System.out.println(file);
        }
        System.out.println("\n=== Removed Files ===");
        for (String file : currTree.getRemoved()) {
            System.out.println(file);
        }
        System.out.println("\n=== Modifications Not Staged For Commit ===");
        TreeSet<String> modified = getModified();
        for (String mod : modified) {
            System.out.println(mod);
        }
        System.out.println("\n=== Untracked Files ===");
        for (String file : currTree.getUntracked()) {
            if (!currTree.getStaged().contains(file)) {
                System.out.println(file);
            }
        }
        System.out.println("");
    }

    /**get all the files that have been modified.
     * @return modified files.*/
    public static TreeSet<String> getModified() {
        TreeSet<String> modified = new TreeSet<>();
        Commit head = currTree.getHeadCom();
        File[] files = new File(USER_DIR).listFiles();
        for (String name : head.getBlobs().keySet()) {
            boolean in = false;
            for (File file : files) {
                if (!file.isDirectory() && !file.isHidden()) {
                    if (file.getName().equals(name)) {
                        in = true;
                        String newSha1 =
                                Utils.sha1(Utils.readContents(file), name);
                        if (!head.getBlobs().get(name).equals(newSha1)
                                && !currTree.getStaged().contains(name)) {
                            modified.add(name + " (modified)");
                        } else if (currTree.getStaged().contains(name)) {
                            File f = new File(GITLET_STAGING + name);
                            String tempsha =
                                    Utils.sha1(Utils.readContents(f), name);
                            if (!tempsha.equals(newSha1)) {
                                modified.add(name + " (modified)");
                            }
                        }
                    }
                }
            }
            if (!in && !currTree.getRemoved().contains(name)
                    && !currTree.getStaged().contains(name)) {
                modified.add(name + " (deleted)");
            }
        }
        return modified;
    }

    /**get all the untracked file.*/
    public static void getUntracked() {
        currTree.setUntracked(new HashSet<>());
        Commit commit = currTree.getHeadCom();
        File file = new File(USER_DIR);
        File[] pathNames = file.listFiles();
        for (File f : pathNames) {
            if ((!f.isDirectory() || !f.isHidden())
                    && (!currTree.getStaged().contains(f.getName())
                    && !commit.getBlobs().containsKey(f.getName()))) {
                currTree.getUntracked().add(f.getName());
            }
        }
    }

    /**start up a round of gitlet.
     * @return deserialized commitTree.*/
    public static CommitTree start() {
        CommitTree tree = null;
        File firstGitlet = new File(GITLET_DIR + "tree");
        if (firstGitlet.exists()) {
            tree = CommitTree.deserial();

        }
        return tree;
    }

    /**end a round of gitlet.*/
    public static void end() {
        CommitTree.serial(currTree);
    }

}
