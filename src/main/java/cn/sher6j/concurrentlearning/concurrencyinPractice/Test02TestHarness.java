package cn.sher6j.concurrentlearning.concurrencyinPractice;

import java.util.concurrent.CountDownLatch;

/**
 * 闭锁CountDownLatch的应用
 * 主线程统计多个线程执行的时间
 * 启动门能够使主线程同时释放所有工作线程
 * 结束们能够使主线程等待最后一个线程执行完成，而不是顺序地等待每个线程执行完成
 * @author sher6j
 * @create 2020-10-10-19:37
 */
public class Test02TestHarness {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Thread thread = Thread.currentThread();
                System.out.println(thread);
            }
        };

        long time = Test02TestHarness.timeTasks(10, task);
        System.out.println(time);
    }

    /**
     * 统计多个线程执行任务的时间
     * @param n 线程数
     * @param task 任务
     * @return
     * @throws InterruptedException
     */
    public static long timeTasks(int n, final Runnable task) throws InterruptedException {
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(n);

        for (int i = 0; i < n; i++) {
            new Thread(() -> {
                try {
                    startGate.await();
                    try {
                        task.run();
                    } finally {
                        endGate.countDown();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        long startTime = System.nanoTime();
        startGate.countDown();
        endGate.await();
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
