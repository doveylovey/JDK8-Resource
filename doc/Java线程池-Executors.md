线程池之 Executors
=================

# Java 通过 Executors 提供四种线程池
Executors 创建线程池时实际上调用了 ThreadPoolExecutor 类的构造方法(定时任务使用的是 ScheduledThreadPoolExecutor)。

### newSingleThreadExecutor：创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
```java
public class Executors {
    public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>()));
    }

    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
        return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory));
    }
}
```
- 底层：FinalizableDelegatedExecutorService 包装的 ThreadPoolExecutor 实例。其 corePoolSize 为 1，maximumPoolSize 为 1，keepAliveTime 为 0L，unit 为 TimeUnit.MILLISECONDS，workQueue 为 LinkedBlockingQueue(无界阻塞队列)
- 通俗：创建只有一个线程的线程池，且线程的存活时间是无限的；当该线程正繁忙时，对于新任务会进入阻塞队列中
- 适用：一个任务一个任务执行的场景

### newCachedThreadPool：创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
```java
public class Executors {
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    }

    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), threadFactory);
    }
}
```
- 底层：返回 ThreadPoolExecutor 实例。其 corePoolSize 为 0，maximumPoolSize 为 Integer.MAX_VALUE，keepAliveTime 为 60L，unit 为 TimeUnit.SECONDS，workQueue 为 SynchronousQueue(同步队列)
- 通俗：当有新任务到来，则插入到 SynchronousQueue 中，由于 SynchronousQueue 是同步队列，因此会在线程池中寻找可用线程来执行，若有可用线程则执行，若没有可用线程则创建一个线程来执行该任务；若池中线程空闲时间超过指定大小，则该线程会被销毁
- 适用：执行很多短期异步的小程序或者负载较轻的服务器

### newFixedThreadPool：创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待
```java
public class Executors {
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory);
    }
}
```
- 底层：返回 ThreadPoolExecutor 实例，接收参数为所设定线程数量 nThread。其 corePoolSize 为 nThread，maximumPoolSize 为 nThread，keepAliveTime 为 0L(不限时)，unit 为 TimeUnit.MILLISECONDS，WorkQueue 为 LinkedBlockingQueue(无界阻塞队列)
- 通俗：创建可容纳固定数量线程的池子，每个线程的存活时间是无限的，当池子满了就不再添加新线程；如果池子中的所有线程均在繁忙状态，则新任务会进入阻塞队列中
- 适用：执行长期的任务，性能好很多

### newScheduledThreadPool：创建一个可定期或者延时执行任务的定长线程池，支持定时及周期性任务执行
```java
public class Executors {
    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        return new ScheduledThreadPoolExecutor(corePoolSize);
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, ThreadFactory threadFactory) {
        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
    }
}
```
- 底层：创建 ScheduledThreadPoolExecutor 实例。其 corePoolSize 为传递来的参数，maximumPoolSize 为 Integer.MAX_VALUE，keepAliveTime 为 0，unit 为 TimeUnit.NANOSECONDS，workQueue 为 DelayedWorkQueue(一个按超时时间升序排序的队列)
- 通俗：创建一个固定大小的线程池，线程池内线程存活时间无限制，线程池可以支持定时及周期性任务执行，如果所有线程均处于繁忙状态，对于新任务会进入DelayedWorkQueue队列中，这是一种按照超时时间排序的队列结构
- 适用：周期性执行任务的场景

# 参考
- https://www.cnblogs.com/zincredible/p/10984459.html


