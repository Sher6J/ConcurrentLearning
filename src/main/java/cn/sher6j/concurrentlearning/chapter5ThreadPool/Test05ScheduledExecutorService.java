package cn.sher6j.concurrentlearning.chapter5ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 带调度功能的线程池
 * 延时执行任务或循环执行任务：
 * java.util.Timer已经过时，第一个执行会影响第二个任务执行，且若第一个任务执行出错，第二个不会执行
 * 使用ScheduledExecutorService代替Timer吧，第一个任务的执行不会影响第二个任务执行
 * @author sher6j
 * @create 2020-10-08-15:36
 */
@Slf4j(topic = "c.Timer")
public class Test05ScheduledExecutorService {
    public static void main(String[] args) {
//        TimerTest(); // 过时
//        delayExecutor(); // 用ScheduledExecutorService延时执行任务
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        log.debug("start...");
        // scheduleAtFixedRate的任务执行时间若大于period，则不会有延时
//        pool.scheduleAtFixedRate(() -> {
//            log.debug("running...");
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 1, 1, TimeUnit.SECONDS);

        // 注意和上面不同
        pool.scheduleWithFixedDelay(() -> {
            log.debug("running...");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 延时执行任务
     */
    private static void delayExecutor() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        log.debug("start...");
        pool.schedule(() -> {
            log.debug("task 1");
            int i = 1 / 0;
            try {
                TimeUnit.SECONDS.sleep(4);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);

        pool.schedule(() -> {
            log.debug("task 2");
        }, 1, TimeUnit.SECONDS);
    }

    private static void TimerTest() {
        Timer timer = new Timer();
        TimerTask t1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
            }
        };
        TimerTask t2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };

        log.debug("start....");
        timer.schedule(t1, 1000);
        timer.schedule(t2, 1000);
    }
}
