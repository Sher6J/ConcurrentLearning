package cn.sher6j.concurrentlearning.chapter2SharedModel;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁：可打断
 * 线程 t1 需要锁，主线程打断 t1 告诉其不要再等了
 * 一种被动的可打断，这样可以防止 t1 一直等待锁造成死锁
 * @author sher6j
 * @create 2020-09-26-14:24
 */
@Slf4j(topic = "c.Interrupt")
public class Test11ReentrantLockInterrupt {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                log.debug("尝试获得锁");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获得锁，被打断");
                return;
            }
            try {
                log.debug("获得锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock(); // 主线程先获得锁

        t1.start();

        TimeUnit.SECONDS.sleep(1);
        log.debug("打断t1");
        t1.interrupt();
    }
}
