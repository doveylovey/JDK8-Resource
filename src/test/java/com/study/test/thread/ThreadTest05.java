package com.study.test.thread;

import org.junit.Test;

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
    class MyThread01 extends Thread {
        /**
         * isAlive() 方法用于判断当前线程是否处于活动状态。什么是活动状态呢？
         * 活动状态就是线程已经启动但尚未终止，线程处于正在运行或准备开始运行的状态，就认为线程是存活的。
         */
        @Override
        public void run() {
            System.out.println("run => " + this.isAlive());
        }
    }

    @Test
    public void testMyThread01() throws InterruptedException {
        MyThread01 myThread01 = new MyThread01();
        System.out.println("begin => " + myThread01.isAlive());
        myThread01.start();
        // end 这行代码的结果是不确定的：输出 true 是因为 myThread01 线程还未执行完毕；输出 false 是因为 myThread01 在 3s 之内已经完成。
        TimeUnit.SECONDS.sleep(3);
        System.out.println("end => " + myThread01.isAlive());
    }
}
