package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 重入锁的锁超时
 * 由线程自身决定不再等待锁
 * @author sher6j
 * @create 2020-09-26-14:34
 */
@Slf4j(topic = "c.OutOfTime")
public class Test12ReentrantLockOutofTime {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("尝试获得锁");
            try {
                // 等待1秒
                if (!lock.tryLock(1, TimeUnit.SECONDS)) {
                    log.debug("获取不到锁");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获取不到锁");
                return;
            }
            try {
                log.debug("获得锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock(); // 主线程先获得锁
        log.debug("获得锁");

        t1.start();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock(); // 0.5秒后主线程释放锁
        log.debug("释放锁");
    }
}
