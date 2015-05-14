package aiproj.squatter;


/*
 *   Piece:
 *      Define types of states that can appear on a board
 *      
 *   @author lrashidi
 *   
 */

public interface Piece {
    public static final int WHITE = 1, 
                            BLACK = 2,
                            DEAD = 3,
                            EMPTY = 0,
    		                INVALID = -1;
}
