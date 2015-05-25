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
	boolean PRINTSCORE = true;
	boolean PRINTCOORDS = true;

    //set to -1 if no one has moved
    //do not change this - something relies on it.. not sure what.
    int currentPlayer= 1;


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

		try{
            return gameBoard[m.Row][m.Col] == EMPTY;
        }catch(ArrayIndexOutOfBoundsException e){
            return false;
        }
	}

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
			}
			captured.add(cell);
		}
		capture(captured, boundary);
	}

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


    public int[] getScore(){
        return score;
    }

    public int getSize(){return size;}

    //create a new deep copy board
    public Board clone() {
        Board b = new Board(this.getSize());

        b.gameBoard = deepCopyArrayD(this.gameBoard);
        b.score     = deepCopyArray(this.score);
        //size wont change so shallow copy okay
        b.size = this.size;

        return b;
    }

    public static int[] deepCopyArray(int[] original){
        if(original == null){
            return null;
        }
        return Arrays.copyOf(original, original.length);
    }

    // @Rorick code from stack over flow question /1564832
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




    public int getCurrentPlayer(){return currentPlayer;}
}
