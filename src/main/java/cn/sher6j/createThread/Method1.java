package cn.sher6j.createThread;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程 方法一：直接使用Thread类
 * @author sher6j
 * @create 2020-09-17-19:32
 */

@Slf4j(topic = "c.Method1")
public class Method1 {
    public static void main(String[] args) {

        Thread t = new Thread("t1") {
            @Override
            public void run() {
                log.debug("t1 running");
            }
        };
//        t.setName("t1");

        t.start();

        log.debug("main running");
    }
}
