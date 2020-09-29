package cn.sher6j.concurrentlearning.chapter2SharedModelWithLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sher6j
 * @create 2020-09-19-15:01
 */
@Slf4j(topic = "c.Problem")
public class Test02Synchronized {

//    static int counter = 0;
//    static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
//                synchronized (lock) {
//                    counter++;
//                }
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
//                synchronized (lock) {
//                    counter--;
//                }
                room.decrement();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
//        log.debug("{}", counter);
        log.debug("{}", room.getCounter());
    }
}

class Room {
    private int counter = 0;

    public void increment() {
        synchronized (this) {
            counter++;
        }
    }

    public void decrement() {
        synchronized (this) {
            counter--;
        }
    }

    public int getCounter() {
        synchronized (this) {
            return counter;
        }
    }
}