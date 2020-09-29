package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AtomicReference中CAS中的问题：
 * 其他线程修改了该值，但修改后等于原值，ABA问题
 * 如下例子中，主线程感知不到其他线程对ref进行了修改
 * 这种情况用AtomicStampedReference可以感知其他线程的修改
 * @author sher6j
 * @create 2020-09-29-20:03
 */
@Slf4j(topic = "c.Test")
public class Test02CasProblem {
    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {
        log.debug("main start...");
        String prev = ref.get();
        otherMethod();
        TimeUnit.SECONDS.sleep(1);
        log.debug("change A->C {}", ref.compareAndSet(prev, "C"));
    }

    private static void otherMethod() throws InterruptedException {
        new Thread(() -> {
            log.debug("change A->B {}", ref.compareAndSet("A", "B"));
        }, "t2").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(() -> {
            log.debug("change B->A {}", ref.compareAndSet("B", "A"));
        }, "t3").start();
    }
}
