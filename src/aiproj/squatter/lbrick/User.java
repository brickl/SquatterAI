package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import java.util.Random;
import java.util.Scanner;

import java.io.PrintStream;

public class User implements Player, Piece {

    Board board;
    int playerPiece, opponentPiece;
    Move lastOppMove;
    boolean invalidMove;
    Scanner reader;


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
        Random rn = new Random();
        int i, j;

        m.P = playerPiece;
        System.out.println("Enter the row and column of your next move (with a space in between)");
        m.Row = reader.nextInt();
        m.Col = reader.nextInt();

		while(!board.isValid(m)) {
            System.out.println("Invalid input.  Try again.");
            m.Row = reader.nextInt();
            m.Col = reader.nextInt();
        }

//        m.Row = rn.nextInt(board.size);
//        m.Col = rn.nextInt(board.size);
//
//        while(!board.isValid(m)) {
//            m.Row = rn.nextInt(board.size);
//            m.Col = rn.nextInt(board.size);
//        }
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
            return board.testWin();
        }
    }

    public void printBoard(PrintStream output) { board.print(output); }
}