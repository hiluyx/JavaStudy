package cs_works.works4;

import lombok.Getter;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Getter
public class Adapter extends Thread{

    public static int MOUTH_NUM = 0;
    private final AtomicInteger carRecordListIndex = new AtomicInteger(0);
    private volatile boolean isDone;
    private Thread cur;
    private final Map<Integer, MouthTaskThread> threadMap = new HashMap<>();

    @SneakyThrows
    @Override
    public void run() {
        cur = Thread.currentThread();
        //System.out.println("adapter running...");
        while (true) {
            if (cur.isInterrupted()) {
                isDone = true;
                adapter();
                //System.out.println("adapter has done. total records: " + carRecordListIndex);
                break;
            }
            adapter();
        }
    }

    public void stopThis() {
        //System.out.println("adapter isInterrupted.");
        cur.interrupt();
    }

    private void adapter() {
        try {
            int l;
            synchronized (DigLog.carRecordList) {
                l = DigLog.carRecordList.size();
            }
            if (l == 0) {
                if (isDone) return;
                Thread.sleep(2);
            } else {
                for (int i = carRecordListIndex.get(); i < l; i ++, carRecordListIndex.incrementAndGet()) {
                    DigLog.CarRecord carRecord = DigLog.carRecordList.get(i);
                    MouthTaskThread attach = threadMap.get(carRecord.getMouth());
                    if (attach == null) {
                        attach = new MouthTaskThread();
                        MOUTH_NUM ++;
                        threadMap.put(carRecord.getMouth(), attach);
                    }

                    if (carRecord.isIn()) {
                        attach.getCarInList().add(carRecord);
                    } else {
                        attach.getCarOutList().add(carRecord);
                    }
                }
            }
        } catch (InterruptedException e) {
            cur.interrupt();
        }
    }
}
