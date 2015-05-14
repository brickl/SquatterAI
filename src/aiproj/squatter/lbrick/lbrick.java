package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import java.util.Random;

import java.io.PrintStream;

public class lbrick implements Player, Piece {
	
	Board board;
	int playerPiece, opponentPiece;
	Move lastOppMove;
	boolean invalidMove;
	
	

	
	public int init(int n, int p) {
		invalidMove = false;
		if((board = new Board(n)) != null && (lastOppMove = new Move()) != null) {
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
		
		m.P = playerPiece;
		m.Row = rn.nextInt(board.size);
		m.Col = rn.nextInt(board.size);
		
		while(!board.isValid(m)) {
			m.Row = rn.nextInt(board.size);
			m.Col = rn.nextInt(board.size);
		}
		return m;
	}
	
	public int opponentMove(Move m) {
		if(board.isValid(m)) {
			board.recordMove(m);
		} else {
			invalidMove = true;
		}
		return 1;
	}
	
	public int getWinner() {
		if(invalidMove) {
			return INVALID;
		}
		else {
			return board.testWin();
		}
	}
	
	public void printBoard(PrintStream output) {
		board.print(output);	
	}

}
