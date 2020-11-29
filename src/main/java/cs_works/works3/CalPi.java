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
    private static class CalThread implements Callable<Double> {
        private int left;
        private int right;

        CalThread(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Double call() {
            double serCal = 0.0;
            int n = left;
            double temp;
            int s = (n%2 == 0) ? -1 : 1;
            while (n < right) {
                temp = s*(1.0 / (2 * n - 1));
                s = -s;
                n ++;
                serCal += temp;
            }
            return serCal;
        }
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int thread_num = 4;
        int _e = 100000000;
        int n = (_e + 1) / 2 + 1;
        List<CalThread> calThreads = new ArrayList<>();
        for (int i = 0; i < thread_num; i ++) {
            calThreads.add(new CalThread(i * (n / thread_num) + 1,
                    (i == thread_num - 1) ?
                            n : (i + 1) * (n / thread_num)));
        }

        long sT = System.currentTimeMillis();
        Double pi = 0.0;
        List<FutureTask<Double>> piS = new ArrayList<>(calThreads.size());

        for (CalThread c : calThreads) {
            System.out.println("start-" + c.toString());
            FutureTask<Double> f = new FutureTask<>(c);
            new Thread(f).start();
            piS.add(f);
        }

        for (FutureTask<Double> f : piS) {
            pi += f.get();
        }

        long eT = System.currentTimeMillis() - sT;
        System.out.println("pi: "+pi*4);
        System.out.println("total time: " + eT);
    }
}
