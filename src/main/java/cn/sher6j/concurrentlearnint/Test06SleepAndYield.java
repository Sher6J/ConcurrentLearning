package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

/**
 * sleep 会让当前线程从 RUNNING 进入 TIMED_WAITING 状态
 * yield 会让当前线程从 RUNNING 进入 RUNNABLE 状态
 * 操作系统的任务调度器仍可能分配给 RUNNABLE 状态线程时间片
 * 而不会分配给 TIME_WAITING 状态线程时间片
 * @author sher6j
 * @create 2020-09-18-15:05
 */
@Slf4j(topic = "c.SleepAndYield")
public class Test06SleepAndYield {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug("t1 state: {}", t1.getState()); // RUNNABLE

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("t1 state: {}", t1.getState()); // TIMED_WAITING
    }

}
