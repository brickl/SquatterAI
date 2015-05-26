package aiproj.squatter.lbrick.MinimaxSearch;

import aiproj.squatter.Move;
import aiproj.squatter.Piece;

import aiproj.squatter.Player;
import aiproj.squatter.Referee;
import aiproj.squatter.lbrick.Board;
import aiproj.squatter.lbrick.lbrick;


import java.util.*;


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
    //Return in Descending order based on weighting
    public Move[] getActions(Board b){
        int[][] board;
        int size = b.getSize();
        int value; //Value that determines it order in the actions list - higher rating = more usefull
        board = b.gameBoard;
        //hashmap to store all values
        HashMap<Move, Integer> map = new HashMap<Move, Integer>();
        //compartor used to sort the values
        MyComparator bvc = new MyComparator(map);
        //treemap used to store sorted values
        TreeMap<Move, Integer> sorted_map = new TreeMap<Move, Integer>(bvc);

        Move move;
        for(int column=0; column < size; column++)
        {
            //if a piece is EMPTY then we can move there
            for(int row=0; row < size; row++)
                if (board[row][column] == EMPTY) {
                    move = new Move();
                    move.Row = row;
                    move.Col = column;
                    move.P = b.getCurrentPlayer();
                    //need a function that will return its given value
                    value = calcValue(b, move);
                    map.put(move, value);
                }
        }

        //Need array for API
        Move[] moves = new Move[map.size()]; int i=0;
        //puts data through to comparator
        sorted_map.putAll(map);
        //move data to array
        for(Move m : sorted_map.keySet()){
            moves[i++] = m;
        }

        return moves;
    }




    //Calculates a value based on neighbouring nodes - only checks for the current players nodes
    private int calcValue(Board b, Move m){
        int calculatedValue=-1;
        int currentPlayer = b.getCurrentPlayer();
        int opponent = -1;
        int square;

        if(currentPlayer == Piece.BLACK){
            opponent = Piece.WHITE;
        }else if(currentPlayer == Piece.WHITE){
            opponent = Piece.BLACK;
        }
        //we want to look at all neighbouring cells

        try{
            //above
            if( (square = b.gameBoard[m.Row-1][m.Col]) == currentPlayer || square == opponent ){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //diagonal to the upper right
            if((square = b.gameBoard[m.Row-1][m.Col+1]) == currentPlayer || square == opponent ){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //to the right
            if((square = b.gameBoard[m.Row][m.Col+1]) == currentPlayer || square == opponent){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //to the right bottom diagonal
            if((square = b.gameBoard[m.Row+1][m.Col+1]) == currentPlayer || square == opponent){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //below it
            if((square = b.gameBoard[m.Row+1][m.Col]) == currentPlayer || square == opponent){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //to left bottom diagonal
            if( (square = b.gameBoard[m.Row+1][m.Col-1]) == currentPlayer || square == opponent){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //to left
            if((square = b.gameBoard[m.Row][m.Col-1]) == b.getCurrentPlayer() || square == opponent){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }
        try{
            //to left upper diagonal
            if((square =b.gameBoard[m.Row-1][m.Col-1]) == b.getCurrentPlayer() || square == opponent){
                //System.out.printf("%d", b.gameBoard[m.Row-1][m.Col-1]);
                calculatedValue +=1;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            //
        }

        return calculatedValue;
    }


    //geting some weird shit with this...
    //utility function returns a score
    //no idea on a good function.. soo we need to adjust
    public int getUtility(Board b, Integer p){

        int utilityValue = weight_corners(b, p);
        utilityValue += check_connections(b, p);

        utilityValue += b.getScore()[p]*100;
        //i have seen this work 2 moves ahead
        if( p == WHITE){
            utilityValue -= b.getScore()[BLACK]*100;
        }
        else if(p == BLACK){
            utilityValue -= b.getScore()[WHITE]*100;
        }

        // if the winner is us. or not us
        if(b.testWin() == p){
            utilityValue += 1000;
        } else if( b.testWin()%DEAD != 0 ){
            /* The other player has won */
            utilityValue -= 1000;
        }

        return utilityValue;
    }

    private int weight_corners(Board b, int p) {
        int utilityValue = 0;
        /* Add value if cells next to corners are filled */
        if (b.gameBoard[0][1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[1][0] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[0][b.getSize()-2] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[1][b.getSize()-1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[b.getSize()-2][b.getSize()-1] == p){
            utilityValue+=10;
        }
        if (b.gameBoard[b.getSize()-1][b.getSize()-2] == p){
            utilityValue+=10;
        }

        /* Reduce value if corners are filled */
        if (b.gameBoard[b.getSize()-1][b.getSize()-1] == p) {
            utilityValue -= 20;
        } if (b.gameBoard[b.getSize()-1][0] == p) {
            utilityValue -= 20;
        } if (b.gameBoard[0][b.getSize()-1] == p) {
            utilityValue -= 20;
        } if (b.gameBoard[0][0] == p) {
            utilityValue -= 20;
        }

        return utilityValue;
    }


    private int check_connections(Board b, int p) {
        int i, j, utility = 0;
        for (i = 0; i < b.getSize(); i++) {
            for (j = 0; j < b.getSize(); j++) {
                if (b.gameBoard[i][j] == p) {
                    /* Increase utility for each pair of neighbours
                    (therefore only need to check half of the neighbours of each piece */
                    try {
                        if (b.gameBoard[i][j + 1] == p
                                || b.gameBoard[i + 1][j + 1] == p
                                || b.gameBoard[i - 1][j + 1] == p
                                || b.gameBoard[i + 1][j] == p) {
                            utility += 20;
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }
        return utility;
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

