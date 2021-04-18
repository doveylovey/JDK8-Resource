package com.study.test.thread;

import java.util.concurrent.*;

/**
 * 该类的描述
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021-04-18
 */
public class ThreadPoolUtil {
    private static volatile ThreadPoolUtil INSTANCE;
    private ExecutorService pool;

    private ThreadPoolUtil() {
        //int processors = Runtime.getRuntime().availableProcessors();
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        pool = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, workQueue);
    }

    public static ThreadPoolUtil getInstance() {
        if (null == INSTANCE) {
            synchronized (ThreadPoolUtil.class) {
                if (null == INSTANCE) {
                    INSTANCE = new ThreadPoolUtil();
                }
            }
        }
        return INSTANCE;
    }

    public void executeThread(Runnable run) {
        pool.execute(run);
    }

    public <V> Future<V> executeThread(Callable<V> call) {
        Future<V> f = pool.submit(call);
        return f;
    }
}
