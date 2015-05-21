package aiproj.squatter.lbrick.MinimaxSearch;

import aiproj.squatter.Move;
import aiproj.squatter.Piece;

import aiproj.squatter.Player;
import aiproj.squatter.Referee;
import aiproj.squatter.lbrick.Board;
import aiproj.squatter.lbrick.lbrick;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by nathan on 20/05/15.
 */

//My Game is basically the Helper function to Minimax Search providing certain methods
public class MyGame implements Game<Board,Move,Integer> {

    private Board board; //this is the board
    private Move move; //
    private Integer player;   // Integer represents class

    public MyGame(Board b,Move m,Integer p){
        this.board = b;
        this.move = m;
        this.player = p;
    }

    //function returns if game is terminal?
    public boolean isTerminal(Board b)
    {
        int[][] board;
        board = b.gameBoard;
        int size = b.getSize();
        for(int column=0; column < size; column++)
        {
            for(int row=0; row < size; row++)
            {
                //if a piece is EMPTY then game is not terminal
                if(board[column][row] == Piece.EMPTY)
                {
                    return false;
                }
            }
        }
        return true;
    }

    //returns board of the board after move
    public Board getResult(Board board, Move m){
        //Board b = board.clone(); do not think we need this

        board.recordMove(m);

        //board.print(System.out);

        return board;
    }

    //returns all possible actions based on board;
    public Move[] getActions(Board b){
        int[][] board;
        int size = b.getSize();
        board = b.gameBoard;

        List<Move> movesList = new ArrayList<>();

        Move move;
        for(int column=0; column < size; column++)
        {
            for(int row=0; row < size; row++)
            {
                //if a piece is EMPTY then we can move there
                if(board[row][column] == Piece.EMPTY)
                {
                    move = new Move();
                    move.Row = row;
                    move.Col = column;
                    move.P = b.getCurrentPlayer();
                    //then we add move to moves
                    movesList.add(move);
                    //System.out.printf("\nmove col: %d, row %d", column, row);
                }
            }
        }
        //converts our ArrayList into our handy Move[] that is required for API
        Move[] moves = new Move[movesList.size()];
        moves = movesList.toArray(moves);
        return moves;
    }


    //geting some weird shit with this...
    //utility function returns a score
    //no idea on a good function.. soo we need to adjust
    public int getUtility(Board b, Integer p){
        int utilityValue=0;
        int size = b.getSize();



        //show that it will put a piece where you want it
        if (b.gameBoard[0][1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[1][0] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[2][1] == p){
            utilityValue+=10;
        }

        utilityValue += b.getScore()[p]*100;

        return utilityValue;
    }

    //returns current player based on state of the game

    //returns the current Player. This updated & maintained by board
    //in record move this is updated
    public Integer getPlayer(Board b){
        //System.out.printf("Current Player %d", b.getCurrentPlayer());
        return b.getCurrentPlayer();
    }

    //We need to clone board in lbrick make move ...
    //might need a new state as well?
    //dont think this func is used
    public Board cloneState(Board b){
        Board newB = b.clone();
        return newB;
    }
}
