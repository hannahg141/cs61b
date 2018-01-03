import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a BST based String Set.
 * @author hannahgrossman
 */
public class BSTStringSet implements StringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        root = null;
    }

    @Override
    public void put(String s) {
        root = putHelper(s, root);

    }

    /**helper.
     * @param s my string
     * @param tree the tree
     * @return the node i made
     */
    public Node putHelper(String s, Node tree) {
        if (tree == null) {
            return new Node(s);
        }
        if (s.compareTo(tree.s) > 0) {
            tree.right = putHelper(s, tree.right);
        } else if (s.compareTo(tree.s) < 0) {
            tree.left = putHelper(s, tree.left);
        }
        return tree;
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(s, root);
    }

    /**helper so it works.
     *@param s the string
     * @param tree the node
     * @return if its in the tree.
     */
    public boolean containsHelper(String s, Node tree) {
        if (tree == null) {
            return false;
        }

        if (s.compareTo(tree.s) > 0) {
            return containsHelper(s, tree.right);
        }
        if (s.compareTo(tree.s) < 0) {
            return containsHelper(s, tree.left);
        }
        return true;
    }

    /**the list I make.
     */
    public List<String> myTree = new ArrayList<String>();
    @Override
    public List<String> asList() {
        if (root != null) {
            myTree.add(root.s);
        }

        return asListHelper(root);
    }

    /**helper so it works.
     * @param tree the tree
     * @return myTree
     */
    public List<String> asListHelper(Node tree) {
        if (tree.left != null) {
            myTree.add(tree.left.s);
        }

        if (tree.right != null) {
            myTree.add(tree.right.s);
        }

        asListHelper(tree.left);
        asListHelper(tree.right);

        return myTree;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        public Node(String sp) {
            s = sp;
        }
    }

    /** Root node of the tree. */
    private Node root;
}
