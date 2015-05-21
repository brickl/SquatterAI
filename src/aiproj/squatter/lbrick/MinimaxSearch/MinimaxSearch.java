package aiproj.squatter.lbrick.MinimaxSearch;

import aiproj.squatter.Player;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 169.<br>
 *
 * <pre>
 * <code>
 * function MINIMAX-DECISION(state) returns an action
 *   return argmax_[a in ACTIONS(s)] MIN-VALUE(RESULT(state, a))
 *
 * function MAX-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *   v = -infinity
 *   for each a in ACTIONS(state) do
 *     v = MAX(v, MIN-VALUE(RESULT(s, a)))
 *   return v
 *
 * function MIN-VALUE(state) returns a utility value
 *   if TERMINAL-TEST(state) then return UTILITY(state)
 *     v = infinity
 *     for each a in ACTIONS(state) do
 *       v  = MIN(v, MAX-VALUE(RESULT(s, a)))
 *   return v
 * </code>
 * </pre>
 *
 * Figure 5.3 An algorithm for calculating minimax decisions. It returns the
 * action corresponding to the best possible move, that is, the move that leads
 * to the outcome with the best utility, under the assumption that the opponent
 * plays to minimize utility. The functions MAX-VALUE and MIN-VALUE go through
 * the whole game tree, all the way to the leaves, to determine the backed-up
 * value of a state. The notation argmax_[a in S] f(a) computes the element a of
 * set S that has the maximum value of f(a).
 *
 *
 * @author Ruediger Lunde
 *
 * @param <STATE>
 *            Type which is used for states in the game.
 * @param <ACTION>
 *            Type which is used for actions in the game.
 * @param <PLAYER>
 *            Type which is used for players in the game.
 */
public class MinimaxSearch<STATE, ACTION, PLAYER> {

    private Game<STATE, ACTION, PLAYER> game;
    private int expandedNodes;
    private final int ROOT = 99999;
    //the depth of the tree you will search until
    private int depth = 3;


    /** Creates a new search object for a given game. */
    public static <STATE, ACTION, PLAYER> MinimaxSearch<STATE, ACTION, PLAYER> createFor(
            Game<STATE, ACTION, PLAYER> game) {
        return new MinimaxSearch<STATE, ACTION, PLAYER>(game);
    }

    public MinimaxSearch(Game<STATE, ACTION, PLAYER> game) {
        this.game = game;
    }

    //@Override
    public ACTION makeDecision(STATE state) {
        expandedNodes = 0;
        ACTION result = null;
        double resultValue = Double.NEGATIVE_INFINITY;
        PLAYER player = game.getPlayer(state);
        System.out.printf("p: %d",player);
        for (ACTION action : game.getActions(state)) {
            //newState seems required..
            STATE newState = game.cloneState(state);
            double value = minValue(game.getResult(newState, action), player,this.depth);

            if (value > resultValue) {
                result = action;
                resultValue = value;
            }
        }
        return result;
    }

    public double maxValue(STATE state, PLAYER player, Integer depth_level) { // returns an utility
        // value
        expandedNodes++;
        if (game.isTerminal(state) || depth_level == 0)
            return game.getUtility(state, player);
        double value = Double.NEGATIVE_INFINITY;
        for (ACTION action : game.getActions(state)){
            STATE newState = game.cloneState(state);
            value = Math.max(value,minValue(game.getResult(newState, action), player,depth_level-1));
        }
        return value;
    }

    public double minValue(STATE state, PLAYER player, Integer depth_level) { // returns an utility
        // value
        expandedNodes++;
        if (game.isTerminal(state) || depth_level == 0)
            return game.getUtility(state, player);
        double value = Double.POSITIVE_INFINITY;
        for (ACTION action : game.getActions(state)){
            STATE newState = game.cloneState(state);
            value = Math.min(value,maxValue(game.getResult(newState, action), player,depth_level-1));
        }
        return value;
    }




}