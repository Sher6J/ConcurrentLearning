package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 用 join 等待其他线程结束
 * 对于调用线程来说：
 *     1. 需要等待其他线程结果返回，才能继续运行则为同步
 *     2. 不需要等待其他线程结果返回，就能继续运行则为异步
 * @author sher6j
 * @create 2020-09-18-15:45
 */
@Slf4j(topic = "c.Join")
public class Test08Join {
    static int c = 0;

    public static void main(String[] args) throws InterruptedException {
        test1();
    }

    private static void test1() throws InterruptedException {
        log.debug("begin...");
        Thread t1 = new Thread(() -> {
           log.debug("begin......");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end......");
            c = 10;
        });
        t1.setName("t1");
        t1.start();
        // 主线程在同步等待t1线程
        t1.join(); // 若没有join() 则结果为 0
        log.debug("result of c: {}", c); // 10
        log.debug("end...");
    }
}
