package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import aiproj.squatter.lbrick.MinimaxSearch.AlphaBetaSearch;
import aiproj.squatter.lbrick.MinimaxSearch.SquatterGame;

import java.util.Random;

import java.io.PrintStream;

public class lbrick implements Player, Piece {
	
	Board board;
	Move lastOppMove;
	SquatterGame game;
	int playerPiece, opponentPiece, moveCount;
	boolean invalidMove;


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
        if(moveCount >= 2)
        {
			AlphaBetaSearch engine = new AlphaBetaSearch<>(game);
            Board b = board.clone();
            //search the board but a copy of the one we have
            //m = (Move)mms.makeDecision(b);
			m = (Move)engine.makeDecision(b);
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


	/**
	 *
	 * @return The piece being used by this player.
	 */
    public int getPlayerPiece(){
        return playerPiece;
    }


	/**
	 *
	 * @return The piece being used by this player's opponent.
	 */
    public int getOpponentPiece(){return opponentPiece;}
}
