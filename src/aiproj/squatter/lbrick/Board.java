package aiproj.squatter.lbrick;

import java.io.PrintStream;
import java.util.ArrayList;
import aiproj.squatter.*;


public class Board implements Piece {

	int[][] gameBoard;
	int size;
	int[] score;
	boolean DEBUG = false;
	boolean PRINTSCORE = false;

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
		if(PRINTSCORE)
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
		ArrayList<Cell> captured = new ArrayList<>();
		ArrayList<Cell> queue = new ArrayList<>();
		Cell cell, newcell;
		int r, c;


		queue.add(new Cell(row, col));
		if(DEBUG)
			System.out.println("Checking " + row + "," + col + ". Boundary = " + boundary);
		while(!queue.isEmpty()) {
			cell = queue.remove(0);
			r = cell.row;
			c = cell.col;

			if((r == 0 || c == 0 || r == size-1 || c == size-1) && gameBoard[r][c] != boundary) {
				/* reached end of board, no captured cells */
				return;
			} else if(gameBoard[r][c] != boundary) {
				if(!captured.contains(newcell = new Cell(r+1, c))) {
					queue.add(newcell);
				}
				if(!captured.contains(newcell = new Cell(r-1, c))) {
					queue.add(newcell);
				}
				if(!captured.contains(newcell = new Cell(r, c+1))) {
					queue.add(newcell);
				}
				if(!captured.contains(newcell = new Cell(r, c-1))) {
					queue.add(newcell);
				}
			}
			captured.add(cell);
		}
		//System.out.println("Hello?");
		capture(captured, boundary);

	}

	private void capture(ArrayList<Cell> captured, int capturer) {;
		if(DEBUG)
			System.out.println("We are capturing");
		for(Cell caught : captured) {
			if(gameBoard[caught.row][caught.col] != capturer && gameBoard[caught.row][caught.col] < DEAD) {
				gameBoard[caught.row][caught.col] += DEAD;
				score[capturer] += 1;
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
