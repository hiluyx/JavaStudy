package cs_works.works4;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class MouthTaskThread implements Runnable{

    public static AtomicInteger lockCounter = new AtomicInteger(0);
    public static final List<DigLog.CarRecord> publicCarInList = new ArrayList<>();
    public static final List<DigLog.CarRecord> publicCarOutList = new ArrayList<>();

    private final List<DigLog.CarRecord> carInList = new ArrayList<>();
    private final List<DigLog.CarRecord> carOutList = new ArrayList<>();

    @Override
    public void run() {
        scanLists(carInList,carOutList);
        publicCarOutList.addAll(carOutList);
        publicCarInList.addAll(carInList);
        synchronized (DigLog.class) {
            if (lockCounter.incrementAndGet() == Adapter.MOUTH_NUM) {
                DigLog.class.notifyAll();
            }
        }
    }

    public static void scanLists(List<DigLog.CarRecord> carInList, List<DigLog.CarRecord> carOutList) {
        int index = 0;
        int index1 = 0;
        boolean isMatch = false;
        int match = 0;
        while (index < carInList.size()) {
            DigLog.CarRecord next = carInList.get(index);
            while (index1 < carOutList.size()) {
                DigLog.CarRecord carOutRecord = carOutList.get(index1);
                if (next.getCarNumber().equals(carOutRecord.carNumber)) { // 车牌匹配
                    isMatch = true;
                    match++;
                    DigLog.carInOutTime.incrementAndGet();
                    DigLog.carParkTime.addAndGet(carOutRecord.getDate().getTime() - next.getDate().getTime());
                    break;
                }
                index1 ++;
            }
            if (index1 != carOutList.size()) carOutList.remove(index1);
            index1 = 0;
            if (isMatch) {
                carInList.remove(index);
                isMatch = false;
            }
            else index ++;
        }
    }
}
