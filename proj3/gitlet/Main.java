package gitlet;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author hannahgrossman
 *
 *  Collaboration with Emilie Hasselman, Jeff Burr,
 *  Sarah Mowris, Daniel Stephens.
 *  Discussed ideas in Lab with: Michelle Lee, Aiden Wang.
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        CommitTree tree = Gitlet.start();
        if (args == null || args.length == 0) {
            System.out.println("Please enter a command");
            System.exit(0);
        } else if (tree == null && !args[0].equals("init")) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
        switch (args[0]) {
        case "init":
            Gitlet.init();
            break;
        case "add":
            Gitlet.add(args[1], tree);
            break;
        case "commit":
            if (args[1].equals("")) {
                System.out.println("Please enter a commit message.");
                return;
            }
            Gitlet.commit(args[1], tree);
            break;
        case "rm":
            Gitlet.rm(args[1], tree);
            break;
        case "log":
            Gitlet.log(tree);
            break;
        case "global-log":
            Gitlet.globalLog(tree);
            break;
        case "find":
            Gitlet.find(args[1], tree);
            break;
        case "status":
            Gitlet.status(tree);
            break;
        case "checkout":
            Gitlet.checkout(args, tree);
            break;
        case "branch":
            Gitlet.branch(args[1], tree);
            break;
        case "rm-branch":
            Gitlet.rmBranch(args[1], tree);
            break;
        case "reset":
            Gitlet.reset(args[1], tree);
            break;
        case "merge":
            Gitlet.merge(args[1], tree);
            break;
        default:
            System.out.println("No command with that name exists.");
            return;
        }
        Gitlet.end();
    }
}




