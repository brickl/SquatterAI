package aiproj.squatter.lbrick.MinimaxSearch;

import aiproj.squatter.lbrick.Board;
import aiproj.squatter.lbrick.lbrick;

import java.util.List;

/**
 * Created by Nathan on 20/05/15.
 * Lochlan Brick lbrick 638126
 * Nathan Malishev nmalishev 637410
 */

/* Interface required by AlphaBetaSearch */

public interface Game<STATE, ACTION, PLAYER>{

    boolean isTerminal(STATE s);

    STATE getResult(STATE s, ACTION a);

    ACTION[] getActions(STATE s);

    int getUtility(STATE s, PLAYER p);

    PLAYER getPlayer(STATE s);

    STATE cloneState(STATE s);

}