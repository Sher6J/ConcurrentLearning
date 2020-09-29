package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author sher6j
 * @create 2020-09-29-20:26
 */
public class Test04AtomicArray {
    public static void main(String[] args) {
        // [8729, 8734, 8687, 8686, 8775, 8797, 8809, 8808, 8806, 8813]
        test(
                () -> new int[10],
                (array) -> array.length,
                (array, idx) -> array[idx]++,
                array -> System.out.println(Arrays.toString(array))
        );

        // [10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000, 10000]
        test(
                () -> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array, idx) -> array.getAndIncrement(idx),
                array -> System.out.println(array)
        );
    }

    /**
     * note：
     * Supplier 提供者 无中生有        () -> res
     * Function 函数 1个参数1个结果    (arg) -> res
     * BiFunction 函数 2个参数1个结果  (arg1, arg2) -> res
     * Consumer 消费者 1个参数没结果   (arg) -> void
     * BiConsumer 消费者 2个参数没结果 (arg1, arg2) -> void
     * @param arraySupplier 提供数组，可以是线程不安全或线程安全
     * @param lengthFunc 获取数组长度
     * @param putConsumer 自增方法
     * @param printConsumer 打印数组的方法
     * @param <T>
     */
    private static <T> void test (
            Supplier<T> arraySupplier,
            Function<T, Integer> lengthFunc,
            BiConsumer<T, Integer> putConsumer,
            Consumer<T> printConsumer) {
        List<Thread> threads = new ArrayList<>();
        T array = arraySupplier.get();
        int len = lengthFunc.apply(array);
        // 10个线程，每个10000次，均摊每个元素应为10000
        for (int i = 0; i < len; i++) {
            threads.add(new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array, j % len);
                }
            }));
        }
        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        printConsumer.accept(array);
    }
}
