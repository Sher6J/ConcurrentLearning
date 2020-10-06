package cn.sher6j.concurrentlearning.chapter5ConcurrencyTools;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JDK中的线程池
 * newFixedThreadPool返回固定核心线程数的线程池（救济线程数为0）
 * @author sher6j
 * @create 2020-10-06-14:47
 */
@Slf4j(topic = "c.Pool")
public class Test02FixedThreadPoolExecution {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger order = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "mypool_id" + order.getAndIncrement());
            }
        });
        for (int i = 0; i < 5; i++) {
            int j = i;
            pool.execute(() -> {
                log.debug("{}", j);
            });
        }
    }
}
