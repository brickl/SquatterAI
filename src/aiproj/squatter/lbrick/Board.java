package aiproj.squatter.lbrick;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import aiproj.squatter.*;


public class Board implements Piece {

	public int[][] gameBoard;
	int size;
	int[] score;
	boolean DEBUG = false;
	boolean PRINTSCORE = false;
	boolean PRINTCOORDS = true;

    //set to -1 if no one has moved
    //do not change this - something relies on it.. not sure what.
    int currentPlayer= 1;


	/**
	 * Constructor.
	 * @param n The size of the board to be created.
	 */
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


	/**
	 * Checks if a move is valid, given the current board state.
	 * @param m
	 * @return
	 */
	public boolean isValid(Move m) {

		try{
            return gameBoard[m.Row][m.Col] == EMPTY;
        }catch(ArrayIndexOutOfBoundsException e){
            return false;
        }
	}


	/**
	 * Updates the board configuration according to the given move.
	 * @param move The move that has been made.
	 */
	public void recordMove(Move move) {
		gameBoard[move.Row][move.Col] = move.P;
        if(move.P == Piece.WHITE){
            this.currentPlayer = Piece.BLACK;
        }
        else{
            this.currentPlayer = Piece.WHITE;
        }
		checkCapture(move);
	}


	/**
	 * Prints the current board configuration.
	 * @param output The printstream to which the output should be printed.
	 */
	public void print(PrintStream output) {
		int i, j;
		String boardPrintout = "";

        /* For human user to play against computer */
		if(PRINTCOORDS) {
			System.out.printf(" ");
			for (i = 0; i < size; i++) {
				System.out.printf(" %d", i);
			}
			System.out.printf("\n");
		}

		for(i=0; i<size; i++) {
			if(PRINTCOORDS) {
				/* For human user to play against computer */
				boardPrintout += Integer.toString(i);
				boardPrintout += " ";
			}


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


	/**
	 * Checks if the game if finished, and if so whether there is a winner.
	 * @return EMPTY if the game is unfinished, DEAD if the game is drawn, BLACK if the black player has won or WHITE
	 * if the white player has won.
	 */
	public int checkState() {
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


	/**
	 * Checks to see if the given move has captured any pieces.
	 * @param m The move to be made.
	 */
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


	/**
	 * Uses a variation of the floodfill algorithm to check if the given row and column is part of a captured region.
	 * @param row The row of the cell to be checked.
	 * @param col The column of the cell to be checked.
	 * @param boundary The colour of the pieces that will form the boundary of the region.
	 */
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
				/* reached edge of board, no captured cells */
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
				captured.add(cell);
			}

		}
		capture(captured, boundary);
	}


	/**
	 * Marks pieces in the given cells as captured and updates the score.
	 * @param captured The cells that have been captured.
	 * @param capturer The player who captured the cells.
	 */
	private void capture(ArrayList<Cell> captured, int capturer) {
		if(DEBUG)
			System.out.println("We are capturing");
		for(Cell caught : captured) {

			/* Capture all cells that haven't already been captured */
			if(gameBoard[caught.row][caught.col] < DEAD) {
				gameBoard[caught.row][caught.col] += DEAD;

				/* Only increase score if captured piece does not belong to the capturer */
				if(gameBoard[caught.row][caught.col] != capturer) {
					score[capturer] += 1;
				}
			}
		}
	}


	/**
	 *
	 * @return An array containing the score for each player.
	 */
    public int[] getScore(){
        return score;
    }


	/**
	 *
	 * @return The size of the game board.
	 */
    public int getSize(){return size;}


	/**
	 * Creates a deep copy of the current board.
	 * @return A replica of this board.
	 */
    public Board clone() {
        Board b = new Board(this.getSize());

        b.gameBoard = deepCopyArrayD(this.gameBoard);
        b.score     = deepCopyArray(this.score);
        //size wont change so shallow copy okay
        b.size = this.size;

        return b;
    }


	/**
	 * Creates a deep copy of a one dimensional array
	 * @param original The arra to be copied.
	 * @return A copy of the array.
	 */
    public static int[] deepCopyArray(int[] original){
        if(original == null){
            return null;
        }
        return Arrays.copyOf(original, original.length);
    }

	/**
	 * @Rorick code from stack over flow question /1564832
	 * Creates a deep copy of a two dimensional array
	 * @param original The array to be copied.
	 * @return A copy of the array.
	 */
    //
    public static int[][] deepCopyArrayD(int[][] original){
        if(original == null){
            return null;
        }

        final int[][] result = new int[original.length][];
        for(int i =0; i <original.length; i++){
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }


	/**
	 *
	 * @return The player whose turn it is to make a move.
	 */
    public int getCurrentPlayer(){return currentPlayer;}
}
