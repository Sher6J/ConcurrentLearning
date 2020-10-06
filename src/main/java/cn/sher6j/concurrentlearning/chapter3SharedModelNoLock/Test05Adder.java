package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author sher6j
 * @create 2020-10-06-8:45
 */
public class Test05Adder {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            demo(
                    () -> new AtomicLong(0),
                    (adder) -> adder.getAndIncrement()
            );
        }

        for (int i = 0; i < 5; i++) {
            demo(
                    () -> new LongAdder(),
                    adder -> adder.increment()
            );
        }
    }

    /**
     *
     * @param adderSup 提供累加器对象
     * @param action 执行累加操作
     * @param <T>
     */
    private static <T> void demo(Supplier<T> adderSup, Consumer<T> action) {
        T adder = adderSup.get();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        threads.forEach(t -> t.start());
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(adder + " cost: " + (end - start) / 1000000);
    }
}
