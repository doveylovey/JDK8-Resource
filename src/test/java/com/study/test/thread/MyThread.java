package com.study.test.thread;

/**
 * 作用描述：继承 Thread 类创建线程
 * 1、定义 Thread 类的子类，并重写该类的 run() 方法，该方法的方法体就代表了线程要完成的任务。因此把 run() 方法称为执行体
 * 2、创建 Thread 子类的实例，即创建了线程对象
 * 3、调用线程对象的 start() 方法来启动该线程
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年09月17日
 */
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
