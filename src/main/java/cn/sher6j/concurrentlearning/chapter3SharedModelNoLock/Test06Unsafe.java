package cn.sher6j.concurrentlearning.chapter3SharedModelNoLock;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe对象不能直接调用，只能通过反射获得
 * @author sher6j
 * @create 2020-10-06-9:41
 */
public class Test06Unsafe {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        System.out.println(unsafe);

        // 1. 获取域的偏移地址
        long idOffset = unsafe.objectFieldOffset(Student.class.getDeclaredField("id"));
        long nameOffset = unsafe.objectFieldOffset(Student.class.getDeclaredField("name"));

        Student s = new Student();

        // 2. 执行cas操作
        unsafe.compareAndSwapInt(s, idOffset, 0, 1);
        unsafe.compareAndSwapObject(s, nameOffset, null, "sher6j");

        // 3. 验证结果
        System.out.println(s);
    }
}

@Data
class Student {
    volatile int id;
    volatile String name;
}
