线程池之 ThreadPoolExecutor
==========================

# ThreadPoolExecutor 概述
ThreadPoolExecutor 类的所有构造方法定义如下：
```java
public class ThreadPoolExecutor extends AbstractExecutorService {
    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), defaultHandler);
    }
    
    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, Executors.defaultThreadFactory(), handler);
    }
    
    public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        // 限于篇幅，具体实现请看源码
    }
}
```

ThreadPoolExecutor 构造方法的七个参数介绍：
- int corePoolSize：线程池的核心线程数。在没有设置 allowCoreThreadTimeOut 为 true 的情况下(默认 false)，核心线程在线程池中一直存活，即使其处于空闲状态
- int maximumPoolSize：线程池所能容纳的最大线程数。当活动线程(核心线程 + 非核心线程)达到这个数值后，后续任务将会根据 RejectedExecutionHandler 来进行拒绝策略处理
- long keepAliveTime：非核心线程闲置时的超时时长。超过该时长，非核心线程就会被回收。若线程池设置了 allowCoreThreadTimeOut 为 true，则该时长同样会作用于核心线程
- TimeUnit unit：参数 keepAliveTime 对应的单位
- BlockingQueue<Runnable> workQueue：线程池中的任务队列，通过线程池的 execute() 方法提交的 Runnable 对象会存储在该队列中
- ThreadFactory threadFactory：线程工厂，即为线程池提供创建新线程的功能。这是一个接口，可以通过自定义，做一些自定义线程名的操作
- RejectedExecutionHandler handler)：当任务无法被执行(即 workQueue 被排满，且线程数超过 maximumPoolSize)时的处理策略，线程池提供了四种拒绝策略，也可以自定义
> 在这七个参数中，比较容易引起问题的有：corePoolSize 和 maximumPoolSize 设置不当会影响效率，甚至耗尽线程；workQueue 设置不当容易导致 OOM；handler 设置不当会导致提交任务时抛出异常。

### 线程池接受任务的形式：Runnable、Callable
- 方法签名不同：Runnable 接口中的定义是 void run()；Callable 接口中的定义是 V call() throws Exception
- 是否允许有返回值，Callable 允许有返回值
- 是否允许抛出异常，Callable 允许抛出异常 
> Callable 是 JDK1.5 时加入的接口，作为 Runnable 的一种补充，允许有返回值，允许抛出异常。

### 向线程池提交任务的三种方式：submit() 和 execute()
- Future<T> submit(Callable<T> task)：关心返回结果
- void execute(Runnable command)：不关心返回结果
- Future<?> submit(Runnable task)：不关心返回结果，虽然返回值类型是 Future，但是其 get() 方法总是返回 null

# 如何正确使用线程池：避免使用无界队列、明确提交新任务时的拒绝策略、获取处理结果和异常
### 避免使用无界队列
不要使用 Executors.newXxxThreadPool() 快捷方法创建线程池，因为这种方式会使用无界的任务队列，为避免 OOM，应该通过 ThreadPoolExecutor 构造方法手动创建线程池，并指定队列的最大长度，例如：
```
ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512), new ThreadPoolExecutor.DiscardPolicy());
```

### 明确提交新任务时的拒绝策略
任务队列总有占满的时候，这时再提交新的任务会怎么样呢？RejectedExecutionHandler 接口为我们提供了控制方式，接口定义如下：
```java
public interface RejectedExecutionHandler {
    void rejectedExecution(Runnable r, ThreadPoolExecutor executor);
}
```
线程池给我们提供了几种常见的拒绝策略(当线程池中的线程数大于等于最大线程数时，再向线程池提交任务的处理方式)：
- 拒绝策略之 AbortPolicy：抛 java.util.concurrent.RejectedExecutionException 异常，涉及到该异常的任务也不会被执行，这是线程池默认的拒绝策略
- 拒绝策略之 DiscardPolicy：默默丢弃不能执行的新任务，也不会抛任何异常
- 拒绝策略之 DiscardOldestPolicy：抛弃工作队列头部的任务(即等待时间最久的任务)，尝试为当前新提交的任务腾出位置
- 拒绝策略之 CallerRunsPolicy：如果提交的任务被拒绝了，则由调用线程(提交任务的线程)直接执行此任务

线程池默认的拒绝策略是 AbortPolicy，也就是抛出 RejectedExecutionHandler 异常，该异常是非受检异常，很容易忘记捕获。如果不关心任务被拒绝的事件，则可以将拒绝策略设置成 DiscardPolicy，这样多余的任务会悄悄的被忽略，例如：
```
ExecutorService executorService = new ThreadPoolExecutor(2, 2, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512), new ThreadPoolExecutor.DiscardPolicy());
```

### 获取处理结果和异常
线程池的处理结果、处理过程中的异常都被包装到 Future 中，并在调用 future.get() 方法时获取，执行过程中的异常会被包装成 ExecutionException，submit() 方法本身不会传递结果和任务执行过程中的异常，例如：
```
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
```

# 线程池的常用场景
### 正确构造线程池
```
int poolSize = Runtime.getRuntime().availableProcessors() * 2;
BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(512);
RejectedExecutionHandler policy = new ThreadPoolExecutor.DiscardPolicy();
ExecutorService executorService = new ThreadPoolExecutor(poolSize, poolSize, 0, TimeUnit.SECONDS, queue, policy);    
```

### 获取单个结果
通过 submit() 向线程池提交任务后会返回一个 Future，调用 future.get() 方法能够阻塞等待执行结果，get(long timeout, TimeUnit unit) 方法可以指定等待的超时时间。

### 获取多个结果
如果向线程池提交了多个任务，要获取这些任务的执行结果，可依次调用 future.get() 获得。但这种场景更应该使用 ExecutorCompletionService，该类的 take() 方法总是阻塞等待某一个任务完成，然后返回该任务的 Future 对象。向 CompletionService 批量提交任务后，只需调用相同次数的 CompletionService 的 take() 方法，就能获取所有任务的执行结果，获取顺序是任意的，取决于任务的完成顺序：
```
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
    for (int i = 0; i < list.size(); ++i) {
        // 获取每一个完成的任务的结果
        String result = completionService.take().get();
        System.out.println(result);
    }
}
```

### 单个任务的超时时间
V Future.get(long timeout, TimeUnit unit) 方法可以指定等待的超时时间，超时未完成会抛出 TimeoutException 异常。

### 多个任务的超时时间
等待多个任务完成，并设置最大等待时间，可以通过 CountDownLatch 完成：
```
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
    latch.await(10, TimeUnit.SECONDS);
}
```

# 线程池任务执行流程
- 当线程池中的线程数小于 corePoolSize 时，新提交的任务将会创建一个新线程执行，即使此时线程池中存在空闲线程
- 当线程池中的线程数达到 corePoolSize 时，新提交的任务将被放入 workQueue 中，等待线程池中的线程调度执行
- 当 workQueue 已满且 maximumPoolSize 大于 corePoolSize 时，新提交的任务将会创建新线程执行
- 当提交任务数超过 maximumPoolSize 时，新提交任务由 RejectedExecutionHandler 处理
- 当线程池中的线程数超过 corePoolSize 时，空闲时间达到 keepAliveTime 的线程将被关闭
- 当设置 allowCoreThreadTimeOut(true) 时，线程池中 corePoolSize 线程空闲时间达到 keepAliveTime 也将关闭

# ThreadPoolExecutor 的重要参数
- corePoolSize：核心线程数
```
1、核心线程会一直存活，及时没有任务需要执行
2、当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理
3、如果设置了 allowCoreThreadTimeout=true(默认false)，则核心线程也会超时关闭
```

- queueCapacity：任务队列容量(阻塞队列)
```code
1、当核心线程数达到最大时，新任务会放在队列中排队等待执行
```

- maxPoolSize：最大线程数
```code
1、当线程数 >= corePoolSize 且任务队列已满时，线程池会创建新线程来处理任务
2、当线程数 = maxPoolSize 且任务队列已满时，线程池会拒绝处理任务而抛出异常
```

- keepAliveTime：线程空闲时间
```code
1、当线程的空闲时间达到 keepAliveTime 时，线程会退出，直到线程数量等于 corePoolSize
2、如果设置了 allowCoreThreadTimeout=true，则线程会退出，直到线程数量等于0
```

- allowCoreThreadTimeout：允许核心线程超时

- rejectedExecutionHandler：任务拒绝处理器
```code
1、两种情况下会拒绝处理任务：当线程数已经达到 maxPoolSize 且队列已满，会拒绝新任务；当线程池被调用 shutdown() 后，会等待线程池里的任务执行完毕后再 shutdown。如果在调用 shutdown() 和线程池真正 shutdown 之间提交任务，则会拒绝这些任务
2、线程池会调用 rejectedExecutionHandler 来处理这个任务，如果未设置则默认是 AbortPolicy，会抛出异常
3、ThreadPoolExecutor 类有几个内部实现类来处理这类情况，分别是：AbortPolicy-丢弃任务并抛出 RejectedExecutionException 异常；CallerRunsPolicy-由调用线程处理该任务；DiscardPolicy-静默丢弃任务，且不抛出异常；DiscardOldestPolicy-丢弃队列最前面的任务，然后重新提交被拒绝的任务
4、通过实现 RejectedExecutionHandler 接口来自定义拒绝策略
```

参考
- https://www.cnblogs.com/zincredible/p/10984459.html
- https://www.cnblogs.com/CarpenterLee/p/9558026.html
- https://www.cnblogs.com/waytobestcoder/p/5323130.html


