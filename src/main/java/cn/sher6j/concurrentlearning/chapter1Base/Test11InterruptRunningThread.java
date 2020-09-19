package cn.sher6j.concurrentlearning.chapter1Base;

import lombok.extern.slf4j.Slf4j;

/**
 * 主线程打断
 * @author sher6j
 * @create 2020-09-19-13:47
 */
@Slf4j(topic = "c.Interrupt")
public class Test11InterruptRunningThread {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
           while (true) {
               boolean interrupted = Thread.currentThread().isInterrupted();
               if (interrupted) {
                   log.debug("t1 is interrupted, exit!");
                   break;
               }
           }
        }, "t1");

        t1.start();

        Thread.sleep(2000);
        log.debug("main interrupt t1");
        t1.interrupt();
    }
}
