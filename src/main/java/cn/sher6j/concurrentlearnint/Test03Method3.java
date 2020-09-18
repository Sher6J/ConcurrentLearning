package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 创建线程 方法三：FutureTask类配合Thread类
 * FutureTask 能够接收 Callable 类型的参数，用来处理有返回结果的情况
 * @author sher6j
 * @create 2020-09-17-19:45
 */

@Slf4j(topic = "c.Method3")
public class Test03Method3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("running");
                Thread.sleep(1000);
                return 100;
            }
        });

        // FutureTask类实现了Runnable接口，可以作为参数传递给Thread的构造方法
        Thread t = new Thread(task, "t1");
        t.start();

        // {} 表示占位符，指后面的task.get()
        log.debug("{}", task.get()); // 主线程等待其执行完毕
    }
}
