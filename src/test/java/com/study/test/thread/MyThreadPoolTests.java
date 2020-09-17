package com.study.test.thread;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolTests {
    /**
     * 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
     */
    @Test
    public void newSingleThreadExecutorTest() throws InterruptedException {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            final int ii = i;
            pool.execute(() -> System.out.println(Thread.currentThread().getName() + "=>" + ii));
        }
        // 模拟程序一直运行
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    /**
     * 1、创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程，那么就会回收部分空闲(60秒不执行任务)的线程
     * 2、当任务数增加时，此线程池又可以智能的添加新线程来处理任务
     * 3、此线程池不会对线程池大小做限制，线程池大小完全依赖于操作系统(或者说JVM)能够创建的最大线程数
     */
    @Test
    public void newCachedThreadPoolTest() throws InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 1; i <= 10; i++) {
            final int ii = i;
            TimeUnit.SECONDS.sleep(ii * 1);
            pool.execute(() -> System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行" + ii));
        }
        // 模拟程序一直运行
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    /**
     * 1、创建固定大小的线程池。每次提交一个任务就创建一个线程，直到线数量程达到线程池的最大大小
     * 2、线程池的大小一旦达到最大值就会保持不变，如果某个线程因为执行异常而结束，那么线程池会补充一个新线程
     * 3、因为线程池大小为3，每个任务输出 index 后 sleep 2秒，所以每两秒打印3个数字和线程名称
     */
    @Test
    public void newFixedThreadPoolTest() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            final int ii = i;
            pool.execute(() -> {
                System.out.println("线程名称：" + Thread.currentThread().getName() + "，执行" + ii);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // 模拟程序一直运行
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    /**
     * 创建一个定长线程池，支持定时及周期性任务执行。延迟执行
     */
    @Test
    public void newScheduledThreadPoolTest() throws InterruptedException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(5);
        Runnable task1 = () -> System.out.println("当前时间：" + System.currentTimeMillis() + "，线程名称：" + Thread.currentThread().getName() + "，执行：3秒后执行");
        pool.schedule(task1, 3, TimeUnit.SECONDS);
        Runnable task2 = () -> System.out.println("当前时间：" + System.currentTimeMillis() + "，线程名称：" + Thread.currentThread().getName() + "，执行：延迟2秒后每5秒执行一次");
        pool.scheduleAtFixedRate(task2, 2, 5, TimeUnit.SECONDS);
        Runnable task3 = () -> System.out.println("当前时间：" + System.currentTimeMillis() + "，线程名称：" + Thread.currentThread().getName() + "，执行：普通任务");
        for (int i = 0; i < 5; i++) {
            pool.execute(task3);
        }
        // 模拟程序一直运行
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void testSubmit() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                if (new Random().nextBoolean()) {
                    // 该异常会在调用 Future.get() 时传递给调用者
                    throw new RuntimeException("调用 Callable 中的 call() 出现异常！");
                } else {
                    return "调用 Callable 中的 call() 的结果";
                }
            }
        });
        System.out.println(future.get());
        // 模拟程序一直运行
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
