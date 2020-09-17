package com.study.test.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 作用描述：实现 Callable 接口创建线程(配合 Future 类使用)
 * 1、创建 Callable 接口的实现类，并实现 call() 方法，该方法将作为线程执行体，并且有返回值
 * 2、创建 Callable 实现类的实例
 * 3、使用 FutureTask 类来包装 Callable 对象，FutureTask 封装了 call() 方法的返回值
 * 4、使用 FutureTask 对象作为 Thread 对象的 target 创建并启动新线程
 * 5、调用 FutureTask 对象的 get() 方法来获得子线程执行结束后的返回值
 * 注：FutureTask 是一个包装器，它通过 Callable 创建，它同时实现了 Future 和 Runnable 接口。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年09月17日
 */
public class MyCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int i = 0;
        for (; i < 100; i++) {
            // Thread.currentThread() 方法返回当前正在执行的线程对象
            System.out.println(Thread.currentThread().getName() + "====>" + i);
        }
        return i;
    }

    public static void main(String[] args) {
        // Callable 接口是 Java 新增的接口，而且它不是 Runnable 接口的子接口，所以 Callable 对象不能直接作为 Thread 的 target；另外，call() 方法有返回值，它也不是直接调用，而是作为线程执行体被调用的，所以这里涉及获取 call() 方法返回值的问题。
        // 于是 Java5 提供了 Future 接口来代表 Callable 接口里 call() 方法的返回值，并为 Future 接口提供了一个 FutureTask 实现类，该类实现了 Future 和 Runnable 接口，所以 FutureTask 可以作为 Thread 类的 target。
        // 在 Future 接口里定义了如下几个公共方法来控制与它关联的 Callable 任务：
        // boolean cancel(boolean mayInterruptIfRunning)：试图取消 Future 里关联的 Callable 任务
        // V get()：返回 Callable 任务里 call() 方法的返回值，调用该方法将导致程序阻塞，必须等到子线程结束以后才会得到返回值
        // V get(long timeout, TimeUnit unit)：返回 Callable 任务里 call() 方法的返回值，该方法让程序最多阻塞 timeout 和 unit 指定的时间，如果经过指定时间后，Callable 任务依然没有返回值，将会抛出 TimeoutException 异常
        // boolean isCancelled()：如果 Callable 任务正常完成前被取消，则返回 true
        // boolean isDone()：如果 Callable 任务已经完成， 则返回 true
        MyCallable myCallable = new MyCallable();
        FutureTask<Integer> futureTask = new FutureTask<>(myCallable);
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " 中的循环变量 i = " + i);
            if (i == 20) {
                new Thread(futureTask, "有返回值的线程").start();
            }
        }
        try {
            System.out.println("子线程的返回值：" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}