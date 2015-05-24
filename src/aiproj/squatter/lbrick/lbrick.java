package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import aiproj.squatter.lbrick.MinimaxSearch.AlphaBetaSearch;
import aiproj.squatter.lbrick.MinimaxSearch.MinimaxSearch;
import aiproj.squatter.lbrick.MinimaxSearch.MyGame;

import java.util.Random;

import java.io.PrintStream;

public class lbrick implements Player, Piece {
	
	Board board;
	Move lastOppMove;
	int playerPiece, opponentPiece, moveCount;
	boolean invalidMove;
	boolean PRINTSCORE = true;


	public int init(int n, int p) {
		moveCount = 0;
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

		if(PRINTSCORE)
        	System.out.printf("Scores white: %d, black: %d", this.board.getScore()[WHITE], this.board.getScore()[BLACK]);

		Move m = new Move();
        Move m2= new Move();

        //if we don't want this to go into min-max straight away
        if(this.moveCount >= 2)
        {
            //System.out.printf("not a random move");
            //set up the minmaxsearch
            MyGame game = new MyGame(board, m, this.playerPiece);
            //MinimaxSearch mms = new MinimaxSearch(game);
			AlphaBetaSearch aas = new AlphaBetaSearch<Board, Move, Integer>(game);
            Board b = board.clone();
            //search the board but a copy of the one we have
            //m = (Move)mms.makeDecision(b);
			m = (Move)aas.makeDecision(b);
        }
        else {
//           what we can do instead of minmax for early turns?
			//arround corner moves starting with bottom left
			m.Row = board.getSize() - 2;
			m.Col = 0;

			//other piece for bottom left
			m2.Row = board.getSize() - 1;
			m2.Col = 1;

			//so if both moves are valid we will do one
			//in future should also check all other cells near

			if (board.isValid(m)) {
				m.P = playerPiece;
				board.recordMove(m);
				moveCount++;
				return m;
			} else if (board.isValid(m2)) {
				m2.P = playerPiece;
				board.recordMove(m2);
				moveCount++;
				return m2;
			} else {
				/*Make a random move:*/
				Random rn = new Random();
				m.Row = rn.nextInt(board.size);
				m.Col = rn.nextInt(board.size);

				while(!board.isValid(m)) {
					m.Row = rn.nextInt(board.size);
					m.Col = rn.nextInt(board.size);
				}
			}
		}




        moveCount++;
        m.P = playerPiece;
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

    public int getPlayerPiece(){
        return playerPiece;
    }
    public int getOpponentPiece(){return opponentPiece;}
}
