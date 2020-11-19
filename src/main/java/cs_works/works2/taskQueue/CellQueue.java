package cs_works.works2.taskQueue;


import cs_works.works2.space.Cell;

/**
 * @author best lu
 * @since 2020/11/17
 */
public class CellQueue {
    public int BUFFER_SIZE;
    private final Cell[] cells;
    private int head = 0;
    private int tail = 0;

    public boolean isEmpty() {
        return tail == head;
    }

    public CellQueue(int bufferSize) {
        BUFFER_SIZE = bufferSize;
        cells = new Cell[BUFFER_SIZE];
    }

    public boolean add(Cell cell) {
        synchronized (this) {
            if ((head +1) % BUFFER_SIZE == tail) {
                return false;
            } else {
                cells[head] = cell;
                head = (head + 1) % BUFFER_SIZE;
            }
            return true;
        }
    }

    public Cell consume() {
        synchronized (this) {
            if (tail == head) {
                return null;
            } else {
                Cell cell = cells[tail];
                tail = (tail + 1) % BUFFER_SIZE;
                return cell;
            }
        }
    }
}
