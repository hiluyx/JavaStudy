package cs_works.works3;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CalPiPlus {
    static int threadNum = 8;
    static AtomicInteger calTime = new AtomicInteger(0);
    static AtomicInteger integer = new AtomicInteger(0);
    static int theNumberOfCircles=1000;
    static int preciseFigures=1000;
    static BigDecimal _tempt = new BigDecimal(0);
    static BigDecimal PI = null;//PI

    @EqualsAndHashCode(callSuper = true)
    @Data
    private static class CalThread extends Thread {
        private int left;
        private int right;
        private BigDecimal tempt = new BigDecimal(0);

        CalThread(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void run() {
            System.out.println(toString());
            long s = System.currentTimeMillis();
            while (true) {
                for (int i = left;i<=right;i ++) tempt = runC(i, tempt);
                synchronized (CalPiPlus.class) {
                    if (integer.incrementAndGet() < threadNum){
                        try {
                            CalPiPlus.class.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        preciseFigures += 100;
                        System.out.println(Thread.currentThread().getName() + ", preciseFigures=" + preciseFigures);
                        calTime.incrementAndGet();
                        integer.set(0);
                        CalPiPlus.class.notifyAll();
                    }
                    if (System.currentTimeMillis() - s >= 180000) break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        List<CalThread> calThreads = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            CalThread calThread = new CalThread(i * theNumberOfCircles / threadNum,
                    i == threadNum - 1 ? theNumberOfCircles : (i + 1) * theNumberOfCircles / threadNum);
            calThreads.add(calThread);
        }
        long startTime = System.nanoTime();//开始时间
        for (CalThread c : calThreads) {
            c.start();
        }
        for (CalThread c : calThreads) {
            c.join();
            _tempt = _tempt.add(c.getTempt());
        }

        PI = _tempt.divide(BigDecimal.valueOf(64));//PI=tempt/64
        long endTime = System.nanoTime();//结束时间
        System.out.println("共用时："
                + (endTime - startTime) / 1000000000.0 + "秒");

        BigDecimal s_PI = PI.setScale(preciseFigures, BigDecimal.ROUND_DOWN).divide(new BigDecimal(calTime.intValue()), preciseFigures, BigDecimal.ROUND_HALF_DOWN);
        File file = new File((CalPiPlus.class.getClassLoader().getResource("") + "PI.txt").substring(6));
        FileOutputStream op = new FileOutputStream(file);
        op.write(s_PI.toString().getBytes());
        op.close();
    }

    static BigDecimal runC(int i, BigDecimal tempt) {
        // System.out.println(Thread.currentThread().getName() + ", ---- " + i);
        BigDecimal a;//公式的第一项
        BigDecimal b;//公式的第二项
        BigDecimal c;//公式的第三项
        BigDecimal d;//公式的第四项
        BigDecimal e;//公式的第五项
        BigDecimal f;//公式的第六项
        BigDecimal g;//公式的第七项
        BigDecimal h;//公式的第八项
        //a=(-1/1024)^i
        a = BigDecimal.valueOf(-1).divide(BigDecimal.valueOf(1024)).pow(i);
        //返回其值为 (this^n) 的 BigDecimal

        //b=32/(4i+1)
        BigDecimal multiply = BigDecimal.valueOf(4).multiply(BigDecimal.valueOf(i));
        b = BigDecimal.valueOf(32).divide(multiply.add(BigDecimal.valueOf(1)), preciseFigures+3,BigDecimal.ROUND_DOWN);
        //为求精确,计算精度比输出值多三位
        //ROUND_DOWN接近零的舍入模式（截取）

        //c=1/(4n+3)
        c = BigDecimal.valueOf(1).divide(multiply.add(BigDecimal.valueOf(3)), preciseFigures+3,BigDecimal.ROUND_DOWN);

        //d=256/(10n+1)
        BigDecimal multiply1 = BigDecimal.valueOf(10).multiply(BigDecimal.valueOf(i));
        d = BigDecimal.valueOf(256).divide(multiply1.add(BigDecimal.valueOf(1)), preciseFigures+3,BigDecimal.ROUND_DOWN);

        //e=64/(10n+3)
        e = BigDecimal.valueOf(64).divide(multiply1.add(BigDecimal.valueOf(3)), preciseFigures+3,BigDecimal.ROUND_DOWN);

        //f=4/(10n+5)
        f = BigDecimal.valueOf(4).divide(multiply1.add(BigDecimal.valueOf(5)), preciseFigures+3,BigDecimal.ROUND_DOWN);

        //g=4/(10n+7)
        g = BigDecimal.valueOf(4).divide(multiply1.add(BigDecimal.valueOf(7)), preciseFigures+3,BigDecimal.ROUND_DOWN);

        //h=1/(10n+9)
        h = BigDecimal.valueOf(1).divide(multiply1.add(BigDecimal.valueOf(9)), preciseFigures+3,BigDecimal.ROUND_DOWN);

        //tempt=tempt+a(d+h-b-c-e-f-g)  循环累加
        return tempt.add(a.multiply(d.add(h).subtract(b).subtract(c).subtract(e).subtract(f).subtract(g)));
    }
}
