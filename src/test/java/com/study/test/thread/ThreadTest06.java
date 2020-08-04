package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：sleep() 方法的作用是让当前“正在执行的”线程休眠(暂停执行)指定毫秒数。这个“正在执行的”线程是指 this.currentThread() 返回的线程。
 * getId() 方法的作用是获取线程的唯一标识。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月04日
 */
public class ThreadTest06 {
    class MySleepThread01 extends Thread {
        @Override
        public void run() {
            System.out.println("【run()方法】begin，this.currentThread().getName()=" + this.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("【run()方法】end，this.currentThread().getName()=" + this.currentThread().getName());
        }
    }

    @Test
    public void testMySleepThread01() throws IOException {
        MySleepThread01 mySleepThread01 = new MySleepThread01();
        System.out.println("【测试方法】begin=" + System.currentTimeMillis());
        // 直接调用 run() 方法进行测试
        //mySleepThread01.run();

        // 使用 run() 方法启动线程。由于 main 线程与 MySleepThread01 线程是异步执行的，所以测试方法会先有输出，然后 run() 方法才输出
        mySleepThread01.start();
        System.out.println("【测试方法】end=" + System.currentTimeMillis());
        System.in.read();
    }

    class MyGetIdThread01 extends Thread {
        @Override
        public void run() {
            Thread currentThread = Thread.currentThread();
            System.out.println("【run()方法】ThreadName=" + currentThread.getName() + "，ThreadId=" + currentThread.getId());
        }
    }

    @Test
    public void testMyGetIdThread01() throws IOException {
        MyGetIdThread01 myGetIdThread01 = new MyGetIdThread01();
        Thread currentThread = Thread.currentThread();
        System.out.println("【测试方法】ThreadName=" + currentThread.getName() + "，ThreadId=" + currentThread.getId());
        myGetIdThread01.start();
        System.in.read();
    }
}
