package aiproj.squatter.lbrick.MinimaxSearch;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by Nathan on 20/05/15.
 * Lochlan Brick lbrick 638126
 * Nathan Malishev nmalishev 637410
 */

//This comparator will create a descending keyset - change the comparison if you want ascending
public class MoveComparator implements Comparator{
    Map map;

    public MoveComparator(Map map){
        this.map = map;
    }

    public int compare(Object o1, Object o2){
        if((int)map.get(o2) > (int)map.get(o1)){
            return 1;
        }else{
            return -1;
        }

    }

}
