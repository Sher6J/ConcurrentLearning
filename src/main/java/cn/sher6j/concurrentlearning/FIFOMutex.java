package cn.sher6j.concurrentlearning;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * 先入先出非重入锁————park/unpark实现（见于LockSupport源码）
 * @author sher6j
 * @create 2020-12-07-9:58
 */
public class FIFOMutex {
    /**
     * 是否上锁
     */
    private final AtomicBoolean locked = new AtomicBoolean(false);

    /**
     * 等待队列
     */
    private final Queue<Thread> waiters
            = new ConcurrentLinkedQueue<>();

    public void lock() {
        boolean wasInterrupted = false;
        Thread current = Thread.currentThread();
        waiters.add(current);
        // Block while not first in queue or cannot acquire lock
        while (waiters.peek() != current ||
                !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            // ignore interrupts while waiting
            if (Thread.interrupted()) {
                wasInterrupted = true;
            }
        }
        // 删除队头线程
        waiters.remove();
        // reassert interrupt status on exit
        if (wasInterrupted) {
            current.interrupt();
        }
    }

    public void unlock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}