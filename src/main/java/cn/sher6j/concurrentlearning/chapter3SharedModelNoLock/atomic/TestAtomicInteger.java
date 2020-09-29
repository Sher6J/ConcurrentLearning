package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * 原子整数类型
 * 注意 updateAndGet 的实现
 * @author sher6j
 * @create 2020-09-29-19:27
 */
public class TestAtomicInteger {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(5);
//        System.out.println(i.incrementAndGet()); // ++i的原子操作
//        System.out.println(i.getAndIncrement()); // i++的原子操作
//        System.out.println(i.get());
//        System.out.println(i.getAndAdd(5)); // 原子操作
//        System.out.println(i.addAndGet(5)); // 原子操作
//        System.out.println(i.get());

        i.updateAndGet(x -> x * 10); // 原子操作

        System.out.println(i.get());

//        while (true) {
//            int prev = i.get();
//            int next = prev * 10;
//            if (i.compareAndSet(prev, next)) break;
//        }

        updateAndGet(i, p -> p / 2);

        System.out.println(i.get());
    }

    public static void updateAndGet(AtomicInteger i, IntUnaryOperator operator) {
        while (true) {
            int prev = i.get();
            int next = operator.applyAsInt(prev);
            if (i.compareAndSet(prev, next)) break;
        }
    }
}
