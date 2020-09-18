package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

/**
 * 开始一个新线程需要用start()，而不能直接调用run()
 * 直接调用 run 是在主线程中执行了 run，没有启动新的线程
 * 使用 start 是启动新的线程，通过新的线程间接执行 run 中的代码
 * @author sher6j
 * @create 2020-09-18-12:00
 */
@Slf4j(topic = "c.StartAndRun")
public class Test04StartAndRun {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        // run()并没有启动一个新的线程，而是由主线程执行的run方法，不能达到提升性能、异步处理的效果
        t1.run(); // [main] c.StartAndRun - running...

        t1.start(); // [t1] c.StartAndRun - running...
    }
}
