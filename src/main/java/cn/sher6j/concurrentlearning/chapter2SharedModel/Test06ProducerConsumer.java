package cn.sher6j.concurrentlearning.chapter2SharedModel;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

/**
 * 生产者消费者模式，由中间消息队列解耦
 * @author sher6j
 * @create 2020-09-25-19:32
 */
@Slf4j(topic = "c.PC")
public class Test06ProducerConsumer {
    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);

        // Variable used in lambda expression should be final or effectively final
        // lambda表达式要求引用的外部局部变量要是final的，所以Message传参不能用i
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                queue.put(new Message(/*i*/id, "消息" + id));
            }, "生产者" + i).start();
        }

        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = queue.take();
            }
        }, "消费者").start();
    }
}

/*
消息队列, Java线程间通信
 */
@Slf4j(topic = "c.消息队列")
class MessageQueue {
    // 消息队列集合，头部获取消息，尾部存放消息
    private Deque<Message> deque = new LinkedList<>();
    // 队列容量
    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 获取消息
    public Message take() {
        // 检查队列是否为空
        synchronized (deque) {
            while (deque.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待");
                    deque.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = deque.removeFirst();
            log.debug("消费者已消费消息：{}", message);
            deque.notifyAll();
            return message;
        }
    }

    // 存入消息
    public void put(Message message) {
        // 检查队列是否满
        synchronized (deque) {
            while (deque.size() == capacity) {
                try {
                    log.debug("队列已满，生产者线程等待");
                    deque.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            deque.addLast(message);
            log.debug("生产者已生产消息：{}", message);
            deque.notifyAll();
        }
    }
}

/*
消息
 */
final class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    // 只有get方法，无set方法，即内部值不可变，只能通过构造方法赋值

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}