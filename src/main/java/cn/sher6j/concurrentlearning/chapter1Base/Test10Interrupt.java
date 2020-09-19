package cn.sher6j.concurrentlearning.chapter1Base;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author sher6j
 * @create 2020-09-19-12:16
 */
@Slf4j(topic = "c.Interrupt")
public class Test10Interrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("sleep...");
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();
        TimeUnit.SECONDS.sleep(3);
        log.debug("interrupt t1");
        t1.interrupt();
        log.debug("打断标记：{}", t1.isInterrupted());
    }
}
