package cn.sher6j.concurrentlearning.chapter5ConcurrencyTools;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * SingleThreadPool返回一个单线程的线程池
 * 自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，
 * 而线程池还会新建一个线程，保证池的正常工作
 * @author sher6j
 * @create 2020-10-06-14:59
 */
@Slf4j(topic = "c.Single")
public class Test03SingleTreadPoolExecution {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        /*
        自己单线程执行完1就异常停止了
         */
//        for (int i = 0; i < 7; i++) {
//            int j = i;
//            log.debug("{}", j);
//            if (j % 2 != 0) {
//                int m = 1 / 0;
//            }
//        }

        /*
        线程池中始终会保证有一个可用的线程执行任务
        15:05:04.602 [pool-1-thread-1] c.Single - 0
        15:05:04.606 [pool-1-thread-1] c.Single - 1
        15:05:04.607 [pool-1-thread-2] c.Single - 2
        15:05:04.607 [pool-1-thread-2] c.Single - 3
        15:05:04.607 [pool-1-thread-3] c.Single - 4
        15:05:04.607 [pool-1-thread-3] c.Single - 5
        15:05:04.608 [pool-1-thread-4] c.Single - 6
         */
        for (int i = 0; i < 7; i++) {
            int j = i;
            pool.execute(() -> {
                log.debug("{}", j);
                if (j % 2 != 0) {
                    int m = 1 / 0;
                }
            });
        }
    }
}
