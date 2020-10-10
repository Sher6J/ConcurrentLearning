package cn.sher6j.concurrentlearning.concurrencyinPractice;


import org.junit.Test;

import java.util.*;

/**
 * @author sher6j
 * @create 2020-10-10-15:11
 */
public class Test01SingleTheadConcurrencyModificationExceptionTest {
    /**
     * java.util.ConcurrentModificationException
     */
    @Test
    public void test1() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            list.remove(0);
            System.out.println(iterator.next());
        }
    }

    /**
     * java.util.ConcurrentModificationException
     */
    @Test
    public void test2() {
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            list.remove(0);
            System.out.println(iterator.next());
        }
    }

    /**
     * java.util.ConcurrentModificationException
     */
    @Test
    public void test3() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        for (int i : list) {
            list.remove(0);
        }
    }

    /**
     * success
     */
    @Test
    public void test4() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        for (int i = 0; i < list.size(); i++) {
            list.remove(0);
        }
    }

    /**
     * java.lang.UnsupportedOperationException
     */
    @Test
    public void test5() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            list.remove(0);
            System.out.println(iterator.next());
        }
    }
}
