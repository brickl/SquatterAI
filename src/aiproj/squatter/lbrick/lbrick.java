package aiproj.squatter.lbrick;

import aiproj.squatter.*;
import aiproj.squatter.lbrick.MinimaxSearch.MinimaxSearch;
import aiproj.squatter.lbrick.MinimaxSearch.MyGame;

import java.util.Random;

import java.io.PrintStream;

public class lbrick implements Player, Piece {
	
	Board board;
	int playerPiece, opponentPiece;
	Move lastOppMove;
	boolean invalidMove;

    int moveCount=0;


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
		int i, j;

		/*Make a random move:*/

		m.Row = rn.nextInt(board.size);
		m.Col = rn.nextInt(board.size);
		
		while(!board.isValid(m)) {
			m.Row = rn.nextInt(board.size);
			m.Col = rn.nextInt(board.size);
		}



//        System.out.printf("\nTesting all functions from My game\n");
//        System.out.printf("First: isterminaL?");
//        System.out.print( game.isTerminal(board));
//
//        System.out.printf("\nBoard get results before: \n");
//        board.print(System.out);
//        System.out.printf("After the move\n");
//        game.getResult(board, m);
//        board.print(System.out);
//
//        System.out.printf("get Actions\n");
//        for(Move newmove : game.getActions(board))
//        {
//            System.out.printf("move: col:%d row:%d\n", newmove.Col, newmove.Row);
//        }
//
//        System.out.printf("\ncurrent utility");
//        System.out.printf(": %d", game.getUtility(board,this));

//        System.out.printf("testin get results\n");
//        Board b1 = new Board(lbrick,6);


        //if we don't want this to go into min-max straight away
        if( !(this.moveCount < 0))
        {
            System.out.printf("not a random move");
            //set up the minmaxsearch
            MyGame game = new MyGame(board, m, this.playerPiece);
            MinimaxSearch mms = new MinimaxSearch(game);
            Board b = board.clone();
            //search the board but a copy of the one we have
            m = (Move)mms.makeDecision(b);
        }
        else
        {
//           what we can do instead of minmax for early turns?
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
