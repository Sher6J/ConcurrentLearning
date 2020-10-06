package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import cn.sher6j.concurrentlearning.utils.UnsafeAccessor;
import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

/**
 * 自己实现原子整数类
 * @author sher6j
 * @create 2020-10-06-9:49
 */
@Slf4j(topic = "c.My")
public class Test07MyAtomicInteger {
    public static void main(String[] args) {
        Account.thread100(new MyAtomicInteger(10000));
    }
}

class MyAtomicInteger implements Account{
    private volatile int value; // 配合cas使用需要用volatile
    private static final long offsetValue; // value字段的偏移值
    private static final Unsafe UNSAFE;

    static {
        UNSAFE = UnsafeAccessor.getUnsafe();
        try {
            offsetValue = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    /**
     * 原子减法
     * @param x
     */
    public void decrement(int x) {
        while (true) {
            int prev = this.value;
            int next = prev - x;
            if (UNSAFE.compareAndSwapInt(this, offsetValue, prev, next)) {
                break;
            }
        }
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    @Override
    public Integer getBalance() {
        return getValue();
    }

    @Override
    public void withdraw(Integer amount) {
        decrement(amount);
    }
}


