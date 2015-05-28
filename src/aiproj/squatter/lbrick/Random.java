package aiproj.squatter.lbrick;

import aiproj.squatter.Move;

import java.io.PrintStream;
import java.util.Scanner;
import aiproj.squatter.Player;
import aiproj.squatter.Piece;

/**
 * Created by lochiebrick on 25/05/2015.
 */
public class Random implements Player, Piece {
    private Board board;
    private int playerPiece, opponentPiece;
    private Move lastOppMove;
    private boolean invalidMove;
    private Scanner reader;


    public int init(int n, int p) {
        invalidMove = false;
        if((board = new Board(n)) != null
                && (lastOppMove = new Move()) != null
                && (reader = new Scanner(System.in)) != null) {
            playerPiece = p;
            if(p == WHITE) {
                opponentPiece = BLACK;
            } else {
                opponentPiece = WHITE;
            }
            return 0;
        }

        return -1;
    }

    public Move makeMove() {
        Move m = new Move();
        java.util.Random rn = new java.util.Random();
        int i, j;

        m.P = playerPiece;

        do {
            m.Row = rn.nextInt(board.size);
            m.Col = rn.nextInt(board.size);
        } while(!board.isValid(m));
            board.recordMove(m);
        return m;
    }

    public int opponentMove(Move m) {
        if(board.isValid(m)) {
            board.recordMove(m);

            return 0;
        } else {
            invalidMove = true;
            return INVALID;
        }
    }

    public int getWinner() {
        if(invalidMove) {
            return INVALID;
        }
        else {
            return board.checkState();
        }
    }

    public void printBoard(PrintStream output) { board.print(output); }

    public int[] getScore(){int[] a={}; return a;}
}
