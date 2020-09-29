package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock.atomic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子引用类型
 * @author sher6j
 * @create 2020-09-29-19:53
 */
public class TestAtomicReference {
    public static void main(String[] args) {
        DecimalAccount.thread1000(new DecimalAccountCas(new BigDecimal("10000")));
    }
}

class DecimalAccountCas implements DecimalAccount {

    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance) {
        this.balance = new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev, next)) break;
        }
    }
}

interface DecimalAccount {
    // 获取余额
    BigDecimal getBalance();

    // 取款
    void withdraw(BigDecimal amount);

    static void thread1000(DecimalAccount account) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                account.withdraw(BigDecimal.TEN);
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(account.getBalance());
    }
}
