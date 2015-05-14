package aiproj.squatter.lbrick;

import java.io.PrintStream;

import aiproj.squatter.*;


public class Board implements Piece {
	
	char[][] gameBoard;
	int size, whiteScore, blackScore;
	
	public Board (int n) {
		int i, j;
		gameBoard = new char[n][n];
		for(i=0; i<n; i++) {
			for(j=0; j<n; j++) {
				gameBoard[i][j] = '+';
			}
		}
		
		whiteScore = 0;
		blackScore = 0;
		size = n;
	}
	
	public boolean isValid(Move m) {
		if(gameBoard[m.Row][m.Col] == '+') {
			return true;
		} else {
			return false;
		}
	}
	
	public void recordMove(Move move) {
		if(move.P == WHITE) {
			gameBoard[move.Row][move.Col] = 'W';
		} else if (move.P == BLACK) {
			gameBoard[move.Row][move.Col] = 'B';
		}
	}
	
	public void print(PrintStream output) {
		int i, j;
		String boardPrintout = "";
		for(i=0; i<size; i++) {
			for(j=0; j<size; j++) {
				boardPrintout += gameBoard[i][j] + " ";
			}
			boardPrintout += "\n";
		}
		output.print(boardPrintout);
	}
	
	public int testWin() {
		int i, j;
		for(i=0; i<size; i++) {
			for(j=0; j<size; j++) {
				if(gameBoard[i][j] == '+') {
					return EMPTY;
				}
			}
		}
		if(blackScore > whiteScore) {
			return BLACK;
		} else if(whiteScore > blackScore) {
			return WHITE;
		}
		return EMPTY;
		
	}

}
