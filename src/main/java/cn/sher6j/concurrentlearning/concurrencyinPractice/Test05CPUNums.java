package cn.sher6j.concurrentlearning.concurrencyinPractice;

/**
 * 获得CPU的数目
 * @author sher6j
 * @create 2020-10-12-15:37
 */
public class Test05CPUNums {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
