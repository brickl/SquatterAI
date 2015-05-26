package aiproj.squatter.lbrick.MinimaxSearch;

import java.util.Comparator;
import java.util.Map;

/**
 * Created by nathan on 25/05/15.
 */

//This comparator will create a descending keyset - change the comparison if you want ascending
public class MyComparator implements Comparator{
    Map map;

    public MyComparator(Map map){
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
