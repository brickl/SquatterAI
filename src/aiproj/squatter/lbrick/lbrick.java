package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import aiproj.squatter.lbrick.MinimaxSearch.AlphaBetaSearch;
import aiproj.squatter.lbrick.MinimaxSearch.SquatterGame;

import java.util.Random;

import java.io.PrintStream;

public class lbrick implements Player, Piece {
	
	private Board board;
	private Move lastOppMove;
	private SquatterGame game;
	private int playerPiece, opponentPiece, moveCount;
	private boolean invalidMove;
    private AlphaBetaSearch gameEngine;
	//Default set up for a board not size 6 or 7
	private int depth_step = 8;
	private int default_moves =3;

	/**
	 * Initializes a player for a new game.
	 * @param size The size of the game board to be used.
	 * @param piece The piece to be used by this player
	 * @return 0 if the player is successfully initialized, -1 if not.
	 */
	public int init(int size, int piece) {
		moveCount = 0;
		invalidMove = false;


		if((board = new Board(size)) != null && (lastOppMove = new Move()) != null && (game = new SquatterGame()) != null) {
			playerPiece = piece;
            //if board size 6 make 2 defualt moves
            if (size == 6){
                this.default_moves = 2;
                gameEngine = new AlphaBetaSearch<Board,Move,Integer>(game);
                gameEngine.setDepth(4);
				this.depth_step = 7;
            }else if(size == 7){
                this.default_moves = 2;
                gameEngine = new AlphaBetaSearch<Board,Move,Integer>(game);
                gameEngine.setDepth(3);
				this.depth_step = 8;
            }

            if(piece == WHITE) {
				opponentPiece = BLACK;
			} else {
				opponentPiece = WHITE;
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

		Move m = new Move();
        Move m2= new Move();

        //if we don't want this to go into min-max straight away
        if(moveCount >= default_moves)
        {

            Board b = board.clone();
            //search the board but a copy of the one we have
            //m = (Move)mms.makeDecision(b);
			m = (Move)this.gameEngine.makeDecision(b);

            if(moveCount % depth_step == 0){
                gameEngine.setDepth( gameEngine.getDepth()+1);
            }
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
		//increase move counter, & record peice move
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
	 * @return INVALID if the opponent has made an illegal move, EMPTY if the game is still going, DEAD if the game is a
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
