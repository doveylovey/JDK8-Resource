package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：留意 i-- 与 System.out.println() 的异常
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年07月27日
 */
public class ThreadTest04 {
    class MyThread01 extends Thread {
        private int i = 5;

        /**
         * 注意 run() 方法中有无 synchronized 关键字的区别。
         */
        @Override
        public /*synchronized*/ void run() {
            // 注意：i-- 由之前示例中的单独一行运行改为在 System.out.println() 方法中直接打印
            System.out.println("i = " + (i--) + ", 当前线程：" + Thread.currentThread().getName());
        }
    }

    @Test
    public void testMyThread01() throws IOException {
        // 该程序运行后还是有可能出现非线程安全问题
        // 本测试用例的目的：虽然 println() 方法在内部是同步的，但 i-- 操作是在进入 println() 之前发生的，所以有发生非线程安全问题的可能
        // 所以，为了防止非线程安全问题，还是需要使用同步方法
        MyThread01 myThread01 = new MyThread01();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        System.in.read();
    }

    class MyThread02 extends Thread {
        public MyThread02() {
            System.out.println("【构造方法】当前线程：" + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            System.out.println("【run() 方法】当前线程：" + Thread.currentThread().getName());
        }
    }

    @Test
    public void testMyThread02() {
        MyThread02 myThread02 = new MyThread02();
        // MyThread02 类的构造方法是被 main 线程调用的，而 run() 方法是被 Thread-0 线程自动调用的
        myThread02.start();
        // MyThread02 类的构造方法、run() 方法都是被 main 线程调用的：直接调用 run() 方法达不到多线程的目的
        //myThread02.run();
    }

    class MyCurrentThread extends Thread {
        public MyCurrentThread() {
            System.out.println("【构造方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【构造方法】this.currentThread().getName()=" + this.currentThread().getName());
            System.out.println("【构造方法】this.getName()=" + this.getName());
        }

        @Override
        public void run() {
            System.out.println("【run() 方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【run() 方法】this.currentThread().getName()=" + this.currentThread().getName());
            System.out.println("【run() 方法】this.getName()=" + this.getName());
        }
    }

    @Test
    public void testCountOperateThread01() {
        // 测试一
        // 该例涉及2个类：ThreadTest04 和 MyCurrentThread。
        // 首先，由 main 线程创建 MyCurrentThread 类的对象，在类初始化时会调其构造方法，打印的当前线程名字就是 main。
        // 注意 this.getName() 方法：MyCurrentThread 类本身没有这个方法，而是其父类 Thread 的，由于该方法目前没有被重写，所以调用它会给出默认值即 Thread-0。
        // 其次，线程直接调用 start() 方法，此时该线程已经被命名成 A 了，再调用 this.getName() 方法得到的就是重写后的 A 了。
        MyCurrentThread myCurrentThread1 = new MyCurrentThread();
        myCurrentThread1.setName("A");
        myCurrentThread1.start();
    }

    @Test
    public void testCountOperateThread02() {
        // 测试二
        MyCurrentThread myCurrentThread2 = new MyCurrentThread();
        Thread thread2 = new Thread(myCurrentThread2);
        thread2.setName("A");
        thread2.start();
    }

    @Test
    public void testCountOperateThread03() {
        // 测试三
        MyCurrentThread myCurrentThread3 = new MyCurrentThread();
        Thread thread3 = new Thread(myCurrentThread3);
        myCurrentThread3.setName("A");
        thread3.start();
    }
}
