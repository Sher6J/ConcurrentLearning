package cn.sher6j.concurrentlearning.concurrencyinPractice;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Timer在执行所有定时任务时，只会创建一个线程！！！(Timer-0)
 * Timer线程并不捕获异常，因此当TimerTask抛出未检查异常时将终止定时线程（Timer-0）。
 *
 * Exception in thread "Timer-0" java.lang.RuntimeException
 * Exception in thread "main" java.lang.IllegalStateException: Timer already cancelled.
 * @author sher6j
 * @create 2020-10-10-20:49
 */
public class Test03TimerProblem {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new ThrowTask(), 1);
        TimeUnit.SECONDS.sleep(1);
        timer.schedule(new ThrowTask(), 1);
        TimeUnit.SECONDS.sleep(5);
    }

    static class ThrowTask extends TimerTask {

        @Override
        public void run() {
            throw new RuntimeException();
        }
    }
}
