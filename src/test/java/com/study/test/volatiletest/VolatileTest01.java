package com.study.test.volatiletest;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：关键字 volatile 的主要作用是使变量在多个线程间可见。
 * <p>
 * 使用 volatile 关键字增加了实例变量在多个线程之间的可见性，但其致命缺点是不支持原子性。下面对比下 volatile 和 synchronized 关键字：
 * 1、volatile 是线程同步的轻量级实现，因此 volatile 性能要优于 synchronized。且 volatile 只能用于修饰变量，而 synchronized 可以修饰方法和代码块。
 * 随着 JDK 新版本的发布，synchronized 在执行效率上也有了很大的提升，在实际开发中使用 synchronized 的概率还是比较大的。
 * 2、多线程访问 volatile 不会发生阻塞，而 synchronized 会出现阻塞。
 * 3、volatile 能保证数据的可见性，但不能保证原子性；而 synchronized 可以保证原子性，也可以间接保证可见性，因为它会将公共内存和私有内存中的数据做同步。
 * 4、volatile 解决的是变量在多个线程之间的可见性，而 synchronized 保证的是多个线程之间访问资源的同步性。
 * <p>
 * 注意：线程安全包含原子性和可见性两个方面，Java 中的同步机制都是围绕这两个方面来确保线程安全的。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class VolatileTest01 {
    /**
     * 关键字 volatile 与死循环。
     * 如果不是在多继承的情况下，使用继承 Thread 类和实现 Runnable 接口在取得程序运行结果上并没有太大的区别。
     * 而一旦出现 “多继承” 的情况，则采用实现 Runnable 接口的方式来处理多线程的问题就是很有必要的了。
     * <p>
     * 本示例将用实现 Runnable 接口的方式来继续理解多线程技术的使用，并且使用 volatile 关键字来实验在并发情况下的一些特性。
     * 此示例同样适用于继承 Thread 类。
     */
    class VolatileAndDeadLoop {
        private boolean flag = true;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void method() throws InterruptedException {
            while (flag) {
                System.out.println("【method()方法】threadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
                Thread.sleep(1000);
            }
        }
    }

    /**
     * 运行后会发现：{@link VolatileAndDeadLoop#method()} 根本不会停止打印。
     * 主要原因是：main 线程一直在处理 while() 循环，导致程序不能继续执行后面的代码。解决办法就是使用多线程技术。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedInfiniteWait01() throws IOException, InterruptedException {
        VolatileAndDeadLoop volatileAndDeadLoop = new VolatileAndDeadLoop();
        volatileAndDeadLoop.method();
        System.out.println("我要停止它！stopThreadName=" + Thread.currentThread().getName());
        volatileAndDeadLoop.setFlag(false);
        System.in.read();
    }

    /**
     * 解决同步死循环
     */
    class SolveSynchronizedDeadLoop implements Runnable {
        private boolean flag = true;

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public void method() throws InterruptedException {
            while (flag) {
                System.out.println("【method()方法】threadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
                Thread.sleep(1000);
            }
        }

        @Override
        public void run() {
            try {
                method();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 运行后会发现：{@link SolveSynchronizedDeadLoop#method()} 会停止打印。
     * 但当该示例代码运行在 -server 服务器模式中 64bit 的 JVM 上时，会出现死循环，解决办法就是使用 volatile 关键字。
     * volatile 关键字的作用就是强制从公共堆栈中取得变量的值，而不是从线程私有的数据栈中取得变量的值。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSolveSynchronizedDeadLoop01() throws IOException, InterruptedException {
        SolveSynchronizedDeadLoop solveSynchronizedDeadLoop = new SolveSynchronizedDeadLoop();
        new Thread(solveSynchronizedDeadLoop).start();
        System.out.println("我要停止它！stopThreadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
        solveSynchronizedDeadLoop.setFlag(false);
        System.in.read();
    }

    /**
     * 解决异步死循环
     */
    class SolveAsyncDeadLoop extends Thread {
        private /*volatile*/ boolean isRunning = true;

        public boolean isRunning() {
            return isRunning;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        @Override
        public void run() {
            System.out.println("【run()方法】threadName=" + Thread.currentThread().getName());
            while (isRunning) {
            }
            System.out.println("【run()方法】threadName=" + Thread.currentThread().getName() + " 被停止了！");
        }
    }

    /**
     * 在 win7 + 64位 JDK + eclipse 环境下，会有3行输出。
     * 但让该代码运行在 server 模式的 JVM 中，运行后会出现死锁，导致 System.out.println("【run()方法】threadName=" + Thread.currentThread().getName() + " 被停止了！");永远执行不到。
     * <p>
     * 是什么原因导致在 server 模式的 JVM 中运行就会出现死循环呢？
     * 在启动 main 线程时，变量 private boolean isRunning = true; 存在于公共堆栈和线程私有堆栈中。
     * 而 JVM 设置成 server 模式是为了提高线程的运行效率，线程一直在私有堆栈中取得 isRunning = true，
     * 虽然代码 solveAsyncDeadLoop.setRunning(false); 被执行了，但更新的是公共堆栈中 isRunning 的值，线程私有堆栈中并未改变，
     * 所以出现了死循环，解决办法就是用 volatile 修饰 isRunning 变量，即 private volatile boolean isRunning = true;。
     * <p>
     * 这个问题其实就是公共堆栈和线程私有堆栈中变量的值不同步造成的，解决这类问题就要使用 volatile 关键字了，其作用就是当线程访问 isRunning 变量时强制从公共堆栈中读取值。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSolveAsyncDeadLoop01() throws IOException, InterruptedException {
        SolveAsyncDeadLoop solveAsyncDeadLoop = new SolveAsyncDeadLoop();
        solveAsyncDeadLoop.setName("test");
        solveAsyncDeadLoop.start();
        Thread.sleep(1000);
        solveAsyncDeadLoop.setRunning(false);
        System.out.println("threadName=" + Thread.currentThread().getName() + " 已经赋值为 false");
        System.in.read();
    }
}
