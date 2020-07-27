package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
    public void testLoginThread01() {
        MyThread01 myThread01 = new MyThread01();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
