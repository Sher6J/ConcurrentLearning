package cn.sher6j.concurrentlearning.chapter2SharedModel;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 活锁：活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束
 * 可以增加随机的睡眠时间来避免活锁的产生
 *
 * 对于死锁：处于死锁的线程都阻塞了
 * 对于活锁：处于活锁的线程并没有阻塞，但是运行不完
 * @author sher6j
 * @create 2020-09-25-21:09
 */
@Slf4j(topic = "c.活锁")
public class Test09LiveLock {
    static volatile int count = 10;
    static final Object lock = new Object();
    /*
    线程1对count一直--，到0线程结束
    线程2对count一直++，到20线程结束
    单独执行均无问题，一起执行则出现活锁
     */
    public static void main(String[] args) {
        new Thread(() -> {
            // 期望减到 0 退出循环
            while (count > 0) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();
        new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}
