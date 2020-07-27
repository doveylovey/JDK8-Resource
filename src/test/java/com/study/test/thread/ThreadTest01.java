package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;

/**
 * 作用描述：Java 多线程的实现方式
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年07月26日
 */
public class ThreadTest01 {
    @Test
    public void test01() {
        // 在控制台中输出的 main，其实就是一个名称叫做 main 的线程在执行 test01()
        System.out.println(Thread.currentThread().getName());
    }

    /**
     * 通过继承 Thread 类实现线程
     */
    class MyThread01 extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println("执行 MyThread01 类的 run() 方法！");
        }
    }

    @Test
    public void testMyThread01() {
        MyThread01 myThread01 = new MyThread01();
        // 调用 start() 方法启动线程。注意不能直接调用 run() 方法，否则起不到多线程的作用
        myThread01.start();
        // 多次调用 start() 方法则会抛出 java.lang.IllegalThreadStateException 异常
        //myThread01.start();
        System.out.println("运行结束！");
    }

    /**
     * 通过继承 Thread 类实现线程
     */
    class MyThread02 extends Thread {
        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int time = (int) (Math.random() * 1000);
                    TimeUnit.MILLISECONDS.sleep(time);
                    System.out.println("run=" + Thread.currentThread().getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMyThread02() {
        // 测试线程执行的无序性
        MyThread02 myThread02 = new MyThread02();
        myThread02.setName("MyThread02");
        myThread02.start();
        try {
            for (int i = 0; i < 10; i++) {
                int time = (int) (Math.random() * 1000);
                TimeUnit.MILLISECONDS.sleep(time);
                System.out.println("testMyThread02=" + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过继承 Thread 类实现线程
     */
    class MyThread03 extends Thread {
        private int i;

        public MyThread03(int i) {
            super();
            this.i = i;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " ===> " + i);
        }
    }

    @Test
    public void testMyThread03() {
        // 测试线程执行的无序性
        MyThread03 mt01 = new MyThread03(1);
        MyThread03 mt02 = new MyThread03(2);
        MyThread03 mt03 = new MyThread03(3);
        MyThread03 mt04 = new MyThread03(4);
        MyThread03 mt05 = new MyThread03(5);
        MyThread03 mt06 = new MyThread03(6);
        MyThread03 mt07 = new MyThread03(7);
        MyThread03 mt08 = new MyThread03(8);
        MyThread03 mt09 = new MyThread03(9);
        MyThread03 mt10 = new MyThread03(10);
        // 执行 start() 方法的顺序并不代表线程的执行顺序
        mt01.start();
        mt02.start();
        mt03.start();
        mt04.start();
        mt05.start();
        mt06.start();
        mt07.start();
        mt08.start();
        mt09.start();
        mt10.start();
    }

    /**
     * 通过实现 Runnable 接口实现线程
     */
    class MyRunnable01 implements Runnable {
        @Override
        public void run() {
            System.out.println("执行 MyRunnable01 类的 run() 方法！");
        }
    }

    @Test
    public void testMyRunnable01() {
        MyRunnable01 myRunnable01 = new MyRunnable01();
        // 注意：Thread 类也实现了 Runnable 接口
        new Thread(myRunnable01).start();
        System.out.println("运行结束！");
    }

    /**
     * 通过实现 Callable<V> 接口实现线程
     */
    class MyCallable01 implements Callable<String> {
        @Override
        public String call() throws Exception {
            String threadName = Thread.currentThread().getName();
            System.out.println("执行了 MyCallable01 类的 call() 方法：" + threadName);
            return threadName;
        }
    }

    @Test
    public void testMyCallable01() {
        // 创建 MyCallable01 实例
        Callable<String> myCallable01 = new MyCallable01();
        // 通过 Callable 创建 FutureTask 实例
        FutureTask<String> futureTask = new FutureTask<>(myCallable01);
        // 通过 FutureTask 创建 Thread 实例
        Thread thread = new Thread(futureTask);
        // 开启线程
        thread.start();
        try {
            System.out.println("线程的执行结果为：" + futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Java 通过 Executors 提供4种线程池，分别为：
     * newCachedThreadPool：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
     * newFixedThreadPool：创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     * newSingleThreadExecutor：创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
     * newScheduledThreadPool：创建一个可定期或者延时执行任务的定长线程池，支持定时及周期性任务执行。
     * <p>
     * 线程池任务执行流程：
     * 1、当线程池小于 corePoolSize 时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
     * 2、当线程池达到 corePoolSize 时，新提交任务将被放入 workQueue 中，等待线程池中任务调度执行
     * 3、当 workQueue 已满，且 maximumPoolSize > corePoolSize 时，新提交任务会创建新线程执行任务
     * 4、当提交任务数超过 maximumPoolSize 时，新提交任务由 RejectedExecutionHandler 处理
     * 5、当线程池中超过 corePoolSize 线程，空闲时间达到 keepAliveTime 时，关闭空闲线程
     * 6、当设置 allowCoreThreadTimeOut(true) 时，线程池中 corePoolSize 线程空闲时间达到 keepAliveTime 也将关闭
     */
    @Test
    public void testThreadPool01() {
        // 底层：返回 ThreadPoolExecutor 实例，corePoolSize 为 0；maximumPoolSize 为 Integer.MAX_VALUE；keepAliveTime 为 60L；unit 为 TimeUnit.SECONDS；workQueue 为 SynchronousQueue(同步队列)
        // 通俗：当有新任务到来，则插入到 SynchronousQueue 中，由于 SynchronousQueue 是同步队列，因此会在池中寻找可用线程来执行，若有可以线程则执行，若没有可用线程则创建一个线程来执行该任务；若池中线程空闲时间超过指定大小，则该线程会被销毁。
        // 适用：执行很多短期异步的小程序或者负载较轻的服务器
        /*ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 500; i++) {
            cachedThreadPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("【newCachedThreadPool()线程池执行方法示例】当前线程：" + threadName);
            });
        }*/

        // 底层：返回 ThreadPoolExecutor 实例，接收参数为所设定线程数量 nThread，corePoolSize 为 nThread，maximumPoolSize 为 nThread；keepAliveTime 为 0L(不限时)；unit 为 TimeUnit.MILLISECONDS；WorkQueue 为 new LinkedBlockingQueue<Runnable>() 无界阻塞队列
        // 通俗：创建可容纳固定数量线程的池子，每隔线程的存活时间是无限的，当池子满了就不在添加线程了；如果池中的所有线程均在繁忙状态，对于新任务会进入阻塞队列中(无界的阻塞队列)
        // 适用：执行长期的任务，性能好很多
        /*ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 500; i++) {
            fixedThreadPool.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("【newFixedThreadPool()线程池执行方法示例】当前线程：" + threadName);
            });
        }*/

        // 底层：FinalizableDelegatedExecutorService 包装的 ThreadPoolExecutor 实例，corePoolSize 为 1；maximumPoolSize 为 1；keepAliveTime 为 0L；unit 为 TimeUnit.MILLISECONDS；workQueue 为 new LinkedBlockingQueue<Runnable>() 无解阻塞队列
        // 通俗：创建只有一个线程的线程池，且线程的存活时间是无限的；当该线程正繁忙时，对于新任务会进入阻塞队列中(无界的阻塞队列)
        // 适用：一个任务一个任务执行的场景
        /*ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 500; i++) {
            singleThreadExecutor.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("【newSingleThreadExecutor()线程池执行方法示例】当前线程：" + threadName);
            });
        }*/

        // 底层：创建 ScheduledThreadPoolExecutor 实例，corePoolSize 为传递来的参数，maximumPoolSize 为 Integer.MAX_VALUE；keepAliveTime 为 0；unit 为 TimeUnit.NANOSECONDS；workQueue 为 new DelayedWorkQueue() 一个按超时时间升序排序的队列
        // 通俗：创建一个固定大小的线程池，线程池内线程存活时间无限制，线程池可以支持定时及周期性任务执行，如果所有线程均处于繁忙状态，对于新任务会进入 DelayedWorkQueue 队列中，这是一种按照超时时间排序的队列结构
        // 适用：周期性执行任务的场景
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        System.out.println("当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        scheduledThreadPool.schedule(() -> {
            System.out.println("【newScheduledThreadPool()线程池执行方法示例】当前线程：" + Thread.currentThread().getName() + "，3 秒后执行，当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }, 3, TimeUnit.SECONDS);
        scheduledThreadPool.scheduleAtFixedRate(() -> {
            System.out.println("【newScheduledThreadPool()线程池执行方法示例】当前线程：" + Thread.currentThread().getName() + "，延迟 2 秒后每 3 秒执行一次，当前时间：" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }, 2, 3, TimeUnit.SECONDS);
        /*for (int i = 0; i < 500; i++) {
            scheduledThreadPool.execute(() -> {
                System.out.println("【newScheduledThreadPool()线程池执行方法示例】当前线程：" + Thread.currentThread().getName() + "，执行普通任务");
            });
        }*/

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
