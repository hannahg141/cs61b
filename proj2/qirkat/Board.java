package qirkat;

import java.util.Iterator;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;


import static qirkat.PieceColor.*;
import static qirkat.Move.*;

/** A Qirkat board.   The squares are labeled by column (a char value between
 *  'a' and 'e') and row (a char value between '1' and '5'.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (with row 0 being the bottom row)
 *  counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author hannahgrossman
 */
class Board extends Observable {

    /**stack of all boards.*/
    private Stack<PieceColor[]> stack;
    /** the board variable to be used throught the game.*/
    private PieceColor[] _board;


    /**Keep track of occupied locations to not move backwards.*/
    private HashMap<Integer, ArrayList<Integer>> occupied =
            new HashMap<>();

    /** A new, cleared board at the start of the game. */
    Board() {
        _board = new PieceColor[MAX_INDEX + 1];
        stack = new Stack<>();
        clear();
    }

    /** A copy of B. */
    Board(Board b) {
        internalCopy(b);
    }

    /** Return a constant view of me (allows any access method, but no
     *  method that modifies it). */
    Board constantView() {
        return this.new ConstantBoard();
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions. */
    void clear() {
        _whoseMove = WHITE;
        _gameOver = false;


        for (int i = 0; i <= MAX_INDEX; i++) {
            if (i <= 9 || i == 13 || i == 14) {
                _board[i] = PieceColor.WHITE;
                occupied.put(i, new ArrayList<>());
            }
            if (i >= 15 || i == 10 || i == 11) {
                _board[i] = PieceColor.BLACK;
                occupied.put(i, new ArrayList<>());
            }
            if (i == 12) {
                _board[i] = PieceColor.EMPTY;
                occupied.put(i, new ArrayList<>());
            }

        }

        setChanged();
        notifyObservers();
    }

    /** Copy B into me. */
    void copy(Board b) {
        internalCopy(b);
    }

    @Override
    public boolean equals(Object obj) {
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (this._board[i] != _board[i]) {
                return false;
            }
        }
        if (this._gameOver != _gameOver) {
            return false;
        }
        if (this.stack != stack) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        this._board = b._board.clone();
        this._gameOver = b._gameOver;
        this._whoseMove = b._whoseMove;
        this.occupied = b.occupied;

    }

    /** Set my contents as defined by STR.  STR consists of 25 characters,
     *  each of which is b, w, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board in row-major order, starting
     *  with the bottom row (row 1) and left column (column a). All squares
     *  are initialized to allow horizontal movement in either direction.
     *  NEXTMOVE indicates whose move it is.
     */
    void setPieces(String str, PieceColor nextMove) {
        if (nextMove == EMPTY || nextMove == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }

        for (int i = 0; i <= MAX_INDEX; i++) {
            occupied.put(i, new ArrayList<Integer>());
        }

        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
            case '-':
                set(k, EMPTY);
                break;
            case 'b': case 'B':
                set(k, BLACK);
                break;
            case 'w': case 'W':
                set(k, WHITE);
                break;
            default:
                break;
            }
        }
        _gameOver = false;
        _whoseMove = nextMove;

        setChanged();
        notifyObservers();
    }

    /** Return true iff the game is over: i.e., if the current player has
     *  no moves. */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current contents of square C R, where 'a' <= C <= 'e',
     *  and '1' <= R <= '5'.  */
    PieceColor get(char c, char r) {
        assert validSquare(c, r);
        return get(index(c, r));
    }

    /** Return the current contents of the square at linearized index K. */
    PieceColor get(int k) {
        assert validSquare(k);
        return _board[k];
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'e', and
     *  '1' <= R <= '5'. */
    private void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);
    }

    /** Set get(K) to V, where K is the linearized index of a square. */
    private void set(int k, PieceColor v) {
        assert validSquare(k);
        this._board[k] = v;
    }

    /** Return true iff MOV is legal on the current board. */
    boolean legalMove(Move mov) {
        if (gameOver() || !checkToMove(mov)) {
            return false;
        }
        if (!mov.isJump() && hasBeen(mov)) {
            return false;
        }
        if (enemyBase(index(mov.col0(), mov.row0())) && !mov.isJump()) {
            return false;
        }
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (get(i).equals(_whoseMove)) {
                if (jumpPossible(i) && jumpPossible(mov.col0(), mov.row0())) {
                    if (!get(mov.jumpedCol(),
                            mov.jumpedRow()).equals(_whoseMove.opposite())) {
                        return false;
                    }
                    if (!mov.isJump()) {
                        return false;
                    }
                    if (mov.isJump() && checkJump(mov, true)) {
                        if ((Math.abs(getAscii(mov.col0())
                                - getAscii(mov.col1())) > 2)
                                || (Math.abs(getAscii(mov.row0())
                                - getAscii(mov.row1())) > 2)) {
                            return false;
                        }
                        if ((!mov.isDiagonal(mov.col0(), mov.row0())
                                && mov.isDiagonal(mov.col1(), mov.row1()))
                                || (mov.isDiagonal(mov.col0(), mov.row0())
                                && !mov.isDiagonal(mov.col1(), mov.row1()))) {
                            return false;
                        }
                        return true;
                    }
                } else if (jumpPossible(i)
                        && !jumpPossible(mov.col0(), mov.row0())) {
                    return false;
                }
            }
        }
        if (!diagonalism(mov)) {
            return false;
        }
        if (get(mov.fromIndex()) != (_whoseMove)) {
            return false;
        }
        if (!mov.isJump() && Math.abs(getAscii(mov.row0())
                - getAscii(mov.row1())) > 1) {
            return false;
        }
        if (Math.abs(getAscii(mov.col0()) - getAscii(mov.col1())) > 1) {
            return false;
        }
        if (!mov.isJump() && hasBeen(mov)) {
            return false;
        }
        return true;
    }


    /**Do the diagonal moves match.
     * @param mov the move to make
     * @return
     */
    boolean diagonalism(Move mov) {
        if (mov.col0() != mov.col1()) {
            if (index(mov.col0(), mov.row0()) % 2 == 0) {
                if (mov.row0() != mov.row1()) {
                    if (Math.abs(getAscii(mov.col0()) - getAscii(mov.col1()))
                            != Math.abs(getAscii(mov.row0())
                            - getAscii(mov.row1()))) {
                        return false;
                    }

                }
            }
        }
        return true;
    }

    /**Have I occupied this spot before.
     * @param mov that I make
     * @return
     */
    boolean hasBeen(Move mov) {
        if (occupied == null || occupied.size() == 0) {
            return false;
        }
        return occupied.get(mov.fromIndex()).contains(mov.toIndex());
    }
    /**turn my Characters (columns) into ints.
     * @param letter which is the char
     * @return *the int version
     */
    int getAscii(char letter) {
        if (letter == 'a') {
            return 1;
        } else if (letter == 'b') {
            return 2;
        } else if (letter == 'c') {
            return 3;
        } else if (letter == 'd') {
            return 4;
        } else if (letter == 'e') {
            return 5;
        } else if (letter == '1') {
            return 1;
        } else if (letter == '2') {
            return 2;
        } else if (letter == '3') {
            return 3;
        } else if (letter == '4') {
            return 4;
        } else {
            return 5;
        }
    }

    /** Return a list of all legal moves from the current position. */
    ArrayList<Move> getMoves() {
        ArrayList<Move> result = new ArrayList<>();
        getMoves(result);
        return result;
    }

    /** Add all legal moves from the current position to MOVES. */
    void getMoves(ArrayList<Move> moves) {
        if (gameOver()) {
            return;
        }
        if (jumpPossible()) {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                if (get(k).equals(_whoseMove)) {
                    getJumps(moves, k);
                }

            }
        } else {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                if (get(k).equals(_whoseMove)) {
                    getMoves(moves, k);
                }
            }
        }
    }

    /** Add all legal non-capturing moves from the position
     *  with linearized index K to MOVES. */
    private void getMoves(ArrayList<Move> moves, int k) {
        ArrayList<Move> tempMoves = new ArrayList<>();
        if (k < 15 && get(k).equals(WHITE)) {
            Move up = move(Move.col(k), Move.row(k),
                    Move.col(k), Move.row(k + 5));
            tempMoves.add(up);
        }
        if (k > 4 && get(k).equals(BLACK)) {
            Move down = move(Move.col(k), Move.row(k),
                    Move.col(k), Move.row(k - 5));
            tempMoves.add(down);
        }
        if (col(k) < 'e') {
            Move right = move(Move.col(k), Move.row(k),
                    Move.col(k + 1), Move.row(k));
            tempMoves.add(right);
            if (k < (2 * 10)) {
                Move upRightDiag = move(Move.col(k),
                        Move.row(k), Move.col(k + 1), Move.row(k + 6));
                if (upRightDiag.isDiagonal(k) && get(k).equals(WHITE)) {
                    tempMoves.add(upRightDiag);
                }
            }
            if (k > 4) {
                Move downRightDiag = move(Move.col(k),
                        Move.row(k), Move.col(k + 1), Move.row(k - 4));
                if (downRightDiag.isDiagonal(k) && get(k).equals(BLACK)) {
                    tempMoves.add(downRightDiag);
                }
            }
        }
        if (col(k) > 'a') {
            Move left = move(Move.col(k), Move.row(k),
                    Move.col(k - 1), Move.row(k));
            tempMoves.add(left);
            if (k < (2 * 10)) {
                Move upLeftDiag = move(Move.col(k),
                        Move.row(k), Move.col(k - 1), Move.row(k + 4));
                if (upLeftDiag.isDiagonal(k) && get(k).equals(WHITE)) {
                    tempMoves.add(upLeftDiag);
                }
            }
            if (k > 4) {
                Move downLeftDiag = move(Move.col(k),
                        Move.row(k), Move.col(k - 1), Move.row(k - 6));
                if (downLeftDiag.isDiagonal(k) && get(k).equals(BLACK)) {
                    tempMoves.add(downLeftDiag);
                }
            }
        }
        for (Move mov : tempMoves) {
            if (legalMove(mov)) {
                moves.add(mov);
            }
        }
    }

    /**Am BLACK at the bottom row, or WHITE at top.
     * @param k linearized index
     * @return
     */
    boolean enemyBase(int k) {
        if (get(k).equals(WHITE) && getAscii(Move.row(k)) == 5) {
            return true;
        } else if (get(k).equals(BLACK) && getAscii(Move.row(k)) == 1) {
            return true;
        }
        return false;
    }

    /** Add all legal captures from the position with linearized index K
     *  to MOVES. */
    private void getJumps(ArrayList<Move> moves, int k) {
        for (int i = 0; i <= MAX_INDEX; i++) {
            Move myMove = move(Move.col(k),
                    Move.row(k), Move.col(i), Move.row(i));
            if (get(k).equals(_whoseMove) && legalMove(myMove)
                    && get(myMove.jumpedCol(),
                    myMove.jumpedRow()).equals(_whoseMove.opposite())) {
                if (i != k && Math.abs(myMove.col0() - myMove.col1()) < 3
                        && Math.abs(myMove.row0() - myMove.row1()) < 3) {
                    moves.add(myMove);

                }
            }
        }
        if (moves.size() > 0) {
            moves = helperJumps(moves);
        }

    }

    /** Helper function to concatenate Jumptails.
     * @param moves the temp list
     * @return moves */
    private ArrayList<Move> helperJumps(ArrayList<Move> moves) {
        ArrayList<Move> tempMoves = new ArrayList<>(moves);
        moves.clear();
        while (tempMoves.size() != 0) {
            ArrayList<Move> doubles = new ArrayList<>();
            for (Iterator<Move> i = tempMoves.iterator(); i.hasNext(); ) {
                Move initial = i.next();
                Move mov = initial;
                moves.add(initial);
                Board temp = new Board(this);
                miniSet(mov, temp);
                while (mov.jumpTail() != null) {
                    mov = mov.jumpTail();
                    miniSet(mov, temp);
                }
                for (int j = 0; j <= MAX_INDEX; j++) {
                    Move mov2 = move(mov.col1(), mov.row1(),
                            Move.col(j), Move.row(j));
                    if (mov2.isJump() && checkToMove(mov2)
                            && temp.legalMove(mov2)
                            && temp.get(mov2.jumpedCol(),
                            mov2.jumpedRow()).equals(_whoseMove.opposite())) {
                        Move finalMov = Move.move(initial, mov2);
                        doubles.add(finalMov);
                    }
                }
                if (doubles.size() > 0) {
                    moves.remove(initial);
                }
            }
            tempMoves.clear();
            tempMoves = new ArrayList<>(doubles);
        }
        return moves;
    }


    /**set the pieces on my temp board.
     * @param mov that I make to change the pieces
     * @param temp my board
     */
    void miniSet(Move mov, Board temp) {
        temp.set(mov.col0(), mov.row0(), PieceColor.EMPTY);
        temp.set(mov.jumpedCol(), mov.jumpedRow(), PieceColor.EMPTY);
        temp.set(mov.col1(), mov.row1(), _whoseMove);
    }

    /** is that a valid place to move? ie: null.
     * @param move the inputted move
     * @return if i can move there.
     */
    boolean checkToMove(Move move) {
        return get(move.col1(), move.row1()).equals(EMPTY);
    }

    /** Return true iff MOV is a valid jump sequence on the current board.
     *  MOV must be a jump or null.  If ALLOWPARTIAL, allow jumps that
     *  could be continued and are valid as far as they go.  */
    boolean checkJump(Move mov, boolean allowPartial) {
        if (mov == null) {
            return true;
        }
        if (mov.jumpTail() != null) {
            allowPartial = true;
        }
        if (mov.col0() != mov.col1()
                && index(mov.col0(), mov.row0()) % 2 == 1
                && mov.row0() != mov.row1()) {
            return false;
        }
        if (mov.col1() < 'a' || mov.col1() > 'e'
                || mov.row1() < '1' || mov.row1() > '5') {
            return false;
        }
        Board currBoard = new Board(this);
        Move move = mov;
        while (move != null) {
            if (this.checkToMove(move)) {
                char newCol = move.jumpedCol();
                char newRow = move.jumpedRow();
                if (get(newCol, newRow).equals(_whoseMove.opposite())) {
                    miniSet(move, currBoard);
                    move = move.jumpTail();
                    if (move == null) {
                        return true;
                    }
                } else {
                    return false;

                }
            } else {
                return false;
            }
        }

        return true;

    }


    /** Return true iff a jump is possible for a piece at position C R. */
    boolean jumpPossible(char c, char r) {

        return jumpPossible(index(c, r));
    }

    /** Return true iff a jump is possible for a piece at position with
     *  linearized index K. */
    boolean jumpPossible(int k) {
        ArrayList<Move> tempMoves = new ArrayList<>();
        char c = Move.col(k); char r = Move.row(k);
        if (k < 15) {
            Move up = move(c, r, Move.col(k), Move.row(k + 10));
            tempMoves.add(up);
        }
        if (k > 9) {
            Move down = move(c, r, Move.col(k), Move.row(k - 10));
            tempMoves.add(down);
        }
        if (Move.col(k) < 'd') {
            Move right = move(c, r, Move.col(k + 2), Move.row(k));
            tempMoves.add(right);
            if (k < 15) {
                Move upRightDiag = move(c, r,
                        Move.col(k + 2), Move.row(k + 12));
                if (upRightDiag.isDiagonal(k)) {
                    tempMoves.add(upRightDiag);
                }
            }
            if (k > 9) {
                Move downRightDiag = move(c, r,
                        Move.col(k + 2), Move.row(k - 8));
                if (downRightDiag.isDiagonal(k)) {
                    tempMoves.add(downRightDiag);
                }
            }
        }
        if (Move.col(k) > 'b') {
            Move left = move(c, r, Move.col(k - 2), Move.row(k));
            tempMoves.add(left);
            if (k < 15) {
                Move upLeftDiag = move(c, r, Move.col(k - 2), Move.row(k + 8));
                if (upLeftDiag.isDiagonal(k)) {
                    tempMoves.add(upLeftDiag);
                }
            }
            if (k > 9) {
                Move downLeftDiag = move(c, r,
                        Move.col(k - 2), Move.row(k - 12));
                if (downLeftDiag.isDiagonal(k)) {
                    tempMoves.add(downLeftDiag);
                }
            }
        }
        for (Move mov : tempMoves) {
            Move next = mov.jumpTail();
            if (next != null) {
                if (checkJump(mov, true) && get(mov.jumpedCol(),
                        mov.jumpedRow()).equals(_whoseMove.opposite())) {
                    return true;
                }
            } else if (checkJump(mov, false) && get(mov.jumpedCol(),
                    mov.jumpedRow()).equals(_whoseMove.opposite())) {
                return true;
            }
        }
        return false;
    }

    /** Return true iff a jump is possible from the current board. */
    boolean jumpPossible() {
        for (int k = 0; k <= MAX_INDEX; k += 1) {
            if (get(k).equals(_whoseMove) && jumpPossible(k)) {
                return true;
            }
        }
        return false;
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Perform the move C0R0-C1R1, or pass if C0 is '-'.  For moves
     *  other than pass, assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        makeMove(Move.move(c0, r0, c1, r1, null));
    }

    /** Make the multi-jump C0 R0-C1 R1..., where NEXT is C1R1....
     *  Assumes the result is legal. */
    void makeMove(char c0, char r0, char c1, char r1, Move next) {
        makeMove(Move.move(c0, r0, c1, r1, next));
    }

    /** Make the Move MOV on this Board, assuming it is legal. */
    void makeMove(Move mov) {
        try {
            assert legalMove(mov);
            while (mov != null) {
                set(mov.col0(), mov.row0(), PieceColor.EMPTY);
                set(mov.col1(), mov.row1(), _whoseMove);
                if (mov.isJump()) {
                    set(mov.jumpedCol(), mov.jumpedRow(), PieceColor.EMPTY);
                    occupied.put(mov.toIndex(), new ArrayList<>());
                    occupied.put(mov.fromIndex(), new ArrayList<>());
                } else {
                    ArrayList<Integer> oldValue = occupied.get(mov.fromIndex());
                    oldValue.add(mov.fromIndex());
                    occupied.put(mov.fromIndex(), new ArrayList<>());
                    occupied.put(mov.toIndex(), oldValue);
                }
                mov = mov.jumpTail();
            }
            PieceColor[] next = new PieceColor[MAX_INDEX + 1];
            for (int i = 0; i < next.length; i++) {
                next[i] = _board[i];
            }

            stack.add(next);
            _whoseMove = _whoseMove.opposite();
            setChanged();
            notifyObservers();

        } catch (AssertionError e) {
            System.out.print("");
        }
        if (!isMove() && getMoves().size() == 0) {
            _gameOver = true;
        }

    }


    /** Undo the last move, if any. */
    void undo() {

        if (stack.size() > 1) {
            stack.pop();
            PieceColor[] newBoard = stack.pop();
            clear();
            for (int i = 0; i < newBoard.length; i++) {
                _board[i] = newBoard[i];
            }
            stack.add(newBoard);

        }
        if (stack.size() == 1) {
            stack.pop();
            clear();
        }
        _whoseMove = _whoseMove.opposite();

        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {

        return toString(false);
    }

    /** Return a text depiction of the board.  If LEGEND, supply row and
     *  column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();
        for (char j = '5'; j >= '1'; j--) {
            out.format("%s", "  ");
            if (legend) {
                out.format("%s", j);
            }

            for (char i = 'a'; i <= 'e'; i++) {
                out.format("%s", get(i, j).shortName());
                if (i != 'e') {
                    out.format("%s", " ");
                }
                if (i == 'e' && j != '1') {
                    out.format("%n");
                }
            }
        }

        if (legend) {
            for (char k = 'a'; k <= 'e'; k++) {
                out.format("%s", Character.toString(k));
                if (k != 'e') {
                    out.format("%s", " ");
                }
            }
        }

        return out.toString();
    }

    /** Return true iff there is a move for the current player. */
    private boolean isMove() {

        return getMoves().size() != 0;
    }


    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Set true when game ends. */
    private boolean _gameOver;

    /** Convenience value giving values of pieces at each ordinal position. */
    static final PieceColor[] PIECE_VALUES = PieceColor.values();

    /** One cannot create arrays of ArrayList<Move>, so we introduce
     *  a specialized private list type for this purpose. */
    private static class MoveList extends ArrayList<Move> {
    }

    /** A read-only view of a Board. */
    private class ConstantBoard extends Board implements Observer {
        /** A constant view of this Board. */
        ConstantBoard() {
            super(Board.this);
            Board.this.addObserver(this);
        }

        @Override
        void copy(Board b) {
            assert false;
        }

        @Override
        void clear() {
            assert false;
        }

        @Override
        void makeMove(Move move) {
            assert false;
        }

        /** Undo the last move. */
        @Override
        void undo() {
            assert false;
        }

        @Override
        public void update(Observable obs, Object arg) {
            super.copy((Board) obs);
            setChanged();
            notifyObservers(arg);
        }
    }
}
