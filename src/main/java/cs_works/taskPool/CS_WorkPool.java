package cs_works.taskPool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author best lu
 * @since 2020/11/17
 */
public class CS_WorkPool {
    private static final int QUEUE_SIZE = 8;//缓冲队列队长
    private static final int ALIVE_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 10;
    private static final int KEEP_ALIVE_TIME = 2;
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(
            ALIVE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(QUEUE_SIZE),
            new WorkFactory()
    );

    public static void execute(Runnable runnable) {
        THREAD_POOL.execute(runnable);
    }

    static final class WorkFactory implements ThreadFactory {

        private static final String prefix = "work-pool-thread-";
        private static final AtomicInteger integer = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, prefix + integer.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }
}
