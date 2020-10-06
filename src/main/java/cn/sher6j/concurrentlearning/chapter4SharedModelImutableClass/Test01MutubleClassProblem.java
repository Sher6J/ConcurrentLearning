package cn.sher6j.concurrentlearning.chapter4SharedModelImutableClass;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * 可变类在多线程访问时会出现问题
 * @author sher6j
 * @create 2020-10-06-10:21
 */
@Slf4j(topic = "c.P")
public class Test01MutubleClassProblem {
    public static void main(String[] args) {
//        problem();
        DateTimeFormatter stf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                TemporalAccessor parse = stf.parse("1997-11-26");
                log.debug("{}", parse);
            }).start();
        }
    }

    /**
     * SimpleDateFormat为可变类
     * 会出现线程安全问题
     */
    private static void problem() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                // 可以给下面代码对sdf加锁保证线程安全，但是影响性能
                try {
                    log.debug("{}", sdf.parse("1997-11-26"));
                } catch (Exception e) {
                    log.error("{}", e);
                }
            }).start();
        }
    }
}
