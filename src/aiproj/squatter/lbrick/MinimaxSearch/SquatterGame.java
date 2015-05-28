package aiproj.squatter.lbrick.MinimaxSearch;

import aiproj.squatter.Move;
import aiproj.squatter.Piece;
import aiproj.squatter.lbrick.Board;
import java.util.*;


/**
 * Created by Nathan on 20/05/15.
 */

/* SquatterGame implements the Game interface required by the AlphaBetaSearch engine */
public class SquatterGame implements Piece, Game<Board,Move,Integer> {

    public static final int CORNERWEIGHT = 2;
    public static final int CORNERDEDUCTION = 50;
    public static final int WINWEIGHT = 100000;
    public static final int STRINGWEIGHT = 10;
    public static final int NEIGHBOURWEIGHT = 20;
    public static final int WHITEWEIGHT = 200;
    public static final int BLACKWEIGHT = 200;//WHITEWEIGHT - STRINGWEIGHT - 1;
    /* Every capture adds 3 new 'strings'.  In order to value defence more when playing as black and offense more when
    playing as white, the difference b/w BLACKWEIGHT and WHITEWEIGHT needs to be >= 3*STRINGWEIGHT
     */



    /**
     * Checks whether the game board is in a terminal state (i.e. if the game is finished).
     * @param b A possible board configuration.
     * @return true if the game is finished, false if it is not finished.
     */
    public boolean isTerminal(Board b)
    {
        if(b.checkState() == 0)
            return false;
        return true;
    }

    /**
     * Returns the state of the board when a given move is applied to it.
     * @param board The current board configuration.
     * @param m The move to be applied.
     * @return The updated board.
     */
    public Board getResult(Board board, Move m){
        board.recordMove(m);
        return board;
    }


    /**
     * Finds all possible moves given the current board state, and returns them in order of how likely each move is to
     * have a high utility function.
     * @param b The current board state.
     * @return An array of all possible moves.
     */
    public Move[] getActions(Board b){
        int[][] board;
        int size = b.getSize();
        int value; //Value that determines it order in the actions list - higher rating = more useful
        board = b.gameBoard;
        //hashmap to store all values
        HashMap<Move, Integer> map = new HashMap<Move, Integer>();
        //comparator used to sort the values
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
                    //value = 1;
                    map.put(move, value);

                }
        }

        //Need array for API
        Move[] moves = new Move[map.size()]; int i=0;
        //puts data through to comparator
        sorted_map.putAll(map);
        //move data to array
        for(Move m : sorted_map.keySet()){
            //System.out.printf("val %d, Move row: %d, col: %d",sorted_map.get(m),m.Row, m.Col);
            //try{Thread.sleep(1000);}catch(Exception e){}
            moves[i++] = m;
        }//System.out.println();

        return moves;
    }


    /**
     * Approximates how likely it is that a given move will be a good move.
     * @param b The current board state.
     * @param m A possible move.
     * @return An approximation of the utiity of the given move.
     */
    private int calcValue(Board b, Move m){
        int calculatedValue=-1;
        int currentPlayer = b.getCurrentPlayer();
        int opponent = -1;
        int square;

        if(currentPlayer == BLACK){
            opponent = WHITE;
        }else if(currentPlayer == WHITE){
            opponent = BLACK;
        }
        //we want to look at all neighbouring cells

        try{
            //diagonal to the upper right
            if((square = b.gameBoard[m.Row-1][m.Col+1]) == currentPlayer ){//|| square == opponent){
                calculatedValue +=1;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            //
        }

        try{
            //to the right bottom diagonal
            if((square = b.gameBoard[m.Row+1][m.Col+1]) == currentPlayer ){//|| square == opponent){
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
            //to left upper diagonal
            if((square =b.gameBoard[m.Row-1][m.Col-1]) == currentPlayer ){//|| square == opponent){
                calculatedValue +=1;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            //
        }

        return calculatedValue;
    }


    /**
     * Returns the utility of a given board state, for the given player
     * @param b The current board state.
     * @param p The code of the player for whom the utility is being assessed.
     * @return An integer utility value.
     */
    public int getUtility(Board b, Integer p){
        int utilityValue = 0;
        utilityValue += weightCorners(b, p);
        utilityValue += checkStrings(b, p);

        if(p == BLACK) {
            utilityValue += b.getScore()[BLACK]*BLACKWEIGHT;
            utilityValue -= b.getScore()[WHITE]*WHITEWEIGHT;
        } else if (p==WHITE) {
            utilityValue += b.getScore()[WHITE]*BLACKWEIGHT;
            utilityValue -= b.getScore()[BLACK]*WHITEWEIGHT;
        }

        // if the winner is us. or not us
        if(b.checkState() == p){
            utilityValue += WINWEIGHT;
        } else if( b.checkState()%DEAD != 0 ){
            /* The other player has won */
            utilityValue -= WINWEIGHT;
        }
        //if(b.gameBoard[4][1] == p) System.out.println("Utility = " + utilityValue);
        return utilityValue;
    }

    /**
     * Checks the utility weighting of the corners of the board, as being close to the corners but not in the corners
     * puts a player in a strong position.
     * @param b The board configuration.
     * @param p The player for whom the utility is being assessed.
     * @return The utility contributed by the corners of the board.
     */
    private int weightCorners(Board b, int p) {
        int utilityValue = 0;
        /* Add value if cells next to corners are filled */


        /* Reduce value if corners are filled */
        if (b.gameBoard[b.getSize()-1][b.getSize()-1] == p) {
            utilityValue -= CORNERDEDUCTION;
        } if (b.gameBoard[b.getSize()-1][0] == p) {
            utilityValue -= CORNERDEDUCTION;
        } if (b.gameBoard[0][b.getSize()-1] == p) {
            utilityValue -= CORNERDEDUCTION;
        } if (b.gameBoard[0][0] == p) {
            utilityValue -= CORNERDEDUCTION;
        }

        return utilityValue;
    }



    private int checkStrings(Board b, int p) {
        int row, col, utility = 0;
        for(row=0; row<b.getSize(); row++) {
            for(col=0; col<b.getSize(); col++) {
                if(b.gameBoard[row][col] == p) {
                    if ( (pieceAbove(b, row, col, p) && pieceBelow(b, row, col, p))
                            || (pieceToLeft(b, row, col, p) && pieceToRight(b, row, col, p)) ) {
                        utility += STRINGWEIGHT;
                    }
                }
            }
        }
        return utility;
    }

    private boolean pieceAbove(Board b, int row, int col, int p) {
        try {
            if (b.gameBoard[row - 1][col - 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row - 1][col] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row - 1][col + 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return false;
    }

    private boolean pieceBelow(Board b, int row, int col, int p) {
        try {
            if (b.gameBoard[row + 1][col - 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row + 1][col] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row + 1][col + 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return false;
    }

    private boolean pieceToLeft(Board b, int row, int col, int p) {
        try {
            if (b.gameBoard[row][col - 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row - 1][col - 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row + 1][col - 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return false;
    }

    private boolean pieceToRight(Board b, int row, int col, int p) {
        try {
            if (b.gameBoard[row][col + 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row + 1][col + 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (b.gameBoard[row - 1][col + 1] == p)
                return true;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return false;
    }




    /**
     * Returns the player whose turn it is to move.
     * @param b The current board state.
     * @return The player whose turn it is to move.
     */
    public Integer getPlayer(Board b){
        return b.getCurrentPlayer();
    }

    /**
     * Creates a deep copy of the given board.
     * @param b The board to be copied.
     * @return The new copy of the board.
     */
    public Board cloneState(Board b){
        Board newB = b.clone();
        return newB;
    }
}

