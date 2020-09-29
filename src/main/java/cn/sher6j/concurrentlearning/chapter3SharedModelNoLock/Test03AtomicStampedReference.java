package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author sher6j
 * @create 2020-09-29-20:12
 */
@Slf4j(topic = "c.test")
public class Test03AtomicStampedReference {
    // 0 为版本号
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        String prev = ref.getReference(); // 值
        int stamp = ref.getStamp(); // 版本号
        otherMethod();
        TimeUnit.SECONDS.sleep(1);
        log.debug("change A->C {}", ref.compareAndSet(prev, "C", stamp, stamp + 1));
    }

    private static void otherMethod() throws InterruptedException {
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.debug("change A->B {}", ref.compareAndSet("A", "B", stamp, stamp + 1));
        }, "t2").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.debug("change B->A {}", ref.compareAndSet("B", "A", stamp, stamp + 1));
        }, "t3").start();
    }
}
