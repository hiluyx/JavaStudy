package cs_works.works4;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Getter
public class Adapter extends Thread{

    private AtomicInteger carRecordListIndex = new AtomicInteger(0);
    private volatile boolean isDone;
    private Thread cur;
    private final Map<Integer, MouthTaskThread> threadMap = new HashMap<>();

    @Override
    public void run() {
        cur = Thread.currentThread();
        while (true) {
            if (cur.isInterrupted()) {
                isDone = true;
                adapter();
                break;
            }
            adapter();
        }
        System.out.println(carRecordListIndex.intValue());
    }

    public void stopThis() {
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
