package cs_works.works5;

import cs_works.taskPool.CS_WorkPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PrimeNumberJudge {
    static final int producer_thread_num = 1;
    static final int judge_thread_num = 4;
    static final AtomicInteger judgeAmount_BeDone = new AtomicInteger(0);
    static final AtomicInteger producerAmount_BeDone = new AtomicInteger(0);
    static TaskQueue<NumberTask> taskQueue = new TaskQueue<>(10000000);
    static AtomicInteger integer_random_times = new AtomicInteger(0);
    static AtomicInteger integer_prime = new AtomicInteger(0);
    static final List<PrimeJudge> judgeList = new ArrayList<>();
    static final int TIMES_PRODUCE = 2000000;
    static final long BASE_NUMBER = 2000000000;

    public static void main(String[] args) throws InterruptedException {

        long sT = System.currentTimeMillis();
        for (int i = 0; i < producer_thread_num; i++) {
            CS_WorkPool.execute(new NumberProducer());
        }

        for (int i = 0; i < judge_thread_num; i++) {
            PrimeJudge primeJudge = new PrimeJudge();
            judgeList.add(primeJudge);
            CS_WorkPool.execute(primeJudge);
        }

        synchronized (PrimeNumberJudge.class) {
            PrimeNumberJudge.class.wait();
        }

        System.out.println(integer_random_times);
        System.out.println(integer_prime);
        System.out.println("Total time: " + (System.currentTimeMillis() - sT)/1000.0);
    }

    /**
     * 素数任务类，生产者与消费者的公用对象
     */
    static class NumberTask {
        long number;

        NumberTask(long number) {
            this.number = number;
        }

        public boolean isPrime() {
            if (number==2) {
                return true;
            }
            if(number<2){
                return false;
            }
            double max=Math.sqrt(number)+1;
            for (int i = 2; i < max; i++) {
                if (number%i==0) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 生产者线程单元
     */
    static class NumberProducer implements Runnable {
        @Override
        public void run() {
            System.out.println("NumberProducer " + Thread.currentThread().getName() + " start.");
            long l1 = BASE_NUMBER / 2;
            while (integer_random_times.incrementAndGet() != TIMES_PRODUCE) {
                long l = (long) (Math.random() * l1 + BASE_NUMBER);
                while (!taskQueue.add(new NumberTask(l))) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            /*
            判断是否 所有 生产者线程都已完工，若是，打断 所有 消费者当前的工作。
             */
            if (producerAmount_BeDone.incrementAndGet() == producer_thread_num) {
                for (PrimeJudge p : judgeList) {
                    p.stopConsumer();
                }
            }
        }
    }

    /**
     * 消费者处理单元
     */
    static class PrimeJudge implements Runnable {
        Thread cur;

        @Override
        public void run() {
            cur = Thread.currentThread();
            System.out.println("PrimeJudge " + cur.getName() + " start.");
            while (true) {
                /*
                先判断有没有被打断。
                 */
                if (cur.isInterrupted()) {
                    /*
                    刚好消费者线程被生产者被打断而且队列为空
                     */
                    if (taskQueue.isEmpty()) {
                        // 队列为空返回true
                        break;
                    }
                    /*
                    队列有未处理的Task，处理完再结束。
                     */
                    NumberTask consume = taskQueue.consume();
                    while (consume != null) {
                        if (consume.isPrime()) {
                            integer_prime.incrementAndGet();
                        }
                        consume = taskQueue.consume();
                    }
                    break;
                }
                /*
                消费者没有被打断，说明还有生产者在工作。
                 */
                try {
                    NumberTask consume = taskQueue.consume();
                    if (consume == null) {
                        /*
                        消费者过快，先休息一会。
                         */
                        Thread.sleep(10);
                        // 重新开始循环。
                        continue;
                    }
                    // 队列有任务
                    if (consume.isPrime()) {
                        integer_prime.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    cur.interrupt();
                }
            }

            /*
            判断是否所有消费者都已经完工，若是，唤醒主线程。
             */
            if (judgeAmount_BeDone.incrementAndGet() == judge_thread_num) {
                synchronized (PrimeNumberJudge.class) {
                    PrimeNumberJudge.class.notifyAll();
                }
            }
        }

        public void stopConsumer() {
            cur.interrupt();
        }
    }
}

