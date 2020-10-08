package cn.sher6j.concurrentlearning.chapter5ThreadPool;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 例子：让每周四16:40定时执行任务
 * @author sher6j
 * @create 2020-10-08-16:29
 */
public class Test06Schedule {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        // 获取周四时间
        LocalDateTime time = now.withHour(16).withMinute(40).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        // 如果当前时间大于本周周四，需要找下周周四
        if (now.compareTo(time) > 0) {
            time.plusWeeks(1);
        }

        // initialDelay 当前时间和周四的时间差
        long initialDelay = Duration.between(now, time).toMillis();
        // period 一周的时间
        long period = 1000 * 60 * 60 * 24 * 7;

        pool.scheduleAtFixedRate(() -> {
            System.out.println("定时任务");
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }
}
