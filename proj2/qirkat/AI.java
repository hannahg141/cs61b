package qirkat;


import java.util.ArrayList;

import static qirkat.PieceColor.*;

/** A Player that computes its own moves.
 *  @author hannahgrossman
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 5;
    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    /** Return a legal move for me. Assumes that
     *  board.whoseMove() == myColor and that !board.gameOver(). */
    @Override
    Move myMove() {
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        if (move != null) {
            System.out.println(myColor() + " moves " + move.toString() + ".");
        }
        return move;
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (myColor() == WHITE) {
            findMove(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            findMove(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        Move best;
        best = null;
        int good = 0;
        if (depth == 0) {
            return staticScore(board);
        }
        ArrayList<Move> moves = board.getMoves();
        Board newBoard = new Board();
        if (sense == -1) {
            good = INFTY;
            for (Move mov : moves) {
                newBoard.copy(board);
                newBoard.makeMove(mov);
                int val = findMove(newBoard, depth - 1,
                        !saveMove, -sense, alpha, beta);
                if (val <= good) {
                    good = val;
                    best = mov;
                }
                if (val <= beta) {
                    beta = val;
                }
                if (good <= beta) {
                    beta = good;
                }
                if (beta <= alpha) {
                    break;
                }
            }

        } else if (sense == 1) {
            good = -INFTY;
            for (Move mov : moves) {
                newBoard.copy(board);
                newBoard.makeMove(mov);
                int val = findMove(newBoard, depth - 1,
                        !saveMove, -sense, alpha, beta);
                if (val >= good) {
                    good = val;
                    best = mov;
                }
                if (val >= alpha) {
                    alpha = val;
                }
                if (good >= alpha) {
                    alpha = good;
                }
                if (beta <= alpha) {
                    break;
                }
            }
        }

        if (saveMove) {
            _lastFoundMove = best;
        }
        return good;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        if (board.gameOver()) {
            if (board.whoseMove().equals(BLACK)) {
                return -WINNING_VALUE;
            } else {
                return WINNING_VALUE;
            }
        }
        int white = 0;
        int black = 0;
        for (int i = 0; i <= MAX_DEPTH; i++) {
            if (board.get(i).equals(BLACK)) {
                if (_lastFoundMove != null && _lastFoundMove.isJump()) {
                    if (_lastFoundMove.jumpTail() != null) {
                        black += 5 * 3 * 10;
                    }
                    black += 6 * 5;
                }
                if (board.enemyBase(i)) {
                    black += 3 * 10 + 5;
                    white -= 5;
                }
                if (board.getAscii(Move.row(i)) < 4) {
                    black += 7;
                }
                if (board.getAscii(Move.row(i)) < 3) {
                    black += 15;
                }
                black++;
            } else {
                if (_lastFoundMove != null && _lastFoundMove.isJump()) {
                    if (_lastFoundMove.jumpTail() != null) {
                        white += 5 * 3 * 10;
                    }
                    white += 6 * 5;
                }
                if (board.enemyBase(i)) {
                    white += 3 * 10 + 5;
                    black -= 5;
                }
                if (board.getAscii(Move.row(i)) > 2) {
                    white += 7;
                }
                if (board.getAscii(Move.row(i)) > 3) {
                    white += 15;
                }
                white++;
            }
        }

        return white - black;
    }
}

