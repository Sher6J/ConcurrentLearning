package cn.sher6j.concurrentlearning.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 获取unsafe对象的工具类
 * @author sher6j
 * @create 2020-10-06-9:51
 */
public class UnsafeAccessor {
    private static final Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new Error(e);
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
