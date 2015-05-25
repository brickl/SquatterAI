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

/* Squatter Game is a helper class for AlphaBetaSearch, providing certain methods */
public class SquatterGame implements Piece, Game<Board,Move,Integer> {



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
                if(board[column][row] == EMPTY)
                {
                    return false;
                }
            }
        }
        return true;
    }

    //returns state of the board after move
    public Board getResult(Board board, Move m){
        board.recordMove(m);
        return board;
    }

    //returns all possible moves based on board;
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
                if(board[row][column] == EMPTY)
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



        //have those squares next to the corners weighted positively..
        if (b.gameBoard[0][1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[1][0] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[0][size-2] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[1][size-1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[size-2][size-1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[size-1][size-2] == p){
            utilityValue+=10;
        }

        //i have seen this work 2 moves ahead
        if( p == WHITE){
            utilityValue += b.getScore()[p]*100;
            utilityValue -= b.getScore()[BLACK]*100;
        }
        else if(p==BLACK){
            utilityValue += b.getScore()[p]*100;
            utilityValue -= b.getScore()[WHITE]*100;
        }

        // if the winner is us. or not us
        if(b.testWin() == p){
            utilityValue += 1000;
        }
        else if( b.testWin() == WHITE && p == BLACK || b.testWin() == BLACK && p == WHITE ){
            utilityValue -= 1000;
        }

        return utilityValue;
    }


    //returns the current Player. This updated & maintained by board
    //in record move this is updated
    public Integer getPlayer(Board b){
        //System.out.printf("Current Player %d", b.getCurrentPlayer());
        return b.getCurrentPlayer();
    }


    public Board cloneState(Board b){
        Board newB = b.clone();
        return newB;
    }
}

