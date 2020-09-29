package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 死锁 一个线程需要同时获取多把锁，这时就容易发生死锁
 * t1 线程 获得 A对象 锁，接下来想获取 B对象 的锁
 * t2 线程 获得 B对象 锁，接下来想获取 A对象 的锁
 *
 * 定位死锁：
 * 1. 用jps查看java进程，然后用jstack查看栈信息
 * 2. 用jconsole工具
 * @author sher6j
 * @create 2020-09-25-20:41
 */
@Slf4j(topic = "c.死锁")
public class Test07DeadLock {
    public static void main(String[] args) {
        test();
    }

    private static void test() {
        Object A = new Object();
        Object B = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (A) {
                log.debug("lock A");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (B) {
                    log.debug("lock B");
                    log.debug("操作...");
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            synchronized (B) {
                log.debug("lock B");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (A) {
                    log.debug("lock A");
                    log.debug("操作...");
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
