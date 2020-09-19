package cn.sher6j.concurrentlearning.chapter1Base;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Java线程的六种状态
 * NEW
 * RUNNABLE
 * WAITING
 * TIME_WAITING
 * BLOCKED
 * TERMINATED
 * @author sher6j
 * @create 2020-09-19-14:27
 */
@Slf4j(topic = "c.ThreadState")
public class Test12ThreadState {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("running...");
        }, "t1");

        Thread t2 = new Thread(() -> {
            while (true) {

            }
        }, "t2");
        t2.start();

        Thread t3 = new Thread(() -> {
            log.debug(("t3 running..."));
        }, "t3");
        t3.start();

        Thread t4 = new Thread(() -> {
            synchronized (Test12ThreadState.class) {
                try {
                    TimeUnit.HOURS.sleep(1); // TIMED_WAITING
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t4");
        t4.start();

        Thread t5 = new Thread(() -> {
            try {
                t2.join(); // WAITING
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t5");
        t5.start();

        Thread t6 = new Thread(() -> {
            // 拿不到该锁，该锁被t4使用，故BLOCKED
            synchronized (Test12ThreadState.class) {
                try {
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t6");
        t6.start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.debug("t1 state : {}", t1.getState());
        log.debug("t2 state : {}", t2.getState());
        log.debug("t3 state : {}", t3.getState());
        log.debug("t4 state : {}", t4.getState());
        log.debug("t5 state : {}", t5.getState());
        log.debug("t6 state : {}", t6.getState());
    }
}
