package cn.sher6j.concurrentlearning.chapter1Base;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程 方法一：直接使用Thread类
 * 有一种特殊的线程叫做守护线程，只要其它
 * 非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束
 * @author sher6j
 * @create 2020-09-17-19:32
 */

@Slf4j(topic = "c.Method1")
public class Test01Method1 {
    public static void main(String[] args) {

        Thread t = new Thread("t1") {
            @Override
            public void run() {
                while (true) {
                    log.debug("t1 running");
                }
            }
        };
//        t.setName("t1");
        t.setDaemon(true);
        t.start();

        log.debug("main running");
    }
}
