package com.study.test.thread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class MyThreadPoolExecutorTests {
    @Test
    public void executorServiceTest01() throws InterruptedException, ExecutionException {
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

    @Test
    public void executorServiceTest02() throws InterruptedException, ExecutionException {
        // 正确构造线程池 begin
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
        RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
        ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, queue, policy);
        // 正确构造线程池 end
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

    @Test
    public void executorServiceTest03() throws InterruptedException {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(5);
        ThreadFactory threadFactory = r -> new Thread(r, "test-thread-pool");
        // ThreadPoolExecutor.AbortPolicy：丢弃任务并抛出 RejectedExecutionException 异常。这是线程池默认的拒绝策略，在任务不能再提交的时候，抛出异常，及时反馈程序运行状态。如果是比较关键的业务，推荐使用此拒绝策略，这样子在系统不能承载更大的并发量的时候，能够及时的通过异常发现。
        // ThreadPoolExecutor.DiscardPolicy：丢弃任务，且不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃。使用此策略，可能会使我们无法发现系统的异常状态。建议是一些无关紧要的业务采用此策略。例如博客网站统计阅读量就可以采用的这种拒绝策略。
        // ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新提交被拒绝的任务。此拒绝策略是一种喜新厌旧的拒绝策略。是否要采用此种拒绝策略，还得根据实际业务是否允许丢弃老任务来认真衡量。
        // ThreadPoolExecutor.CallerRunsPolicy：由调用线程处理该任务。如果任务被拒绝了，则由调用线程（提交任务的线程）直接执行此任务。
        RejectedExecutionHandler policy = new ThreadPoolExecutor.CallerRunsPolicy();
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.SECONDS, queue, threadFactory, policy);
        // 设置核心线程超时后是否自动销毁
        //executorService.allowCoreThreadTimeOut(true);
        for (int i = 0; i < 1000; i++) {
            executorService.submit(() -> {
                try {
                    // 注意：若设置拒绝策略为 ThreadPoolExecutor.CallerRunsPolicy，则 main 线程也会执行任务
                    System.out.println(Thread.currentThread().getName() + "：执行任务！");
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        // 模拟程序一直运行
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void completionServiceTest01() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<>(pool);
        Future<String> future = completionService.submit(new Callable<String>() {
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

    @Test
    public void completionServiceTest02() throws InterruptedException, ExecutionException {
        // 任务
        List<Callable<String>> list = new ArrayList<>();
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 1 次调用 Callable 中的 call() 方法");
            return "第 1 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 2 次调用 Callable 中的 call() 方法");
            return "第 2 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 3 次调用 Callable 中的 call() 方法");
            return "第 3 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 4 次调用 Callable 中的 call() 方法");
            return "第 4 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 5 次调用 Callable 中的 call() 方法");
            return "第 5 次调用 Callable 中的 call() 方法";
        });
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CompletionService<String> completionService = new ExecutorCompletionService<>(pool);
        // 任务执行结果
        List<Future<String>> futureList = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        for (Callable<String> s : list) {
            // 提交所有任务
            Future<String> future = completionService.submit(s);
            futureList.add(future);
        }
        for (int i = 0; i < list.size(); ++i) {
            // 获取每一个完成的任务的结果
            String result = completionService.take().get();
            resultList.add(result);
        }
        System.out.println(resultList);
    }

    @Test
    public void completionServiceTest03() throws InterruptedException, ExecutionException {
        // 任务
        List<Callable<String>> list = new ArrayList<>();
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 1 次调用 Callable 中的 call() 方法");
            return "第 1 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 2 次调用 Callable 中的 call() 方法");
            return "第 2 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 3 次调用 Callable 中的 call() 方法");
            return "第 3 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 4 次调用 Callable 中的 call() 方法");
            return "第 4 次调用 Callable 中的 call() 方法";
        });
        list.add(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 5 次调用 Callable 中的 call() 方法");
            return "第 5 次调用 Callable 中的 call() 方法";
        });
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CompletionService<String> completionService = new ExecutorCompletionService<>(pool);
        for (Callable<String> s : list) {
            // 提交所有任务
            completionService.submit(s);
        }
        // 获取多个任务的结果：如果向线程池提交了多个任务，要获取这些任务的执行结果，可依次调用 future.get() 获得。
        // 但这种场景更应该使用 ExecutorCompletionService，该类的 take() 方法总是阻塞等待某一个任务完成，然后返回该任务的 Future 对象。
        // 向 CompletionService 批量提交任务后，只需调用相同次数的 CompletionService 的 take() 方法，就能获取所有任务的执行结果，获取顺序是任意的，取决于任务的完成顺序
        for (int i = 0; i < list.size(); ++i) {
            // 获取每一个完成的任务的结果
            String result = completionService.take().get();
            System.out.println(result);
        }
    }

    @Test
    public void countDownLatchTest() throws InterruptedException {
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(3);
        // 任务
        List<Runnable> list = new ArrayList<>();
        list.add(() -> System.out.println("线程：" + Thread.currentThread().getName() + "，第 1 次调用 Runnable 中的 run() 方法"));
        list.add(() -> System.out.println("线程：" + Thread.currentThread().getName() + "，第 2 次调用 Runnable 中的 run() 方法"));
        list.add(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程：" + Thread.currentThread().getName() + "，第 3 次调用 Runnable 中的 run() 方法");
        });
        list.add(() -> System.out.println("线程：" + Thread.currentThread().getName() + "，第 4 次调用 Runnable 中的 run() 方法"));
        list.add(() -> System.out.println("线程：" + Thread.currentThread().getName() + "，第 5 次调用 Runnable 中的 run() 方法"));

        CountDownLatch latch = new CountDownLatch(list.size());
        for (Runnable r : list) {
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        r.run();
                    } finally {
                        // countDown
                        latch.countDown();
                    }
                }
            });
        }
        // 指定超时时间
        latch.await(5, TimeUnit.SECONDS);
    }
}
