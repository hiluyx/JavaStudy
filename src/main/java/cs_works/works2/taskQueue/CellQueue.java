package cs_works.works2.taskQueue;


import cs_works.works2.space.Cell;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author best lu
 * @since 2020/11/17
 */
public class CellQueue {
    public int BUFFER_SIZE;
    private final Cell[] cells;
    private int head = 0;
    private int tail = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public boolean isEmpty() {
        return tail == head;
    }

    public CellQueue(int bufferSize) {
        BUFFER_SIZE = bufferSize;
        cells = new Cell[BUFFER_SIZE];
    }

    public boolean add(Cell cell) {
        //if ((head +1) % BUFFER_SIZE == tail) return false;
        lock.lock();
        try {
            //System.out.println("adder gain lock.");
            if ((head +1) % BUFFER_SIZE == tail) {
                return false;
            } else {
                cells[head] = cell;
                head = (head + 1) % BUFFER_SIZE;
            }
            //System.out.println("add one cell. --"+head+","+tail);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public Cell consume() {
        if (tail == head) {
            return null;
        } else {
            lock.lock();
            try {
                //System.out.println("consumer gain lock.");
                if (tail == head) {
                    return null;
                } else {
                    Cell cell = cells[tail];
                    tail = (tail + 1) % BUFFER_SIZE;
                    //System.out.println("consume one cell. --"+head+","+tail);
                    return cell;
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
