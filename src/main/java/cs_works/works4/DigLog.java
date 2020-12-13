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

    static final String stu_number = "201825010123";
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static List<CarRecord> carRecordList = new ArrayList<>();
    static RandomAccessFile reader;
    static FileChannel channel;
    static ByteBuffer buffer = ByteBuffer.allocate(1024);
    static boolean isDone = false;
    static Map<String/*carNumber*/,CarRecord> carInMap = new HashMap<>();
    static Map<String/*carNumber*/,CarRecord> carOutMap = new HashMap<>();
    static Map<String/*carNumber*/,CarRecord> extra_carInMap = new HashMap<>();
    static Map<String/*carNumber*/,CarRecord> extra_carOutMap = new HashMap<>();

    static ReentrantLock mapLock = new ReentrantLock();

    static AtomicInteger carInOutTime = new AtomicInteger(0);
    static AtomicLong carParkTime = new AtomicLong(0);
    @Data
    private static class CarRecord {
        boolean in;
        Date date;
        String carNumber;

        CarRecord(Date date, String carNumber, boolean inOrOut) {
            this.carNumber = carNumber;
            this.date = date;
            this.in = inOrOut;
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

        long sT = System.currentTimeMillis();

        try {
            readLine();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        for (CarRecord carRecord : carRecordList){
            // System.out.println("consume: " + carRecord.toString());
            if (carRecord.isIn()) {
                if (carInMap.put(carRecord.getCarNumber(),carRecord) != null) {
                    System.out.println("in");
                    extra_carInMap.put(carRecord.getCarNumber(), carRecord);
                }
            } else {
                if (carOutMap.put(carRecord.getCarNumber(),carRecord) != null){
                    //System.out.println("out");
                    extra_carOutMap.put(carRecord.getCarNumber(), carRecord);
                }
            }
        }

        CarRecord carIn;
        for (Map.Entry<String,CarRecord> entry : carOutMap.entrySet()) {
            carIn = carInMap.get(entry.getKey());
            long park_thisTime = entry.getValue().getDate().getTime() - carIn.getDate().getTime();
            carParkTime.addAndGet(park_thisTime);
            carInOutTime.incrementAndGet();
            //carInMap.remove(entry.getKey());
        }

        for (Map.Entry<String,CarRecord> entry : extra_carOutMap.entrySet()) {
            carIn = extra_carInMap.get(entry.getKey());
            if (carIn == null) continue;
            long park_thisTime = entry.getValue().getDate().getTime() - carIn.getDate().getTime();
            carParkTime.addAndGet(park_thisTime);
            carInOutTime.incrementAndGet();
        }

        System.out.println("total time: "+(System.currentTimeMillis() - sT));
        System.out.println("carInOutTime: " + carInOutTime);
        System.out.println("carParkTime: " + carParkTime);
    }

    static void readLine() throws IOException, ParseException {
        while(channel.read(buffer) > 0) {
            buffer.flip();
            boolean get = false;
            if (get) buffer.get();
            while (buffer.limit() - buffer.position() > 45) {
                byte[] bs = new byte[45];
                byte b;
                b = buffer.get();
                int l = 1;
                while (b != 13) {
                    bs[l - 1] = b;
                    b = buffer.get();
                    if (b == 13) break;
                    l++;
                }
                String gbkLine = new String(bs, Charset.forName("GBK"));
                gbkLine = gbkLine.trim();
                // System.out.println(gbkLine);

                String[] line_split = gbkLine.split(",");
                if (line_split[1].equals(stu_number)) {
                    CarRecord record = new CarRecord(sdf.parse(line_split[0]), line_split[2], line_split[3].equals("in"));
                    carRecordList.add(record);
                }

                if (buffer.position() != buffer.limit()){
                    get = false;
                    buffer.get(); // 去掉回车byte 10
                } else {
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
}
