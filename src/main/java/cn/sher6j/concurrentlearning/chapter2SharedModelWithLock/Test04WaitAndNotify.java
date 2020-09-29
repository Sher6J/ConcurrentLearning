package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

/**
 * @author sher6j
 * @create 2020-09-22-10:14
 */
public class Test04WaitAndNotify {
    static final Object lock = new Object(); // 作为锁对象最好加上final

    public static void main(String[] args) {
        // java.lang.IllegalMonitorStateException
        // 必须先拥有锁，称为锁的主人才可以进行wait，否则报上面异常
//        try {
//            lock.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
