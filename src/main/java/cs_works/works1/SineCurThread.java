package cs_works.works1;

import lombok.SneakyThrows;

public class SineCurThread implements Runnable {

    final double SPLIT = 0.01;// 角度的分割
    final int COUNT = (int) (2 / SPLIT);// 2PI分割的次数，也就是2/0.01个，正好是一周
    final double PI = Math.PI;
    final int INTERVAL = 200;// 时间间隔

    @SneakyThrows
    @Override
    public void run() {
        long[] busySpan = new long[COUNT];
        long[] idleSpan = new long[COUNT];
        int half = INTERVAL / 2;
        double radian = 0.0;
        for (int i = 0; i < COUNT; i++) {
            busySpan[i] = (long) (half + (Math.sin(PI * radian) * half));
            idleSpan[i] = INTERVAL - busySpan[i];
            radian += SPLIT;
        }
        long startTime;
        int j = 0;
        while (true) {
            j = j % COUNT;
            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < busySpan[j])
                ;
            Thread.sleep(idleSpan[j]);
            j++;
        }
    }
}
