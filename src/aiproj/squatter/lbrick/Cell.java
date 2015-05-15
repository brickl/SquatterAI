package aiproj.squatter.lbrick;

/**
 * Created by lochiebrick on 16/05/2015.
 */
public class Cell {
    int row;
    int col;

    public Cell(int r, int c) {
        row = r;
        col = c;
    }

    public boolean equals(Object other) {

        if(!(other instanceof Cell))
            return false;

        Cell otherCell = (Cell)other;
        if(this.row == otherCell.row && this.col == otherCell.col)
            return true;

        return false;

    }

}
