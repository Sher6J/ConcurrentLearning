package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 睡眠被打断后会发生 InterruptedException 异常
 * 建议用 TimeUnit 的 sleep 代替 Thread 的 sleep 来获得更好的可读性
 * @author sher6j
 * @create 2020-09-18-15:09
 */

@Slf4j(topic = "c.InterruptSleep")
public class Test07InterruptSleep {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("begin sleeping...");
                try {
//                    Thread.sleep(2000);
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    log.debug("forced to wake up...");
                    e.printStackTrace();
                }
            }
        };
        t1.start();

        // 主线程睡眠1秒后唤醒t1线程
        try {
//            Thread.sleep(1000);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("interrupt...");
        t1.interrupt();
    }
}
