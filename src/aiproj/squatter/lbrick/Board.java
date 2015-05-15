package aiproj.squatter.lbrick;

import java.io.PrintStream;
import java.util.ArrayList;
import aiproj.squatter.*;


public class Board implements Piece {

	int[][] gameBoard;
	int size;
	int[] score;
	boolean DEBUG = true;

	public Board (int n) {
		int i, j;
		gameBoard = new int[n][n];
		score = new int[]{0, 0, 0};
		for(i=0; i<n; i++) {
			for(j=0; j<n; j++) {
				gameBoard[i][j] = EMPTY;
			}
		}
		size = n;
	}

	public boolean isValid(Move m) {

		return gameBoard[m.Row][m.Col] == EMPTY;

	}

	public void recordMove(Move move) {
		gameBoard[move.Row][move.Col] = move.P;
		checkCapture(move);
	}

	public void print(PrintStream output) {
		int i, j;
		String boardPrintout = "";
		for(i=0; i<size; i++) {
			for(j=0; j<size; j++) {
				if(gameBoard[i][j] == EMPTY) {
					boardPrintout += "+";
				} else if(gameBoard[i][j] == WHITE) {
					boardPrintout += "W";
				} else if(gameBoard[i][j] == BLACK) {
					boardPrintout += "B";
				} else if(gameBoard[i][j] == DEAD) {
					boardPrintout += "-";
				} else if(gameBoard[i][j] == WHITE + DEAD) {
					boardPrintout += "w";
				} else if(gameBoard[i][j] == BLACK + DEAD) {
					boardPrintout += "b";
				}
				boardPrintout += " ";
			}
			boardPrintout += "\n";
		}
		output.print(boardPrintout);
	}

	public int testWin() {
		int i, j;
		System.out.println("Score: Black " + score[BLACK] + " vs White " + score[WHITE]);
		for(i=0; i<size; i++) {
			for(j=0; j<size; j++) {
				if(gameBoard[i][j] == EMPTY) {
					return EMPTY;
				}
			}
		}
		if(score[BLACK] > score[WHITE]) {
			return BLACK;
		} else if(score[WHITE] > score[BLACK]) {
			return WHITE;
		}
		return DEAD;

	}

	public void checkCapture(Move m) {

		if(m.Row+1 < size) {
			floodFill(m.Row+1, m.Col, m.P);
		} if(m.Row-1 >= 0) {
			floodFill(m.Row-1, m.Col, m.P);
		} if(m.Col+1 < size) {
			floodFill(m.Row, m.Col+1, m.P);
		} if(m.Col-1 >= 0) {
			floodFill(m.Row, m.Col-1, m.P);
		}
		if(DEBUG)
			System.out.println("Capture checking finished");
	}


	private void floodFill(int row, int col, int boundary) {
		ArrayList<Cell> queue = new ArrayList<>();
		Cell cell, newcell;
		int r, c;
		int[][] cells = new int[size][size];

		if(gameBoard[row][col] == boundary) {
			return;
		}
		queue.add(new Cell(row, col));

		System.out.println("Checking " + row + "," + col + ". Boundary = " + boundary);
		while(!queue.isEmpty()) {
			cell = queue.remove(0);
			r = cell.row;
			c = cell.col;
			System.out.println("Current cell: " + r + "," + c);
			if (r == 0 || c == 0 || r == size - 1 || c == size - 1)
				return;
			if (gameBoard[r + 1][c] != boundary && cells[r+1][c] != DEAD)
				queue.add(new Cell(r+1, c));
			if (gameBoard[r - 1][c] != boundary && cells[r-1][c] != DEAD)
				queue.add(new Cell(r-1, c));
			if (gameBoard[r][c + 1] != boundary && cells[r][c+1] != DEAD)
				queue.add(new Cell(r, c+1));
			if (gameBoard[r][c - 1] != boundary && cells[r][c-1] != DEAD)
				queue.add(new Cell(r, c-1));
			cells[r][c] = DEAD;
		}
		//System.out.println("Hello?");
		capture(cells, boundary);
	}

	private void capture(int[][] cells, int capturer) {
		int r, c;
		if(DEBUG)
			System.out.println("We are capturing");
		for(r=0; r<size; r++) {
			for(c=0; c<size; c++) {
				if(cells[r][c] == DEAD && gameBoard[r][c] != capturer && gameBoard[r][c] < DEAD) {
					gameBoard[r][c] += DEAD;
					score[capturer] += 1;
				}
			}
		}
	}

	private Move createMove(int P, int row, int col) {
		Move m = new Move();
		m.P = P;
		m.Row = row;
		m.Col = col;
		return m;
	}
}
