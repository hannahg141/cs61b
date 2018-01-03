import java.util.Observable;
import java.util.PriorityQueue;
import java.util.Queue;
/**
 *  @author Josh Hug
 */

public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int _source;
    private int _target;
    private boolean _targetFound = false;
    private Queue<Integer> _fringe;


    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        _source = maze.xyTo1D(sourceX, sourceY);
        _target = maze.xyTo1D(targetX, targetY);
        distTo[_source] = 0;
        edgeTo[_source] = _source;
        _fringe = new PriorityQueue<>();
        // Add more variables here!
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        announce();
        if (_source == _target) {
            _targetFound = true;
            return;
        }
        _fringe.add(_source);
        while (!_fringe.isEmpty()) {
            int curr = _fringe.remove();
            marked[curr] = true;
            if (curr == _target) {
                _targetFound = true;
                return;
            }
            for (Integer adj : maze.adj(curr)) {
                if (!marked[adj]) {
                    edgeTo[adj] = curr;
                    _fringe.add(adj);
                    announce();
                }
                distTo[adj] = distTo[curr]++;
            }
        }

        // TODO: Your code here. Don't forget to update distTo, edgeTo, and marked, as well as call announce()

    }


    @Override
    public void solve() {
        bfs();
    }
}

