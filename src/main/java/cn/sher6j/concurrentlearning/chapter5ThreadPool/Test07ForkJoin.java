package cn.sher6j.concurrentlearning.chapter5ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author sher6j
 * @create 2020-10-08-16:53
 */
@Slf4j(topic = "c.FJ")
public class Test07ForkJoin {
    public static void main(String[] args) {
        // 无参构造方法则创建和CPU核数相同的线程数
        ForkJoinPool pool = new ForkJoinPool();
        System.out.println(pool.invoke(new MyTask(20)));
    }
}

/**
 * 有返回值则继承RecursiveTask，泛型为返回值类型
 * 无返回值则继承RecursiveAction
 * 求 1-n 的整数和
 */
@Slf4j(topic = "c.FJ")
class MyTask extends RecursiveTask<Integer> {

    private int n;

    public MyTask(int n) {
        this.n = n;
    }

    @Override
    public String toString() {
        return "MyTask{" +
                "n=" + n +
                '}';
    }

    @Override
    protected Integer compute() {
        // 拆分终止条件
        if (n == 1) {
            log.debug("join() {}", n);
            return 1;
        }
        MyTask t1 = new MyTask(n - 1);
        t1.fork(); // 拆分让一个线程执行
        log.debug("fork() {} + {}", n, t1);

        int res = n + t1.join();
        log.debug("join() + {} + {} = {}", n, t1, res);
        return res;
    }
}