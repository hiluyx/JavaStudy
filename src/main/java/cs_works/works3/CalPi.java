package cs_works.works3;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CalPi {
    /*
    2n - 1 > 100,000,000
    2n > 100,000,001

    n > 50,000,001
    [left,right]
    [1,n/thread_num], [n/thread_num,2*n/thread_num], ...
     */

    @Data
    private static class CalThread implements Runnable {
        private long left;
        private long right;
        private double serCal;

        CalThread(long left, long right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void run() {
            serCal = 0.0;
            long n = left;
            double temp;
            double s = (n%2 == 0) ? -1.0 : 1.0;
            while (n < right) {
                temp = s*(1.0 / (2 * n - 1));
                s = -s;
                n ++;
                serCal += temp;
            }
        }
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] thread_num = {1,2,4,8};
        long _e = 1000000000;
        long n = (_e*10 + 1) / 2 + 1;

        List<List<CalThread>> calThreadsList = new ArrayList<>();
        for (int i : thread_num) {
            List<CalThread> calThreads = new ArrayList<>();
            if (i == 1) calThreads.add(new CalThread(1,n));
            else for (int j = 0; j < i; j ++) {
                    calThreads.add(new CalThread(j * (n / i) + 1,
                            (j == i - 1) ?
                                    n : (j + 1) * (n / i)));
                }
            calThreadsList.add(calThreads);
        }
//        System.out.println("thread_num_1: "+threadStart(1, calThreadsList.get(0)) / 1000 + "s");
//        System.out.println("thread_num_2: "+threadStart(2, calThreadsList.get(1)) / 1000 + "s");
//        System.out.println("thread_num_4: "+threadStart(4, calThreadsList.get(2)) / 1000 + "s");
        System.out.println("thread_num_8: "+threadStart(8, calThreadsList.get(3)) / 1000 + "s");
    }

    public static double threadStart(int threadNum,List<CalThread> calThreads) throws ExecutionException, InterruptedException {
        // long sT = System.currentTimeMillis();
        double pi = 0.0;
        List<Thread> piS = new ArrayList<>(calThreads.size());
        long sT = System.currentTimeMillis();
        for (CalThread c : calThreads) {
            //System.out.println("start-" + c.toString());
            Thread t = new Thread(c);
            t.start();
            piS.add(t);
        }

        for (Thread f : piS) {
            f.join();
        }

        long eT = System.currentTimeMillis() - sT;

        for (CalThread c : calThreads) {
            pi += c.getSerCal();
        }

       // long eT = System.currentTimeMillis() - sT;
        System.out.println("pi: " + pi * 4 +" by thread_num_" + threadNum);
        return eT;
    }
}
