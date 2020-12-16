package cn.sher6j.concurrentlearning.chapter6ConcurrencyToolsAQS;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 开黑游戏等待所有玩家准备好后主线程才开始游戏
 * 就可以用CountDownLatch实现
 * @author sher6j
 * @create 2020-10-09-14:29
 */
@Slf4j(topic = "c.player")
public class Test06WaitingPlayers {
    static final int PROCESS = 100;
    static final int PLAYER_NUM = 10;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);
        Random r = new Random();
        String[] all = new String[10];
        for (int j = 0; j < PLAYER_NUM; j++) {
            int k = j;
            pool.submit(() -> {
                for (int i = 0; i <= PROCESS; i++) {
                    // 模拟加载时延
                    try {
                        Thread.sleep(r.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    all[k] = i + "%";
                    // \r 回车，将第二行的覆盖为第一行
                    System.out.print("\r" + Arrays.toString(all));
                }
                latch.countDown();
            });
        }

        latch.await();
        System.out.println("\n游戏开始！！！");
        pool.shutdown();
    }
}
