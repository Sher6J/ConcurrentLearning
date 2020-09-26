package cn.sher6j.concurrentlearning.chapter2SharedModel;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 哲学家就餐问题：
 * 有五位哲学家，围坐在圆桌旁。
 * 他们只做两件事，思考和吃饭，思考一会吃口饭，吃完饭后接着思考。
 * 吃饭时要用两根筷子吃，桌上共有 5 根筷子，每位哲学家左右手边各有一根筷子。
 * 如果筷子被身边的人拿着，自己就得等待
 *
 * 线程：每个哲学家为1个线程，共5个线程
 * 资源：筷子，共5根筷子
 * 每个哲学家需要2根筷子
 * @author sher6j
 * @create 2020-09-25-20:55
 */
@Slf4j(topic = "c.哲学家就餐问题")
public class Test08DiningPhilosophersProblem {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosopher("苏格拉底", c1, c2).start();
        new Philosopher("柏拉图", c2, c3).start();
        new Philosopher("亚里士多德", c3, c4).start();
        new Philosopher("赫拉克利特", c4, c5).start();
        new Philosopher("阿基米德", c5, c1).start();
    }
}

/*
筷子类
 */
@Slf4j(topic = "c.哲学家就餐问题")
class Chopstick {
    String name;
    public Chopstick(String name) {
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
class Philosopher extends Thread {
    Chopstick left;
    Chopstick right;
    public Philosopher(String name, Chopstick left, Chopstick right) {
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
            // 获得左手筷子
            synchronized (left) {
                // 获得右手筷子 synchronized拿不到锁就会一直等待
                synchronized (right) { // 这里逐渐造成死锁，一直等待其他线程不释放的资源
                    // 吃饭
                    eat();
                }
                // 放下右手筷子
            }
            // 放下左手筷子
        }
    }
}
