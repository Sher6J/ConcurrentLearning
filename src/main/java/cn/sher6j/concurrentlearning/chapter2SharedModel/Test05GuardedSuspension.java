package cn.sher6j.concurrentlearning.chapter2SharedModel;

import cn.sher6j.concurrentlearning.utils.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 保护性暂停模式
 * 对比 join() 源码，join() 事实上就是保护性暂停模式的应用
 * @author sher6j
 * @create 2020-09-22-16:06
 */
@Slf4j(topic = "c.GuardedSuspension")
public class Test05GuardedSuspension {
    // 线程1 等待 线程2 的下载结果
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();

        new Thread(() -> {
            // 等待结果
            log.debug("等待结果");
            List<String> list = (List<String>) guardedObject.get(1000);
            log.debug("结果：{}", list);
        }, "t1").start();

        new Thread(() -> {
            log.debug("执行下载");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                List<String> list = Downloader.download();
                guardedObject.generate(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t2").start();
    }
}

class GuardedObject {
    private Object response;

    /**
     * 获取结果
     * @param timeout 最多要等待的时间
     * @return
     */
    public Object get(long timeout) {
        synchronized (this) {
            // 开始时间
            long begin = System.currentTimeMillis();
            // 经历时间
            long passed = 0;
            // 没有结果
            while (response == null) {
                // 该轮循环应该等待的时间
                long waitTime = timeout - passed;
//                if (passed >= timeout) break;
                if (waitTime < 0) break;
                try {
                    this.wait(waitTime); // 防止虚假唤醒等待时间变长
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 更新经历时间
                passed = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    /**
     * 产生结果
     * @param response 结果对象
     */
    public void generate(Object response) {
        synchronized (this) {
            this.response = response;
            this.notifyAll();
        }
    }
}
