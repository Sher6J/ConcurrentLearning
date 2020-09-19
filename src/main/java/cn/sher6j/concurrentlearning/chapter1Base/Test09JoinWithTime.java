package cn.sher6j.concurrentlearning.chapter1Base;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * join() 可以有参数，表达等待的时间，若超时则不再等待
 * @author sher6j
 * @create 2020-09-18-15:58
 */
@Slf4j(topic = "c.JoinWithTime")
public class Test09JoinWithTime {
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        test();
    }

    public static void test() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        long start = System.currentTimeMillis();
        t1.start();

        log.debug("join begin...");
        t1.join(1500);

        long end = System.currentTimeMillis();
        log.debug("r1:{}, r2:{}, cost:{}", r1, r2, end - start);
    }
}
