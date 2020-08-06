package com.study.test.volatiletest;

import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：synchronized 同步代码块有 volatile 同步的功能。
 * synchronized 关键字可以使多个线程访问同一资源具有同步性，而且它还具有将线程工作内存中的私有变量与公共内存中的变量进行同步的功能。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class VolatileTest03 {
    /**
     * synchronized 关键字可以使多个线程访问同一资源具有同步性，而且它还具有将线程工作内存中的私有变量与公共内存中的变量进行同步的功能。
     */
    class SynchronizedUpdateNewValue {
        private boolean isRunning = true;

        public void runMethod() {
            String anyString = new String();
            while (isRunning) {
                // 将 JVM 以 server 模式运行，若无下面的同步代码块，将会出现死锁。原因是：各线程间的数据值没有可见性造成的，而 synchronized 可以具有可见性。
                // synchronized 可以保证在同一时刻只有一个线程可以执行某个方法或代码块。它包含两个特征：互斥性、可见性。
                // 同步 synchronized 不仅可以解决一个线程看到对象处于不一致的状态，还可以保证进入同步方法或代码块的每个线程都看到由同一个锁保护之前所有的修改效果。
                /*synchronized (anyString) {
                }*/
            }
            System.out.println("线程 " + Thread.currentThread().getName() + " 停下来了！");
        }

        public void stopMethod() {
            isRunning = false;
        }
    }

    class SynchronizedUpdateNewValueThreadA extends Thread {
        private SynchronizedUpdateNewValue synchronizedUpdateNewValue;

        public SynchronizedUpdateNewValueThreadA(SynchronizedUpdateNewValue synchronizedUpdateNewValue) {
            super();
            this.synchronizedUpdateNewValue = synchronizedUpdateNewValue;
        }

        @Override
        public void run() {
            super.run();
            synchronizedUpdateNewValue.runMethod();
        }
    }

    class SynchronizedUpdateNewValueThreadB extends Thread {
        private SynchronizedUpdateNewValue synchronizedUpdateNewValue;

        public SynchronizedUpdateNewValueThreadB(SynchronizedUpdateNewValue synchronizedUpdateNewValue) {
            super();
            this.synchronizedUpdateNewValue = synchronizedUpdateNewValue;
        }

        @Override
        public void run() {
            super.run();
            synchronizedUpdateNewValue.stopMethod();
        }
    }

    @Test
    public void testSynchronizedUpdateNewValue01() throws IOException, InterruptedException {
        SynchronizedUpdateNewValue synchronizedUpdateNewValue = new SynchronizedUpdateNewValue();
        new SynchronizedUpdateNewValueThreadA(synchronizedUpdateNewValue).start();
        Thread.sleep(1000);
        new SynchronizedUpdateNewValueThreadB(synchronizedUpdateNewValue).start();
        System.out.println("线程 " + Thread.currentThread().getName() + " 已经发起停止命令了！");
        System.in.read();
    }
}
