package cn.sher6j.concurrentlearning.chapter2SharedModel;

import cn.sher6j.concurrentlearning.utils.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 保护性暂停模式
 * 对比 join() 源码，join() 事实上就是保护性暂停模式的应用
 * 1个居民就需要1个邮递员
 * @author sher6j
 * @create 2020-09-22-16:06
 */
@Slf4j(topic = "c.GuardedSuspension")
public class Test05GuardedSuspension {
    // 1个消费者 对应 1个产生者
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Resident().start();
        }

        TimeUnit.SECONDS.sleep(1);

        for (Integer id : MailBox.getIds()) {
            new PostMan(id, "内容" + id).start();
        }
    }
}

/*
居民类
 */
@Slf4j(topic = "c.Resident")
class Resident extends Thread{
    @Override
    public void run() {
        // 收信
        GuardedObject guardedObject = MailBox.createGuardedObject();
        log.debug("居民开始收信，信件id：{}", guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("居民收到信，信件id：{}，内容：{}", guardedObject.getId(), mail);
    }
}

/*
邮递员类
 */
@Slf4j(topic = "c.PostMan")
class PostMan extends Thread{
    private int id;
    private String mail;

    public PostMan(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        // 送信
        GuardedObject guardedObject = MailBox.getGuardedObject(id);
        log.debug("邮递员送信，id：{}，内容：{}", id, mail);
        guardedObject.generate(mail);
    }
}

/*
用于解耦的邮箱类
 */
class MailBox {
    private static Map<Integer, GuardedObject> box = new Hashtable<>();

    private static int id = 1;
    // 产生唯一id
    private static synchronized int generateId() {
        return id++;
    }

    public static GuardedObject getGuardedObject(int id) {
        return box.remove(id);
        // get 和 remove 区别：同样返回值，但是remove会将键值删掉
    }

    public static GuardedObject createGuardedObject() {
        GuardedObject go = new GuardedObject(generateId());
        box.put(go.getId(), go);
        return go;
    }

    public static Set<Integer> getIds() {
        return box.keySet();
    }
}

class GuardedObject {

    private int id; // 唯一标识

    private Object response;

    public GuardedObject(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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
