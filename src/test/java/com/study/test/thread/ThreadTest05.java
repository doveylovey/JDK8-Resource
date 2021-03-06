package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 作用描述：isAlive() 方法用于判断当前线程是否处于活动状态
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月03日
 */
public class ThreadTest05 {
    class MyIsAliveThread01 extends Thread {
        /**
         * isAlive() 方法用于判断当前线程是否处于活动状态。什么是活动状态呢？
         * 活动状态就是线程已经启动但尚未终止，线程处于正在运行或准备开始运行的状态，就认为线程是存活的。
         */
        @Override
        public void run() {
            System.out.println("【run()方法】this.isAlive()=" + this.isAlive());
        }
    }

    @Test
    public void testMyIsAliveThread01() throws InterruptedException, IOException {
        MyIsAliveThread01 myThread01 = new MyIsAliveThread01();
        System.out.println("begin => myThread01.isAlive()=" + myThread01.isAlive());
        myThread01.start();
        // end 这行代码的结果是不确定的：输出 true 是因为 myThread01 线程还未执行完毕；输出 false 是因为 myThread01 在 3s 之内已经完成。
        //TimeUnit.SECONDS.sleep(3);
        System.out.println("end => myThread01.isAlive()=" + myThread01.isAlive());
        System.in.read();
    }

    class MyIsAliveThread02 extends Thread {
        public MyIsAliveThread02() {
            System.out.println("【构造方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【构造方法】Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());
            System.out.println("【构造方法】this.getName()=" + this.getName());
            System.out.println("【构造方法】this.isAlive()=" + this.isAlive());
        }

        @Override
        public void run() {
            System.out.println("【run()方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【run()方法】Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());
            System.out.println("【run()方法】this.getName()=" + this.getName());
            System.out.println("【run()方法】this.isAlive()=" + this.isAlive());
        }
    }

    @Test
    public void testMyIsAliveThread02() throws IOException {
        // 在使用 isAlive() 方法时，如果将自定义线程对象当做参数传递给 Thread 的构造方法然后进行 start() 启动，运行结果与 testMyIsAliveThread01() 是不一样的。
        // 造成这个差异的原因是：Thread.currentThread() 和 this 的差异。
        MyIsAliveThread02 myThread02 = new MyIsAliveThread02();
        Thread thread = new Thread(myThread02);
        System.out.println("begin => thread.isAlive=" + thread.isAlive());
        thread.setName("A");
        thread.start();
        System.out.println("end => thread.isAlive=" + thread.isAlive());
        System.in.read();
    }
}
