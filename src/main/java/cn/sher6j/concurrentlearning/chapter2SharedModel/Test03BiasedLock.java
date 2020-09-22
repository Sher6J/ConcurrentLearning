package cn.sher6j.concurrentlearning.chapter2SharedModel;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * @author sher6j
 * @create 2020-09-21-14:31
 */
@Slf4j(topic = "c.BiasedLock")
public class Test03BiasedLock {
    public static void main(String[] args) throws InterruptedException {
        Lock lock = new Lock();
//        lock.hashCode(); // 调用hashCode会禁用掉对象的偏向锁
        log.debug(ClassLayout.parseInstance(lock).toPrintable());
        TimeUnit.SECONDS.sleep(4);
//        log.debug(ClassLayout.parseInstance(new Lock()).toPrintable());
        synchronized (lock) {
            log.debug(ClassLayout.parseInstance(lock).toPrintable());
        }
        log.debug(ClassLayout.parseInstance(lock).toPrintable());
    }
}

class Lock{

}
