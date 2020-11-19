package cs_works.works2.space;

import lombok.Data;

/**
 * @author best lu
 * @since 2020/11/17
 */
@Data
public class Cell {
    private int x;
    private int y;
    int isLived;
    public Cell(int x, int y, int isLived) {
        this.x = x;
        this.y = y;
        this.isLived = isLived;
    }
}
