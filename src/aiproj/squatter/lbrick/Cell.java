package aiproj.squatter.lbrick;

/**
 * Created by lochiebrick on 16/05/2015.
 * Lochlan Brick lbrick 638126
 * Nathan Malishev nmalishev 637410
 */

public class Cell {
    int row;
    int col;

    public Cell(int r, int c) {
        row = r;
        col = c;
    }

    /**
     * Checks to see if two cells are the same
     * @param other Another cell
     * @return true if the cells are equal, false if not
     */
    public boolean equals(Object other) {

        if(!(other instanceof Cell))
            return false;

        Cell otherCell = (Cell)other;
        if(this.row == otherCell.row && this.col == otherCell.col)
            return true;

        return false;

    }

}
