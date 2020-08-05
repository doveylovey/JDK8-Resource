package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：同步不具有继承性。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest08 {
    class SynchronizedNotInheritFather {
        public synchronized void methodFather() throws InterruptedException {
            System.out.println("【methodFather()方法】before sleep。threadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("【methodFather()方法】after sleep。threadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
        }
    }

    class SynchronizedNotInheritSon extends SynchronizedNotInheritFather {
        /**
         * 同步不可以继承。注意子类方法有无 synchronized 关键字时的输出结果。
         *
         * @throws InterruptedException
         */
        public /*synchronized*/ void methodSon() throws InterruptedException {
            System.out.println("【methodSon()方法】before sleep。threadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
            Thread.sleep(5000);
            System.out.println("【methodSon()方法】after sleep。threadName=" + Thread.currentThread().getName() + "，time=" + System.currentTimeMillis());
            super.methodFather();
        }
    }

    class SynchronizedNotInheritThreadA extends Thread {
        private SynchronizedNotInheritSon synchronizedNotInheritSon;

        public SynchronizedNotInheritThreadA(SynchronizedNotInheritSon synchronizedNotInheritSon) {
            this.synchronizedNotInheritSon = synchronizedNotInheritSon;
        }

        @Override
        public void run() {
            try {
                synchronizedNotInheritSon.methodSon();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedNotInheritThreadB extends Thread {
        private SynchronizedNotInheritSon synchronizedNotInheritSon;

        public SynchronizedNotInheritThreadB(SynchronizedNotInheritSon synchronizedNotInheritSon) {
            this.synchronizedNotInheritSon = synchronizedNotInheritSon;
        }

        @Override
        public void run() {
            try {
                synchronizedNotInheritSon.methodSon();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注意 SynchronizedNotInheritSon#methodSon() 方法有无 synchronized 关键字时的输出结果。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedNotInherit01() throws IOException, InterruptedException {
        SynchronizedNotInheritSon synchronizedNotInheritSon = new SynchronizedNotInheritSon();
        SynchronizedNotInheritThreadA threadA = new SynchronizedNotInheritThreadA(synchronizedNotInheritSon);
        threadA.setName("A");
        threadA.start();
        SynchronizedNotInheritThreadB threadB = new SynchronizedNotInheritThreadB(synchronizedNotInheritSon);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
