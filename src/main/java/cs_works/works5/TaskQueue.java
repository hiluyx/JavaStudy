package cs_works.works5;

import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue<T> {
    public int BUFFER_SIZE;
    private final T[] cells;
    private int head = 0;
    private int tail = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public boolean isEmpty() {
        return tail == head;
    }

    public TaskQueue(int bufferSize) {
        BUFFER_SIZE = bufferSize;
        cells = (T[])new Object[bufferSize];
    }

    public boolean add(T cell) {
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

    public T consume() {
        if (tail == head) {
            return null;
        } else {
            lock.lock();
            try {
                //System.out.println("consumer gain lock.");
                if (tail == head) {
                    return null;
                } else {
                    T cell = cells[tail];
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
