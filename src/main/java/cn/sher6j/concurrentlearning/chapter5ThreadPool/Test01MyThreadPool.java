package cn.sher6j.concurrentlearning.chapter5ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义线程池
 * @author sher6j
 * @create 2020-10-06-13:24
 */
@Slf4j(topic = "c.MyPool")
public class Test01MyThreadPool {
    public static void main(String[] args) {
        MyThreadPool threadPool = new MyThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10,
                (queue, task) -> { // 拒绝策略（即任务队列满时）
//                    queue.put(task); // 1. 死等
//                    queue.offer(task, 100, TimeUnit.MILLISECONDS); // 2. 带超时等待
//                    log.debug("放弃{}", task); // 3. 让调用者放弃任务执行
//                    throw new RuntimeException("任务执行失败" + task); // 4. 让调用者抛出异常
                    task.run(); // 5. 让调用者自己执行任务
                });
        for (int i = 0; i < 15; i++) {
            int j = i;
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("{}", j);
            });
        }
    }
}

/*
拒绝策略接口（即任务队列满时的处理）
 */
@FunctionalInterface
interface RejectPolicy<T> {
    void reject(BlockingQueue<T> queue, T task);
}

@Slf4j(topic = "c.MyPool")
class MyThreadPool {
    // 1. 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 2. 线程集合
    private HashSet<Worker> workers = new HashSet();

    // 3. 核心线程数
    private int coreSize;

    // 4. 获取线程超时时间（即一定时间线程没有执行任务则释放）
    private long timeout;
    private TimeUnit timeUnit;

    // 5. 拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    /**
     * 构造方法
     * @param coreSize 核心线程数
     * @param timeout 超时时间
     * @param timeUnit 超时时间单位
     * @param capacity 阻塞队列容量
     */
    public MyThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int capacity, RejectPolicy<Runnable> rejectPolicy) {
        this.taskQueue = new BlockingQueue<>(capacity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    /**
     * 执行任务
     * @param task
     */
    public void execute(Runnable task) {
        // 任务数没有超过coreSize，直接交给worker对象执行
        // 任务数超过coreSize，则加入任务队列暂存
        synchronized (workers) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增worker:{},{}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
//                taskQueue.put(task); // 死等
                // 1. 队列满了就死等
                // 2. 带超时等待
                // 3. 让调用者放弃任务执行
                // 4. 让调用者抛出异常
                // 5. 让调用者自己执行任务
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        /**
         * 执行任务
         */
        @Override
        public void run() {
            // 1. task不为空，直接执行任务
            // 2. task执行完毕，再接着到任务队列中获取任务执行
//            while (task != null || (task =taskQueue.take()) != null) {
            while (task != null || (task =taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.debug("正在执行{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            synchronized (workers) {
                log.debug("worker被移除:{}", this);
                workers.remove(this);
            }
        }
    }
}

// 阻塞队列
@Slf4j(topic = "c.MyPool")
class BlockingQueue<T> {
    // 1. 任务队列，ArrayDeque比LinkedList性能较好
    private Deque<T> queue = new ArrayDeque<>();

    // 2. 锁
    private ReentrantLock lock = new ReentrantLock();

    // 3. 生产者的条件变量，队列满时等待
    private Condition fullWait = lock.newCondition();

    // 4. 消费者的条件变量，队列空时等待
    private Condition emptyWait = lock.newCondition();

    // 5. 容量
    private int capacity;

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * 带超时时间的阻塞获取
     * @param timeout
     * @param timeUnit
     * @return
     */
    public T poll(long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long waitTime = timeUnit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    // awaitNacos返回的是等待的剩余时间
                    if (waitTime <= 0) return null;
                    waitTime = emptyWait.awaitNanos(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWait.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞获取
     * @return
     */
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                try {
                    emptyWait.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWait.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时的阻塞添加
     * @param task
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列:{}...", task);
                    if (nanos <= 0) return false;
                    nanos = fullWait.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.debug("加入任务队列:{}", task);
            emptyWait.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 阻塞添加
     * @param task
     */
    public void put(T task) {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                try {
                    log.debug("等待加入任务队列:{}...", task);
                    fullWait.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(task);
            log.debug("加入任务队列:{}", task);
            emptyWait.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取大小
     * @return
     */
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 尝试向阻塞队列中添加任务
     * @param rejectPolicy
     * @param task
     */
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否已满
            if (queue.size() == capacity) {
                rejectPolicy.reject(this, task);
            } else {
                queue.addLast(task);
                log.debug("加入任务队列:{}", task);
                emptyWait.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
