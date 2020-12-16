package cn.sher6j.concurrentlearning.chapter6ConcurrencyToolsAQS;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author sher6j
 * @create 2020-10-09-14:20
 */
@Slf4j(topic = "c.CDL")
public class Test05CountDownLatch {
    public static void main(String[] args) throws InterruptedException {
        test();
    }

    private static void test() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            log.debug("begin...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            log.debug("end...");
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            log.debug("end...");
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            latch.countDown();
            log.debug("end...");
        }).start();

        log.debug("waiting...");
        latch.await();
        log.debug("wait end...");
    }
}
