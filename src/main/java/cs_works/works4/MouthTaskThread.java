package cs_works.works4;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Getter
public class MouthTaskThread extends Thread{

    private static List<DigLog.CarRecord> publicCarInList = new ArrayList<>();
    private static List<DigLog.CarRecord> publicCarOutList = new ArrayList<>();

    private List<DigLog.CarRecord> carInList = new ArrayList<>();
    private List<DigLog.CarRecord> carOutList = new ArrayList<>();

    @Override
    public void run() {
        int lastIndex = scanLists(carInList,carOutList);
    }

    public static int scanLists(List<DigLog.CarRecord> carInList, List<DigLog.CarRecord> carOutList) {
        Iterator<DigLog.CarRecord> iterator = carInList.iterator();
        while (iterator.hasNext()) {
            DigLog.CarRecord next = iterator.next();
            int i = 0;
            while (i < carOutList.size()) {
                if (next.getCarNumber().equals(carOutList.get(i).carNumber)) {

                }
            }
            carOutList.remove(i);
            iterator.remove();
        }
    }
}
