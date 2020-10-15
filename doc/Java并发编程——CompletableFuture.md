Java并发编程 —— CompletableFuture
================================

注：CompletableFuture 类实现了 CompletionStage 和 Future 接口，因此可以像 Future 那样使用它。

### 创建 CompletableFuture 对象
说明：以 Async 结尾的方法都是可以异步执行的，如果指定了线程池，则会在指定的线程池中执行，如果没有指定，默认会在 ForkJoinPool.commonPool() 中执行。下面很多方法都是类似的，不再做特别说明。

四个静态方法用来为一段异步执行的代码创建 CompletableFuture 对象，方法的参数类型都是函数式接口，所以可以使用 lambda 表达式实现异步任务。

- runAsync() 方法以 Runnable 函数式接口类型为参数，所以 CompletableFuture 的计算结果为空。
- supplyAsync() 方法以 Supplier<U> 函数式接口类型为参数，CompletableFuture 的计算结果类型为 U。

```text
public static CompletableFuture<Void> runAsync(Runnable runnable);
public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor);
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier);
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor);
```

### 变换结果。示例请参考：com.study.test.async.TestCompletableFuture.completableFutureTest01
```text
public <U> CompletionStage<U> thenApply(Function<? super T,? extends U> fn);
public <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn);
public <U> CompletionStage<U> thenApplyAsync(Function<? super T,? extends U> fn, Executor executor);
```
说明：这些方法的输入是上一个阶段计算后的结果，返回值是经过转化后结果。

### 消费结果。示例请参考：com.study.test.async.TestCompletableFuture.completableFutureTest02
```text
public CompletionStage<Void> thenAccept(Consumer<? super T> action);
public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action);
public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
```
说明：这些方法只是针对结果进行消费，入参是 Consumer，没有返回值。

### 结合两个 CompletionStage 的结果，进行转化后返回。示例请参考：com.study.test.async.TestCompletableFuture.completableFutureTest03
```text
public <U,V> CompletionStage<V> thenCombine(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn);
public <U,V> CompletionStage<V> thenCombineAsync(CompletionStage<? extends U> other,BiFunction<? super T,? super U,? extends V> fn,Executor executor);
```
需要上一阶段的返回值，并且 other 代表的 CompletionStage 也要返回值之后，把这两个返回值，进行转换后返回指定类型的值。
    
说明：同样也存在对两个 CompletionStage 结果进行消耗的一组方法，例如 thenAcceptBoth() ，这里不再进行示例。

### 两个 CompletionStage 谁计算的快，就用那个 CompletionStage 的结果进行下一步的处理。示例请参考：com.study.test.async.TestCompletableFuture.completableFutureTest04
```text
public <U> CompletionStage<U> applyToEither(CompletionStage<? extends T> other,Function<? super T, U> fn);
public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn);
public <U> CompletionStage<U> applyToEitherAsync(CompletionStage<? extends T> other,Function<? super T, U> fn,Executor executor);
```
两种渠道完成同一个事情，就可以调用这个方法，找一个最快的结果进行处理，最终有返回值。

### 运行时出现了异常可以通过 exceptionally() 进行补偿。示例请参考：com.study.test.async.TestCompletableFuture.completableFutureTest05
```text
public CompletionStage<T> exceptionally(Function<Throwable, ? extends T> fn);
```

# 参考
- https://blog.csdn.net/ccc_ccc8/article/details/89667122
