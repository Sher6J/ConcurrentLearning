package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sher6j
 * @create 2020-09-29-18:52
 */
public class Test01Account {
    public static void main(String[] args) {
        Account account1 = new AccountWithSynchronized(10000);
        Account.thread100(account1);

        Account account2 = new AccountWithCasNoLock(10000);
        Account.thread100(account2);
    }
}

/**
 * 基于无锁的方式保护共享资源 CAS（Compare And Swap / Compare And Set）
 */
class AccountWithCasNoLock implements Account {

    private AtomicInteger balance;

    public AccountWithCasNoLock(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
//        while (true) {
//            int prev = balance.get(); // 获取余额最新值
//            int next = prev - amount; // 要修改的余额
//            // 真正修改，compareAndSet是原子的
//            boolean flag = balance.compareAndSet(prev, next);
//            if (flag) break;
//        }
        balance.getAndAdd(-1 * amount);
    }
}

/**
 * 用synchronized锁保护共享资源
 */
class AccountWithSynchronized implements Account {
    private Integer balance; // 余额

    public AccountWithSynchronized(Integer balance) {
        this.balance = balance;
    }

    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}

interface Account {
    // 获取余额
    Integer getBalance();

    // 取款
    void withdraw(Integer amount);

    /**
     * 该静态方法启动1000个线程，每个线程做-10元操作
     */
    static void thread100(Account account) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            threads.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }); // 等待线程执行结束
        long end = System.nanoTime();
        System.out.println(account.getBalance() + " cost:" + (end - start) / 1000000 + "ms");
    }
}


