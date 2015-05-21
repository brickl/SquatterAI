package aiproj.squatter.lbrick.MinimaxSearch;

import aiproj.squatter.lbrick.Board;
import aiproj.squatter.lbrick.lbrick;

import java.util.List;

/**
 * Created by nathan on 20/05/15.
 */
public interface Game<STATE, ACTION, PLAYER>{

    public boolean isTerminal(STATE s);

    public STATE getResult(STATE s, ACTION a);

    public ACTION[] getActions(STATE s);

    public int getUtility(STATE s, PLAYER p);

    public PLAYER getPlayer(STATE s);

    public STATE cloneState(STATE s);

}