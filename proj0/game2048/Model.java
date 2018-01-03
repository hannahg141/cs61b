package game2048;

import java.util.Arrays;
import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author hannahgrossman
 */
class Model extends Observable {

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to _board[c][r].  Be careful! This is not the usual 2D matrix
     * numbering, where rows are numbered from the top, and the row
     * number is the *first* index. Rather it works like (x, y) coordinates.
     */

    /** Largest piece value. */
    static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    Model(int size) {
        _board = new Tile[size][size];
        _score = _maxScore = 0;
        _gameOver = false;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there. */
    Tile tile(int col, int row) {
        return _board[col][row];
    }

    /** Return the number of squares on one side of the board. */
    int size() {
        return _board.length;
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current score. */
    int score() {
        return _score;
    }

    /** Return the current maximum game score (updated at end of game). */
    int maxScore() {
        return _maxScore;
    }

    /** Clear the board to empty and reset the score. */
    void clear() {
        _score = 0;
        _gameOver = false;
        for (Tile[] column : _board) {
            Arrays.fill(column, null);
        }
        setChanged();
    }

    /** Add TILE to the board.  There must be no Tile currently at the
     *  same position. */
    void addTile(Tile tile) {
        assert _board[tile.col()][tile.row()] == null;
        _board[tile.col()][tile.row()] = tile;
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board. */
    boolean tilt(Side side) {
        boolean changed;
        changed = false;
        for (int c = 0; c < size(); c++) {
            int rowMerged = 1000;
            //this is an arbitrary number, the row number will NEVER be this large
            for (int r = size() -1; r>=0; r--) {
                Tile current = vtile(c, r, side);
                if (current != null){
                    int count = r +1;
                    while (count<size() && vtile(c, count, side) == null) {
                        count += 1;
                    }
                    if (count < size() && vtile(c, count, side).value() == current.value() && count != rowMerged){
                        setVtile(c, count, side, current);
                        changed = true;
                        rowMerged = count;
                        _score += current.value() * 2;
                        while (count+1<size() && vtile(c, count + 1, side) == null) {
                            count += 1;
                            setVtile(c, count -1, side, current);
                        }
                    }
                    else if (vtile(c, count - 1, side) == null) {
                        setVtile(c, count - 1, side, current);
                        changed = true;
                    }
                }
            }

        }


        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Return the current Tile at (COL, ROW), when sitting with the board
     *  oriented so that SIDE is at the top (farthest) from you. */
    private Tile vtile(int col, int row, Side side) {
        return _board[side.col(col, row, size())][side.row(col, row, size())];
    }

    /** Move TILE to (COL, ROW), merging with any tile already there,
     *  where (COL, ROW) is as seen when sitting with the board oriented
     *  so that SIDE is at the top (farthest) from you. */
    private void setVtile(int col, int row, Side side, Tile tile) {
        int pcol = side.col(col, row, size()),
            prow = side.row(col, row, size());
        if (tile.col() == pcol && tile.row() == prow) {
            return;
        }
        Tile tile1 = vtile(col, row, side);
        _board[tile.col()][tile.row()] = null;

        if (tile1 == null) {
            _board[pcol][prow] = tile.move(pcol, prow);
        } else {
            _board[pcol][prow] = tile.merge(pcol, prow, tile1);
        }
    }

    /** Deternmine whether game is over and update _gameOver and _maxScore
     *  accordingly. */
    private void checkGameOver() {
        int board = size() * size();
        int total = 0;
        for (int c = 0; c < size(); c++) {
            for (int r = 0; r < size(); r ++) {
                if (tile(c, r) != null)
                    total += 1;
                if (tile(c, r) == null)
                    return;
                if (tile(c, r).value() == MAX_PIECE)
                    return;
                else if (c + 1 == size() && r + 1 == size())
                    continue;
                else if (c + 1 != size() && tile(c+1, r) != null) {
                    if (tile(c, r).value() == tile(c + 1, r).value())
                        return;
                }
                else if (r + 1 != size() && tile(c, r+1) != null) {
                    if (tile(c, r).value() == tile(c, r +1).value())
                        return;
                }
                else if (c +1 != size() && r +1 != size()
                        && tile(c, r+1) != null
                        && tile(c +1, r) != null){
                    if (tile(c, r).value() == tile(c, r+1).value() ||
                            tile(c, r).value() == tile(c+1, r).value())
                        return;
                }
                else
                    total += 1;
            }
        }
        if (total == board){
            _gameOver = true;
            _maxScore = _score;
        }
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        out.format("] %d (max: %d)", score(), maxScore());
        return out.toString();
    }

    /** Current contents of the board. */
    private Tile[][] _board;
    /** Current score. */
    private int _score;
    /** Maximum score so far.  Updated when game ends. */
    private int _maxScore;
    /** True iff game is ended. */
    private boolean _gameOver;

}
