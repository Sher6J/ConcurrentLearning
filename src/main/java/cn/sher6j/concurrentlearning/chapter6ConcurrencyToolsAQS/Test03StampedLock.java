package cn.sher6j.concurrentlearning.chapter6ConcurrencyToolsAQS;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock，JDK8 引入
 * ReadWriteLock的读读还是有锁
 * 而StampedLock升级为先乐观读
 *
 * 是不是一定比ReadWriteLock好呢？
 * 1. StampedLock不支持条件变量，即不能使用 wait / notify
 * 2. StampedLock不支持可重入
 * @author sher6j
 * @create 2020-10-09-10:35
 */
@Slf4j(topic = "c.SL")
public class Test03StampedLock {
    public static void main(String[] args) {
        DataContainerStamped dcs = new DataContainerStamped(1);
//        readAndRead(dcs);
        readAndWrite(dcs);
    }

    /**
     * 读————写
     * 1. 首先读线程进行乐观读（无锁）
     * 2. 然后写线程进行写时获得写锁并更新戳
     * 3. 读线程发现戳失效然后要进行锁升级为读锁
     *    但此时由于读写锁互斥，写线程占有读锁故读线程需要等待读锁
     * 4. 写完成释放写锁
     * 5. 读线程升级读锁成功
     * 10:52:45.342 [t1] c.DCS - optimistic read locking... 256
     * 10:52:45.841 [t2] c.DCS - write lock 384
     * 10:52:46.346 [t1] c.DCS - updating to read lock... 256
     * 10:52:47.843 [t2] c.DCS - write unlock 384
     * 10:52:47.844 [t1] c.DCS - read lock 513
     * 10:52:48.844 [t1] c.DCS - read finish... 513
     * 10:52:48.844 [t1] c.DCS - read unlock 513
     * @param dcs
     */
    private static void readAndWrite(DataContainerStamped dcs) {
        new Thread(() -> {
            dcs.read(1);
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            dcs.write(1000);
        }, "t2").start();
    }

    /**
     * 读————读，则两个都是乐观读，读读并发，均不会加锁，性能比ReadWriteLock更高
     * 10:47:50.926 [t1] c.DCS - optimistic read locking... 256
     * 10:47:51.426 [t2] c.DCS - optimistic read locking... 256
     * 10:47:51.427 [t2] c.DCS - read finish... 256
     * 10:47:51.932 [t1] c.DCS - read finish... 256
     * @param dcs
     */
    private static void readAndRead(DataContainerStamped dcs) {
        new Thread(() -> {
            dcs.read(1);
        }, "t1").start();

        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            dcs.read(0);
        }, "t2").start();
    }
}

@Slf4j(topic = "c.DCS")
class DataContainerStamped {
    private int data;
    private final StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read(int readTime) {
        // 尝试乐观读锁（无锁）
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking... {}", stamp);
        try {
            TimeUnit.SECONDS.sleep(readTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 戳有效，即读取的过程中没有线程进行了写操作，则直接返回
        if (lock.validate(stamp)) {
            log.debug("read finish... {}", stamp);
            return data;
        }
        // 戳无效，已有线程进行了写更新，则进行锁升级，升级为读锁（与写锁互斥）
        log.debug("updating to read lock... {}", stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock {}", stamp);
            try {
                TimeUnit.SECONDS.sleep(readTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("read finish... {}", stamp);
            return data;
        } finally {
            log.debug("read unlock {}", stamp);
            lock.unlockRead(stamp);
        }
    }

    public void write(int newData) {
        long stamp = lock.writeLock();
        log.debug("write lock {}", stamp);
        try {
            TimeUnit.SECONDS.sleep(2);
            this.data = newData;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.debug("write unlock {}", stamp);
            lock.unlockWrite(stamp);
        }
    }
}
