

/** A partition of a set of contiguous integers that allows (a) finding whether
 *  two integers are in the same partition set and (b) replacing two partitions
 *  with their union.  At any given time, for a structure partitioning
 *  the integers 1-N, each partition is represented by a unique member of that
 *  partition, called its representative. kruskal's algorithm.
 *  @author hannahgrossman
 */
public class UnionFind {

    /**parent arrows of the graph.*/
    private int[] prev;


    /** the size array*/
    private int[] sizes;
    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        prev = new int[N + 1];
        sizes = new int[N + 1];
        for (int i = 0; i < N + 1; i++) {
            prev[i] = i;
            sizes[i] = 1;

        }
    }

    /** Return the representative of the partition currently containing V.
     *  Assumes V is contained in one of the partitions.  */
    public int find(int v) {
        while (v != prev[v]) {
            prev[v] = prev[prev[v]];
            sizes[prev[v]]++;
            v = prev[v];
        }
        return v;
    }

    /** Return true iff U and V are in the same partition. */
    public boolean samePartition(int u, int v) {

        return find(u) == find(v);
    }

    /** Union U and V into a single partition, returning its representative. */
    public int union(int u, int v) {
        int uPrev = find(u);
        int vPrev = find(v);
        prev[uPrev] = vPrev;

        if (sizes[uPrev] > sizes[vPrev]) {
            prev[vPrev] = uPrev;
            return uPrev;
        } else {
            prev[uPrev] = vPrev;
            return vPrev;
        }
    }
}
