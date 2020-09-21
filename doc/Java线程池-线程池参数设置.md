Java线程池-线程池参数设置
======================

# ThreadPoolExecutor 构造方法参数详解
### 构造方法
```text
public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
    // 限于篇幅，具体实现请看源码
}
```
### 参数说明
1、corePoolSize: 核心线程数
```text
核心线程会一直存活，即使没有任务需要执行。
当线程数小于核心线程数时，即使有线程空闲，线程池也会优先创建新线程处理。
设置 allowCoreThreadTimeout = true(默认值为 false)时，核心线程也会超时关闭。
```

合理设置 corePoolSize 的前提是清楚任务属于 CPU 密集型还是 IO 密集型。
```text
(1)、CPU 密集型(CPU bound)
CPU 密集型也叫计算密集型，指的是系统的硬盘、内存性能相对 CPU 要好很多，此时系统运作大部分的状况是 CPU Loading 100%，CPU 要读/写 I/O(硬盘/内存)，I/O 在很短的时间就可以完成，而 CPU 还有许多运算要处理，CPU Loading 很高。
在多重程序系统中，大部份时间用来做计算、逻辑判断等 CPU 动作的程序称之 CPU bound。例如一个计算圆周率至小数点一千位以下的程序，在执行的过程当中绝大部份时间用在三角函数和开根号的计算，便是属于 CPU bound 的程序。
CPU bound 的程序一般而言 CPU 占用率相当高，这可能是因为任务本身不太需要访问 I/O 设备，也可能是因为程序是多线程实现因此屏蔽掉了等待 I/O 的时间。

(2)、IO 密集型(I/O bound)
IO 密集型指的是系统的 CPU 性能相对硬盘、内存要好很多，此时系统运作大部分的状况是 CPU 在等 I/O (硬盘/内存) 的读/写操作，此时 CPU Loading 并不高。
I/O bound 的程序一般在达到性能极限时，CPU 占用率仍然较低，这可能是因为任务本身需要大量 I/O 操作，而 pipeline 做得不是很好，没有充分利用处理器能力。

(3)、机器的 CPU 核数
CPU核数 = Runtime.getRuntime().availableProcessors();

(4)、根据任务类型(CPU 密集型、IO 密集型)设置 corePoolSize 的值
CPU密集型：corePoolSize = CPU核数 + 1
IO密集型：corePoolSize = CPU核数 * 2
```

2、maximumPoolSize：最大线程数
```text
当线程数 >= corePoolSize 且任务队列已满时，线程池会创建新线程来处理任务
当线程数 = maxPoolSize 且任务队列已满时，线程池会拒绝处理任务而抛出异常
```

3、keepAliveTime：线程空闲时间
```text
当线程空闲时间达到 keepAliveTime 时，线程会退出，直到线程数量等于 corePoolSize
如果 allowCoreThreadTimeout = true，则会直到线程数量等于 0
```

4、workQueue：工作队列
```text
当核心线程数达到最大时，新任务会放在队列中排队等待执行
```
5、allowCoreThreadTimeout：允许核心线程超时

6、rejectedExecutionHandler：任务拒绝处理器
```text
两种情况会拒绝处理任务：
(1)、当线程数已经达到 maxPoolSize 且队列已满，则会拒绝新任务
(2)、当线程池被调用 shutdown () 后，会等待线程池里的任务执行完毕再 shutdown。如果在调用 shutdown() 和线程池真正 shutdown 之间提交任务，则会拒绝新任务

线程池会调用 java.util.concurrent.RejectedExecutionHandler.rejectedExecution(Runnable r, ThreadPoolExecutor executor) 来拒绝任务，如果没有设置则默认是 AbortPolicy，有以下几种实现：
AbortPolicy：抛 java.util.concurrent.RejectedExecutionException 异常，涉及到该异常的任务也不会被执行，这是线程池默认的拒绝策略
DiscardPolicy：默默丢弃不能执行的新任务，也不会抛任何异常
DiscardOldestPolicy：抛弃工作队列头部的任务(即等待时间最久的任务)，尝试为当前新提交的任务腾出位置
CallerRunsPolicy：如果提交的任务被拒绝了，则由调用线程(提交任务的线程)直接执行此任务
```

# ThreadPoolExecutor 构造方法参数如何设置
### 线程池参数默认值
```text
corePoolSize = 1
queueCapacity = Integer.MAX_VALUE
maxPoolSize = Integer.MAX_VALUE
rejectedExecutionHandler = AbortPolicy()
keepAliveTime = 60s
allowCoreThreadTimeout = false
```

### 如何根据需求进行定制
前提条件
> tasks：每秒的任务数，假设为 500~1000；taskCost：每个任务花费时间，假设为 0.1s；responseTime：系统允许容忍的最大响应时间，假设为 1s。

corePoolSize 的设置(corePoolSize = 每秒需要多少个线程处理)
> 通过 threadCount = tasks / (1 / taskCost) = tasks * taskCost 计算 threadCount =  (500~1000) * 0.1 = 50~100 个线程，即：corePoolSize 设置应该大于 50，根据 8020 原则，如果 80% 的每秒任务数小于 800，那么 corePoolSize 设置为 80 即可。

queueCapacity 的设置
> 通过 queueCapacity = (coreSizePool / taskCost) * responseTime 计算 queueCapacity = 80 / 0.1 * 1 = 80，意思是队列里的线程可以等待 1s，超过了的需要新开线程来执行，切记不能设置为 Integer.MAX_VALUE，这样队列会很大，线程数只会保持在 corePoolSize 大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。

maxPoolSize 的设置
> 通过 maxPoolSize = (max(tasks) - queueCapacity) / (1 / taskCost) 计算 maxPoolSize = (1000-80) / 10 = 92，即：最大线程数 = (最大任务数 - 队列容量) / 每个线程每秒处理能力。

rejectedExecutionHandler 的设置
> rejectedExecutionHandler 需视具体情况而定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理。

keepAliveTime 的设置
> keepAliveTime 采用默认值通常就能满足需求。

allowCoreThreadTimeout 的设置
> allowCoreThreadTimeout 采用默认值通常也能满足需求。

【注意】以上都是理想值，实际情况下要根据机器性能来决定。如果在未达到最大线程数的情况机器 cpu load 已经满了，则需要通过升级硬件和优化代码，降低 taskCost 来处理。

# 参考
- https://blog.csdn.net/riemann_/article/details/104704197
