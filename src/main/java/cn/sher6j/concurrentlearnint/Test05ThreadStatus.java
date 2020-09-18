package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sher6j
 * @create 2020-09-18-12:07
 */
@Slf4j(topic = "c.ThreadStatus")
public class Test05ThreadStatus {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        System.out.println(t1.getState()); // NEW
        t1.start();
        System.out.println(t1.getState()); // RUNNABLE
    }
}
