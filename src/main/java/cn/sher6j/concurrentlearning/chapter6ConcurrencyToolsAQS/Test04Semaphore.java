package cn.sher6j.concurrentlearning.chapter6ConcurrencyToolsAQS;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量————用来限制能同时访问共享资源的线程上限
 * @author sher6j
 * @create 2020-10-09-14:06
 */
@Slf4j(topic = "c.Semaphore")
public class Test04Semaphore {
    public static void main(String[] args) {
        // 1. 创建 构造方法两个参数（许可数，公平非公平）
        Semaphore semaphore = new Semaphore(3);

        // 2. 创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    // 获得许可
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    log.debug("running...");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.debug("end...");
                } finally {
                    // 释放许可
                    semaphore.release();
                }
            }).start();
        }
    }
}
