package cn.sher6j.concurrentlearnint;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程 方法二：使用Runnable接口配合Thread类
 * Runnable接口定义线程要做的事情
 * Thread类代表线程
 * 线程 和 任务 分开
 * @author sher6j
 * @create 2020-09-17-19:35
 */

@Slf4j(topic = "c.Method2")
public class Test02Method2 {
    public static void main(String[] args) {
        /*
        Runnable接口只有一个抽象方法，注解中有@FunctionalInterface，则可以Lambda简化
         */
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                log.debug("running");
//            }
//        };
        Runnable r = () -> {
            log.debug("running");
        };

        Thread t = new Thread(r, "t2");
        t.start();

        log.debug("running");
    }
}
