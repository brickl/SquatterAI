package aiproj.squatter;

/*   
 *   Referee:
 *      A mediator between two players. It is responsible to initialize 
 *      the players and pass the plays between them and terminates the game. 
 *      It is the responsibility of the players to check whether they have won and
 *      maintain the board state.
 *
 *   @author lrashidi
 */


import java.util.ArrayList;

public class Referee implements Piece{

    private static Player P1;
    private static Player P2;
    private static Move lastPlayedMove;


    /*
     * Input arguments: first board size, second path of player1 and third path of player2
     */
    public static void main(String[] args)
    {
        int runTime= 500;
        int wins   = 0;
        int losses = 0;
        int draws  = 0;
        int[] outcome = {0,0,0};
        int totW =0;
        int totB=0;
        long totTime =0;

        for(int i=0; i<runTime; i++){
            long startTime = System.nanoTime();
            outcome = start(args);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            totTime +=duration;
            if( outcome[0] == 1){
                wins++;
            }else if(outcome[0] == 2){
                losses++;
            }else if(outcome[0] == 3){
                draws++;
            }
            totW += outcome[1];
            totB += outcome[2];
        }

        System.out.printf("After %d runs - Wins: %d, losses: %d, draws: %d\n",runTime, wins, losses, draws);
        System.out.printf("Average scores White: %f, Black %f\n", ((double)totW)/runTime, (double)totB/runTime);
        System.out.printf("Average time running: %f\n", (totTime / runTime) / 1000000000.0  );
    }



    private static int[] start(String[] args){
        lastPlayedMove = new Move();
        int NumberofMoves = 0;
        int boardEmptyPieces=Integer.valueOf(args[0])*Integer.valueOf(args[0]);
        int gBoardEmptyPieces=Integer.valueOf(args[0])*Integer.valueOf(args[0]);

        //System.out.println("Referee started !");
        try{
            P1 = (Player)(Class.forName(args[1]).newInstance());
            P2 = (Player)(Class.forName(args[2]).newInstance());
        }
        catch(Exception e){
            System.out.println("Error "+ e.getMessage());
            System.exit(1);
        }

        P1.init(Integer.valueOf(args[0]), WHITE);
        P2.init(Integer.valueOf(args[0]), BLACK);


        while(boardEmptyPieces > 0 && P1.getWinner() == 0 && P2.getWinner() == 0)
        {

            NumberofMoves++;
            lastPlayedMove=P1.makeMove();
            //System.out.println("Placing to. "+lastPlayedMove.Row+":"+lastPlayedMove.Col+" by "+lastPlayedMove.P);
            //P1.printBoard(System.out);
            boardEmptyPieces--;



            if(P2.opponentMove(lastPlayedMove)<0)
            {
                //System.out.println("Exception: Player 2 rejected the move of player 1.");
                //P1.printBoard(System.out);
                //P2.printBoard(System.out);
                System.exit(1);
            }
            else if(P2.getWinner()==0  && P1.getWinner()==0) {
                NumberofMoves++;
                lastPlayedMove = P2.makeMove();
                //System.out.println("Placing to. " + lastPlayedMove.Row + ":" + lastPlayedMove.Col + " by " + lastPlayedMove.P);
                boardEmptyPieces--;

                //P2.printBoard(System.out);

                if (P1.opponentMove(lastPlayedMove) < 0) {
                    System.out.println("Exception: Player 1 rejected the move of player 2.");
                    P2.printBoard(System.out);
                    P1.printBoard(System.out);
                    System.exit(1);
                }
            }

        }
        //System.out.println("--------------------------------------");
        //System.out.println("P2 Board is :");
        //P2.printBoard(System.out);
        // System.out.println("P1 Board is :");
        // P1.printBoard(System.out);

        //System.out.println("Player one (White) indicate winner as: "+ P1.getWinner());
        // System.out.println("Player two (Black) indicate winner as: "+ P2.getWinner());
//        System.out.println("Total Number of Moves Played in the Game: "+ NumberofMoves);
//        System.out.println("Referee Finished !");
        int[] answers = {0,0,0};
        answers[0] = (P1.getWinner());
        answers[1] = (P1.getScore()[Piece.WHITE]);
        answers[2] = (P1.getScore()[Piece.BLACK]);
        return answers;
    }


}
