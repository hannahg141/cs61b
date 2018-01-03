import java.util.Iterator;
import java.util.Observable;
/**
 *  @author Josh Hug
 */

public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private boolean[] cycles;
    int cycle;



    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        cycles = new boolean[maze.V()];
        cycle = Integer.MIN_VALUE;
        for (int i = 0; i < cycles.length; i++) {
            cycles[i] = false;
        }


    }

    @Override
    public void solve() {
        announce();
        isCyclic(0);
        for (int i = 0; i < maze.V(); i++) {
            marked[i] = false;
        }

        if (cycle == Integer.MIN_VALUE) {
            return;
        }

        for (int i = edgeTo[cycle]; i != cycle; i = edgeTo[i]) {
            marked[i] = true;
        }

        marked[cycle] = true;
        announce();


        // TODO: Your code here!
    }

    void isCyclic(int s) {
        marked[s] = true;
        cycles[s] = true;
        for (Integer adj : maze.adj(s)) {
            if (cycles[adj] && edgeTo[s] != adj) {
                cycle = adj;
                edgeTo[adj] = s;
                return;
            }
            if (!marked[adj]) {
                edgeTo[adj] = s;
                isCyclic(adj);
            }
        }
        cycles[s] = false;
    }

}

