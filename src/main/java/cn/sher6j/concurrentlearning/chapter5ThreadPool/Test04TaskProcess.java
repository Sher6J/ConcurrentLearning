package cn.sher6j.concurrentlearning.chapter5ThreadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * execute 执行 Runnable
 * submit 执行 Callable
 * invokeAll 执行若干个 Callable ，均执行
 * invokeAny 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
 * @author sher6j
 * @create 2020-10-06-18:48
 */
@Slf4j(topic = "c.Submit")
public class Test04TaskProcess {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        testInvokeAny(pool);
    }

    private static void testInvokeAny(ExecutorService pool) throws InterruptedException, ExecutionException {
        String res = pool.invokeAny(Arrays.asList(
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(2);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "3";
                }
        ));

        log.debug("{}", res);
    }

    private static void testInvokeAll(ExecutorService pool) throws InterruptedException {
        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(2);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    TimeUnit.SECONDS.sleep(1);
                    return "3";
                }
        ));

        futures.forEach(f -> {
            try {
                log.debug("{}", f.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Submit执行Callable接口有返回值
     * @param pool
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void testSubmit(ExecutorService pool) throws InterruptedException, ExecutionException {
        Future<String> future = pool.submit(() -> {
            log.debug("running");
            TimeUnit.SECONDS.sleep(1);
            return "ok";
        });

        log.debug("{}", future.get());
    }
}
