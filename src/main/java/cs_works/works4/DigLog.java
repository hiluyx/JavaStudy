package cs_works.works4;

import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class DigLog {

    static final String stu_number = "201816060202";
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static final List<CarRecord> carRecordList = new ArrayList<>();
    static RandomAccessFile reader;
    static FileChannel channel;
    static ByteBuffer buffer = ByteBuffer.allocate(1024);
    static boolean isDone = false;
//    static Map<String/*carNumber*/,CarRecord> carInMap = new HashMap<>();
//    static Map<String/*carNumber*/,CarRecord> carOutMap = new HashMap<>();
//    static Map<String/*carNumber*/,CarRecord> second_carInMap = new HashMap<>();
//    static Map<String/*carNumber*/,CarRecord> second_carOutMap = new HashMap<>();


    static AtomicInteger carInOutTime = new AtomicInteger(0);
    static AtomicLong carParkTime = new AtomicLong(0);

    @Data
    public static class CarRecord {
        boolean in;
        Date date;
        int mouth;
        String carNumber;

        CarRecord(Date date,int mouth, String carNumber, boolean inOrOut) {
            this.carNumber = carNumber;
            this.date = date;
            this.in = inOrOut;
            this.mouth = mouth;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        reader = new RandomAccessFile(
                new File(
                        (DigLog.class.getClassLoader().getResource("") +"cs_works_data/cars.txt")
                                .substring(6)),
                "r"
        );

        channel = reader.getChannel();

        System.out.println("Start ReadFile:"+getStandardTime(0));
        long sT = System.currentTimeMillis();

        try {
            long read_sT = System.currentTimeMillis();
            Adapter adapter = new Adapter();

            adapter.start();
            readLine();
            long read_eT = System.currentTimeMillis() - read_sT;
            System.out.println("End ReadFile:  " + getStandardTime(read_eT));

            adapter.stopThis();

            Set<Map.Entry<Integer, MouthTaskThread>> entries = adapter.getThreadMap().entrySet();

            for (Map.Entry<Integer, MouthTaskThread> entry : entries) {
                entry.getValue().start();
            }

            for (Map.Entry<Integer, MouthTaskThread> entry : entries) {
                entry.getValue().join();
            }

            MouthTaskThread.scanLists(MouthTaskThread.publicCarInList, MouthTaskThread.publicCarOutList);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

//        for (CarRecord carRecord : carRecordList){
//            if (carRecord.isIn()) {
//                if (carInMap.put(carRecord.getCarNumber(),carRecord) != null) {
//                    second_carInMap.put(carRecord.getCarNumber(), carRecord);
//                }
//            } else {
//                if (carOutMap.put(carRecord.getCarNumber(),carRecord) != null){
//                    //System.out.println(carRecord.toString());
//                    second_carOutMap.put(carRecord.getCarNumber(), carRecord);
//                }
//            }
//        }
//
//        /*
//         * 开始统计匹配已经分为in和out的两个Map汽车数据
//         */
//        CarRecord carIn;
//        for (Map.Entry<String,CarRecord> entry : carOutMap.entrySet()) {
//            carIn = carInMap.get(entry.getKey());
//            long park_thisTime = entry.getValue().getDate().getTime() - carIn.getDate().getTime();
//            carParkTime.addAndGet(park_thisTime);
//            carInOutTime.incrementAndGet();
//        }
//
//        /*
//         * 开始统计匹配有两次进出库的汽车数据，因为Map的Key不能重复，所以要再统计一遍
//         */
//        for (Map.Entry<String,CarRecord> entry : second_carOutMap.entrySet()) {
//            carIn = second_carInMap.get(entry.getKey());
//            long park_thisTime = entry.getValue().getDate().getTime() - carIn.getDate().getTime();
//            carParkTime.addAndGet(park_thisTime);
//            carInOutTime.incrementAndGet();
//        }

        System.out.println("End Process: "+(System.currentTimeMillis() - sT)/1000.0 + "s");
        System.out.println("carInOutTime: " + carInOutTime + " time cars");
        System.out.println("carParkTime: " + carParkTime.longValue() + "s");
    }

    static void readLine() throws IOException, ParseException {
        boolean get = false;
        int re0 = 0;
        while(channel.read(buffer) > 0) {
            buffer.flip();
            if (get) {
                buffer.position(re0);
                buffer.get();
            }
            while (buffer.limit() - buffer.position() > 45) {
                byte[] bs = new byte[45];
                byte b;
                int l = 0;
                b = buffer.get();
                while (b != 13) {
                    bs[l] = b;
                    b = buffer.get();
                    l++;

                    if (b == 13) break;
                }
                String gbkLine = new String(bs, Charset.forName("GBK"));
                gbkLine = gbkLine.trim();
                // System.out.println(gbkLine);

                String[] line_split = gbkLine.split(",");
                if (line_split[1].equals(stu_number)) {
                    int mouth = Integer.parseInt(line_split[0].substring(5,7));
                    CarRecord record = new CarRecord(sdf.parse(line_split[0]),mouth, line_split[2], line_split[3].equals("in"));
                    synchronized (carRecordList) {
                        carRecordList.add(record);
                    }
                }

                if (buffer.position() != buffer.limit()){
                    get = false;
                    buffer.get(); // 去掉回车byte 10
                } else {
                    re0 = buffer.position();
                    get = true;
                    break;
                }
                buffer.compact(); // 未读取完的bytes前移
                buffer.flip();
            }
            int limit = buffer.limit();

            buffer.limit(buffer.capacity());
            buffer.position(limit);
        }
        isDone = true;
    }

    public static String getStandardTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        Date date = new Date(timestamp);
        sdf.format(date);
        return sdf.format(date);
    }
}
