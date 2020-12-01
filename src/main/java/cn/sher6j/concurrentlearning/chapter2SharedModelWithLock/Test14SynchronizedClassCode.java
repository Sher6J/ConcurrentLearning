package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

/**
 * @author sher6j
 * @create 2020-12-01-18:53
 */
public class Test14SynchronizedClassCode {
    static final Object lock = new Object();
    static int count = 0;

    public static void main(String[] args) {
        synchronized (lock) {
            count++;
        }
    }
}
