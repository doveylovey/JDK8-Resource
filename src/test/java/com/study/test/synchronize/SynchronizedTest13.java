package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：synchronized 同步方法无限等待与解决。
 * 同步方法容易造成死循环.
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class SynchronizedTest13 {
    /**
     * 同步方法造成死循环示例
     */
    class SynchronizedInfiniteWait {
        public synchronized void methodA() {
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
            boolean flag = true;
            while (flag) {
            }
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，end  time：" + System.currentTimeMillis());
        }

        public synchronized void methodB() {
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，end  time：" + System.currentTimeMillis());
        }
    }

    class SynchronizedInfiniteWaitThreadA extends Thread {
        private SynchronizedInfiniteWait synchronizedInfiniteWait;

        public SynchronizedInfiniteWaitThreadA(SynchronizedInfiniteWait synchronizedInfiniteWait) {
            super();
            this.synchronizedInfiniteWait = synchronizedInfiniteWait;
        }

        @Override
        public void run() {
            super.run();
            synchronizedInfiniteWait.methodA();
        }
    }

    class SynchronizedInfiniteWaitThreadB extends Thread {
        private SynchronizedInfiniteWait synchronizedInfiniteWait;

        public SynchronizedInfiniteWaitThreadB(SynchronizedInfiniteWait synchronizedInfiniteWait) {
            super();
            this.synchronizedInfiniteWait = synchronizedInfiniteWait;
        }

        @Override
        public void run() {
            super.run();
            synchronizedInfiniteWait.methodB();
        }
    }

    /**
     * 运行结果是死循环：线程 B 永远得不到运行的机会，被锁死了
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedInfiniteWait01() throws IOException {
        SynchronizedInfiniteWait synchronizedInfiniteWait = new SynchronizedInfiniteWait();
        SynchronizedInfiniteWaitThreadA threadA = new SynchronizedInfiniteWaitThreadA(synchronizedInfiniteWait);
        threadA.setName("A");
        threadA.start();
        SynchronizedInfiniteWaitThreadB threadB = new SynchronizedInfiniteWaitThreadB(synchronizedInfiniteWait);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 用同步代码块解决同步方法造成死循环的问题
     */
    class SynchronizedSolveInfiniteWait {
        Object object1 = new Object();
        Object object2 = new Object();

        public void methodA() {
            synchronized (object1) {
                System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                boolean flag = true;
                while (flag) {
                }
                System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，end  time：" + System.currentTimeMillis());
            }
        }

        public void methodB() {
            synchronized (object2) {
                System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，end  time：" + System.currentTimeMillis());
            }
        }
    }

    class SynchronizedSolveInfiniteWaitThreadA extends Thread {
        private SynchronizedSolveInfiniteWait synchronizedSolveInfiniteWait;

        public SynchronizedSolveInfiniteWaitThreadA(SynchronizedSolveInfiniteWait synchronizedSolveInfiniteWait) {
            super();
            this.synchronizedSolveInfiniteWait = synchronizedSolveInfiniteWait;
        }

        @Override
        public void run() {
            super.run();
            synchronizedSolveInfiniteWait.methodA();
        }
    }

    class SynchronizedSolveInfiniteWaitThreadB extends Thread {
        private SynchronizedSolveInfiniteWait synchronizedSolveInfiniteWait;

        public SynchronizedSolveInfiniteWaitThreadB(SynchronizedSolveInfiniteWait synchronizedSolveInfiniteWait) {
            super();
            this.synchronizedSolveInfiniteWait = synchronizedSolveInfiniteWait;
        }

        @Override
        public void run() {
            super.run();
            synchronizedSolveInfiniteWait.methodB();
        }
    }

    /**
     * 运行结果不再出现同步等待的情况
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedSolveInfiniteWaitWait01() throws IOException {
        SynchronizedSolveInfiniteWait synchronizedSolveInfiniteWait = new SynchronizedSolveInfiniteWait();
        SynchronizedSolveInfiniteWaitThreadA threadA = new SynchronizedSolveInfiniteWaitThreadA(synchronizedSolveInfiniteWait);
        threadA.setName("A");
        threadA.start();
        SynchronizedSolveInfiniteWaitThreadB threadB = new SynchronizedSolveInfiniteWaitThreadB(synchronizedSolveInfiniteWait);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
