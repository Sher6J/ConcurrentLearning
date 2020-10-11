package cn.sher6j.concurrentlearning.concurrencyinPractice;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 素数生成器
 * @author sher6j
 * @create 2020-10-11-19:23
 */
public class Test04PrimeGenerator {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Test04PrimeGenerator.aSecondOfPrimes());
    }

    /**
     * 用1秒的时间生成的素数
     * 客户代码通过调用cancel来请求取消
     * @return
     * @throws InterruptedException
     */
    public static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        new Thread(generator).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } finally {
            generator.cancel();
        }
        return generator.get();

    }
}

/**
 * 素数生成器
 * 在每次搜索素数前先检查是否存在取消请求，如果存在则退出不再生成素数
 */
class PrimeGenerator implements Runnable {
    private final List<BigInteger> primes = new ArrayList<>();
    private volatile boolean cancelled;

    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<>(primes);
    }
}
