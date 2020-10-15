Java并发编程 —— Future
=====================

1. Java 1.5 开始提供了 Callable 和 Future，通过它们可以在任务执行完毕后得到任务的执行结果。
2. Future 接口可以构建异步应用，是多线程开发中常见的设计模式。
3. 当调用一个执行很慢的方法时，需要等待直到返回结果，但有时候可能并不急着要结果，因此可以让被调用者立即返回，让它在后台慢慢处理这个请求，对于调用者来说，还可以先处理一些其他任务，在真正需要这个方法的返回结果时再去尝试获取结果。

### Callable 与 Runnable
java.lang.Runnable 是一个接口，在它里面只声明了一个 run() 方法，其返回值是 void，任务执行完毕后无法返回任何结果。
```java
public interface Runnable {
    public abstract void run();
}
```
Callable 位于 java.util.concurrent 包下，它也是一个接口，在它里面只声明了一个 call() 方法，这是一个泛型接口，call() 方法返回的类型就是传递进来的 V 类型。
```java
public interface Callable<V> {
    V call() throws Exception;
}
```

### Future 与 Callable
Future 就是对于具体的 Runnable 或 Callable 任务的执行结果进行取消、查询是否完成、获取结果。必要时可以通过 get() 方法获取执行结果，该方法会阻塞直到任务返回结果。
```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    V get() throws InterruptedException, ExecutionException;
    V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
```
怎么使用 Future 和 Callable 呢？一般情况下是配合 ExecutorService 来使用的，在 ExecutorService 接口中声明了若干个 submit() 方法的重载版本：
```text
<T> Future<T> submit(Callable<T> task); 
<T> Future<T> submit(Runnable task, T result);  
Future<?> submit(Runnable task);
```

Future + Callable 示例请参考：com.study.test.async.TestFuture

Future 接口的局限性 —— 很难直接表述多个 Future 结果之间的依赖性，开发中经常需要达成以下目的：
1. 将两个异步计算合并为一个（这两个异步计算之间相互独立，同时第二个又依赖于第一个的结果）
2. 等待 Future 集合中的所有任务都完成。
3. 仅等待 Future 集合中最快结束的任务完成，并返回它的结果。

# 参考
- https://blog.csdn.net/ccc_ccc8/article/details/89667122
