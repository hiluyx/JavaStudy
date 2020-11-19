package concurrentTest;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 100;
            }
        });
        Thread t1 = new Thread(task, "t1");
        t1.start();
        System.out.println(task.get());
    }
}
