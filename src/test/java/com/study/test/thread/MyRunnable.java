package com.study.test.thread;

/**
 * 作用描述：实现 Runnable 接口创建线程
 * 1、定义 Runnable 接口的实现类，并重写该接口的 run() 方法，该方法的方法体同样是该线程的线程执行体
 * 2、创建 Runnable 实现类的实例，并以此实例作为 Thread 的 target 来创建 Thread 对象，该 Thread 对象才是真正的线程对象
 * 3、调用线程对象的 start() 方法来启动该线程
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年09月17日
 */
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