package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author sher6j
 * @create 2020-09-26-14:47
 */
public class Test13DiningPhilosophersWithReentrantLock {
    public static void main(String[] args) {
        Chopstick1 c1 = new Chopstick1("1");
        Chopstick1 c2 = new Chopstick1("2");
        Chopstick1 c3 = new Chopstick1("3");
        Chopstick1 c4 = new Chopstick1("4");
        Chopstick1 c5 = new Chopstick1("5");
        new Philosopher1("苏格拉底", c1, c2).start();
        new Philosopher1("柏拉图", c2, c3).start();
        new Philosopher1("亚里士多德", c3, c4).start();
        new Philosopher1("赫拉克利特", c4, c5).start();
        new Philosopher1("阿基米德", c5, c1).start();
    }
}

/*
筷子类
 */
@Slf4j(topic = "c.哲学家就餐问题")
class Chopstick1 extends ReentrantLock {
    String name;
    public Chopstick1(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "筷子{" + name + '}';
    }
}

/*
哲学家类
 */
@Slf4j(topic = "c.哲学家就餐问题")
class Philosopher1 extends Thread {
    Chopstick1 left;
    Chopstick1 right;
    public Philosopher1(String name, Chopstick1 left, Chopstick1 right) {
        super(name);
        this.left = left;
        this.right = right;
    }
    private void eat() throws InterruptedException {
        log.debug("eating...");
        TimeUnit.SECONDS.sleep(1);
    }
    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            // 尝试获得左手筷子
            if (left.tryLock()) {
                try {
                    // 尝试获得右手筷子
                    if (right.tryLock()) {
                        try {
                            eat();
                        } finally {
                            right.unlock(); // 释放自己右手筷子
                        }
                    }
                } finally {
                    left.unlock(); // 释放自己左手筷子
                }
            }
        }
    }
}
