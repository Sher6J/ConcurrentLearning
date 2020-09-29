package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁：可重入
 * 同一个线程如果首次获得了这把锁，那么因为它是这把锁的拥有者，因此有权利再次获取这把锁
 * @author sher6j
 * @create 2020-09-26-14:17
 */
@Slf4j(topic = "c.ReentrantLock")
public class Test10ReentrantLock {
    private static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        lock.lock(); // 用lock()加锁则不可打断，可打断则用lockInterruptibly()
        try {
            log.debug("enter main");
            m1(); // 锁重入
        } finally {
            lock.unlock();
        }
    }

    public static void m1() {
        lock.lock();
        try {
            log.debug("enter m1");
            m2(); // 锁重入
        } finally {
            lock.unlock();
        }
    }

    public static void m2() {
        lock.lock();
        try {
            log.debug("enter m2");
        } finally {
            lock.unlock();
        }
    }
}
