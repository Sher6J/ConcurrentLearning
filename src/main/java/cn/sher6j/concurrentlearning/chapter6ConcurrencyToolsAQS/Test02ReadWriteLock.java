package cn.sher6j.concurrentlearning.chapter6ConcurrencyToolsAQS;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * 读锁与读锁可并发
 * 读锁与写锁互斥
 * 写锁与写锁互斥
 * @author sher6j
 * @create 2020-10-08-20:12
 */
public class Test02ReadWriteLock {
    public static void main(String[] args) {
        DataContainer dc = new DataContainer();

        new Thread(() -> {
            dc.read();
        }, "t1").start();

        new Thread(() -> {
            dc.read();
        }, "t2").start();
    }
}

/**
 * 数据容器，data为共享数据
 */
@Slf4j(topic = "c.Data")
class DataContainer {
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = rw.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = rw.writeLock();

    public Object read() {
        log.debug("获取读锁...");
        readLock.lock();
        try {
            log.debug("读取");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        } finally {
            log.debug("释放读锁...");
            readLock.unlock();
        }
    }

    public void write() {
        log.debug("获取写锁...");
        writeLock.lock();
        try {
            log.debug("写入");
        } finally {
            log.debug("释放写锁...");
            writeLock.unlock();
        }
    }
}
