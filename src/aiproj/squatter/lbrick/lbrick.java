package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import aiproj.squatter.lbrick.MinimaxSearch.AlphaBetaSearch;
import aiproj.squatter.lbrick.MinimaxSearch.SquatterGame;

import java.util.Random;

import java.io.PrintStream;

/**
 * Created by lochiebrick on 20/05/15.
 * Lochlan Brick lbrick 638126
 * Nathan Malishev nmalishev 637410
 */

public class lbrick implements Player, Piece {
	
	private Board board;

	private int playerPiece, moveCount;
	private boolean invalidMove;
    private AlphaBetaSearch gameEngine;
	private int default_moves = 2;

	/**
	 * Initializes a player for a new game.
	 * @param size The size of the game board to be used.
	 * @param piece The piece to be used by this player
	 * @return 0 if the player is successfully initialized, -1 if not.
	 */
	public int init(int size, int piece) {
		SquatterGame game;
		moveCount = 0;
		invalidMove = false;

		if((board = new Board(size)) != null && (game = new SquatterGame())
				!= null) {
			playerPiece = piece;
            //if board size 6 make 2 defualt moves
            if (size == 6){
                gameEngine = new AlphaBetaSearch<Board,Move,Integer>(game);
                gameEngine.setDepth(4);
			} else if(size == 7){
                gameEngine = new AlphaBetaSearch<Board,Move,Integer>(game);
                gameEngine.setDepth(3);
            }

			return 0;
		}
		return -1;
	}


	/**
	 * Instructs the player to make a move.
	 * @return The move chosen by the player.
	 */
	public Move makeMove() {

		Move m;
		Move m1 = new Move();
		Move m2= new Move();
		Move m3=new Move();
		Move m4=new Move();

		/* Avoid using minmax for a certain number of moves */
		if(moveCount >= default_moves)
		{
			Board b = board.clone();
			m = (Move)this.gameEngine.makeDecision(b);
		}
		else {
			/* Play in the corners early on*/
			m1.Row = board.getSize() - 2;
			m1.Col = 0;

			//other piece for bottom left
			m2.Row = board.getSize() - 1;
			m2.Col = 1;

			//we must choose a new corner
			//we choose top left
			m3.Row = 0;
			m3.Col = 1;

			//other piece for bottom left
			m4.Row = 1;
			m4.Col = 0;

			Move[] movesList = {m1,m2,m3,m4};
			int i=0;
			for( i = 0; i< movesList.length; i++){
				if(board.isValid(movesList[i])){
					break;
				}

			}
			m = movesList[i];
		}
		//increase move counter, & record piece move
		moveCount++;
		m.P = playerPiece;
		board.recordMove(m);

		return m;
	}
	/**
	 * Informs the player of the move made by its opponent
	 * @param m The opponent's move.
	 * @return 0 if the move is valid, INVALID (=-1) if the move is invalid.
	 */
	public int opponentMove(Move m) {
		if(board.isValid(m)) {
			board.recordMove(m);
			return 0;
		} else {
			invalidMove = true;
			return INVALID;
		}
	}


	/**
	 * Checks to see if the game is over, and whether there is a winner.
	 * @return INVALID if the opponent has made an illegal move, EMPTY if the
	 * 	game is still going, DEAD if the game is a
	 * draw.  If the game is over, returns the colour of the winning player.
	 */
	public int getWinner() {
		if(invalidMove) {
			return INVALID;
		}
		else {
			return board.checkState();
		}
	}


	/**
	 * Prints the current board configuration.
	 * @param output The printstream to be used for output.
	 */
	public void printBoard(PrintStream output) { board.print(output); }

	/***
	 * @return the array of board scores
	 */
    public int[] getScore(){return board.getScore();}
}
