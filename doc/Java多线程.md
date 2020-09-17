多线程
=====

# Java 中创建线程的三种方式
1、继承 Thread 类创建线程
- 定义 Thread 类的子类，并重写该类的 run() 方法，该方法的方法体就代表了线程要完成的任务。因此把 run() 方法称为执行体
- 创建 Thread 子类的实例，即创建了线程对象
- 调用线程对象的 start() 方法来启动该线程
```java
public class MyThread extends Thread {
    int i = 0;

    // 重写 run() 方法，run() 方法的方法体就是线程执行体
    public void run() {
        for (; i < 100; i++) {
            // 当通过继承 Thread 类的方式实现多线程时，可以直接使用 this 获取当前执行的线程
            System.out.println(this.getName() + ">>>>>" + i);
        }
    }

    public static void main(String[] args) {
        // this.getName() 方法用于返回当前线程的名字，也可以通过 setName() 方法设置当前线程的名字
        // 当程序运行后，至少会创建一个主线程(自动)，主线程的线程执行体不是由 run() 方法确定的，而是由 main() 方法确定的
        // 默认情况下，主线程的线程名字为 main，用户创建的线程的线程名字依次为 Thread—0、Thread—1、...
        for (int i = 0; i < 100; i++) {
            // Thread.currentThread() 方法返回当前正在执行的线程对象
            System.out.println(Thread.currentThread().getName() + "====>" + i);
            if (i == 20) {
                // 创建并启动第一个线程
                new MyThread().start();
                // 创建并启动第二个线程
                new MyThread().start();
            }
        }
    }
}
```
2、实现 Runnable 接口创建线程
- 定义 Runnable 接口的实现类，并重写该接口的 run() 方法，该方法的方法体同样是该线程的线程执行体
- 创建 Runnable 实现类的实例，并以此实例作为 Thread 的 target 来创建 Thread 对象，该 Thread 对象才是真正的线程对象
- 调用线程对象的 start() 方法来启动该线程
```java
public class MyRunnable implements Runnable {
    private int i;

    public void run() {
        for (i = 0; i < 100; i++) {
            // 当线程类实现 Runnable 接口时，要获取当前线程对象只有通过 Thread.currentThread() 方法获取
            System.out.println(Thread.currentThread().getName() + ">>>>>" + i);
        }
    }

    public static void main(String[] args) {
        // 实现 Runnable 接口的类的实例对象仅仅作为 Thread 对象的 target，Runnable 实现类里包含的 run() 方法仅仅作为线程执行体，而实际的线程对象依然是 Thread 实例，这里的 Thread 实例负责执行其 target 的 run() 方法
        // 通过 Runnable 接口来实现多线程时，要获取当前线程对象只能通过 Thread.currentThread() 方法，而不能通过 this 关键字获取
        // 从 Java8 开始，Runnable 接口使用了 @FunctionalInterface 修饰，表明 Runnable 是函数式接口，可使用 lambda 表达式创建对象
        for (int i = 0; i < 100; i++) {
            // Thread.currentThread() 方法返回当前正在执行的线程对象
            System.out.println(Thread.currentThread().getName() + "====>" + i);
            if (i == 20) {
                MyRunnable myRunnable = new MyRunnable();
                // 通过 new Thread(target, name) 的方式创建线程；执行 start() 方法时就会执行被重写的 run() 方法，该方法执行完成后线程就消亡了
                new Thread(myRunnable, "新线程1").start();
                new Thread(myRunnable, "新线程2").start();
            }
        }
    }
}
```
3、实现 Callable 接口创建线程(配合 Future 类使用)
- 创建 Callable 接口的实现类，并实现 call() 方法，该方法将作为线程执行体，并且有返回值
- 创建 Callable 实现类的实例
- 使用 FutureTask 类来包装 Callable 对象，FutureTask 封装了 call() 方法的返回值
- 使用 FutureTask 对象作为 Thread 对象的 target 创建并启动新线程
- 调用 FutureTask 对象的 get() 方法来获得子线程执行结束后的返回值
> 注：FutureTask 是一个包装器，它通过 Callable 创建，它同时实现了 Future 和 Runnable 接口。
```java
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
```

在 Future 接口里定义了如下几个公共方法来控制与它关联的 Callable 任务：
```java
public interface Future<V> {
    boolean cancel(boolean mayInterruptIfRunning);// 试图取消 Future 里关联的 Callable 任务
    V get();// 返回 Callable 任务里 call() 方法的返回值，调用该方法将导致程序阻塞，必须等到子线程结束以后才会得到返回值
    V get(long timeout, TimeUnit unit);// 返回 Callable 任务里 call() 方法的返回值，该方法让程序最多阻塞 timeout 和 unit 指定的时间，如果经过指定时间后，Callable 任务依然没有返回值，将会抛出 TimeoutException 异常
    boolean isCancelled();// 如果 Callable 任务正常完成前被取消，则返回 true
    boolean isDone();// 如果 Callable 任务已经完成， 则返回 true
}
```

# 创建线程的三种方式的对比
1、采用实现 Runnable、Callable 接口的方式创建线程的优缺点
- 优点：线程类实现了 Runnable 或 Callable 接口时还可以继承其他类；在这种方式下，多个线程可以共享同一个 target 对象，所以非常适合多个相同线程来处理同一份资源的情况，从而可以将 CPU、代码和数据分开，形成清晰的模型，较好地体现了面向对象的思想。
- 缺点：编程稍微复杂；如果要访问当前线程，则必须使用 Thread.currentThread() 方法。

2、使用继承 Thread 类的方式创建线程的优缺点
- 优点：编写简单，如果需要访问当前线程，则无需使用 Thread.currentThread() 方法，直接使用 this 即可获得当前线程。
- 缺点：线程类已经继承了 Thread 类，所以不能再继承其他父类；多个线程不能共享同一份资源。

3、Runnable 和 Callable 的区别
- Callable 接口中的方法是 call()，而 Runnable 接口中的方法是 run()。
- Callable 的任务执行后可以有返回值，而 Runnable 的任务是没有返回值的。
- call() 方法可以抛出异常，而 run() 方法不可以。
- 运行 Callable 任务可以拿到一个 Future 对象，表示异步计算的结果。它提供了检查计算是否完成的方法，以等待计算完成，并获得计算结果。通过 Future 对象可以了解任务的执行情况、可以取消任务的执行、可以获取任务的执行结果。

参考
> https://www.cnblogs.com/songshu120/p/7966314.html
