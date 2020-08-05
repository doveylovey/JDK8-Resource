package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：synchronized 锁重入。
 * synchronized 关键字拥有锁重入的功能，也就是在使用 synchronized 关键字时，当一个线程得到一个对象锁后，再次请求此对象锁时是可以再次得到该对象锁的。
 * 这也证明在一个 synchronized 块/方法内部调用本类的其他 synchronized 块/方法时，是永远可以得到锁的。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest06 {
    /**
     * 可重入锁的概念：自己可以再次获取自己的内部锁。
     * 比如，有个线程已经获得了某个对象的锁，且这个对象锁还未被释放，当其还想要再次获取该对象的锁时仍然可以获取到，如果不可重入的话就会造成死锁。
     */
    class SynchronizedReentry {
        public synchronized void methodA() {
            System.out.println("threadName=" + Thread.currentThread().getName() + "，调用了 methodA() 方法");
            methodB();
        }

        public synchronized void methodB() {
            System.out.println("threadName=" + Thread.currentThread().getName() + "，调用了 methodB() 方法");
            methodC();
        }

        public synchronized void methodC() {
            System.out.println("threadName=" + Thread.currentThread().getName() + "，调用了 methodC() 方法");
        }
    }

    class SynchronizedReentryThread extends Thread {
        @Override
        public void run() {
            new SynchronizedReentry().methodA();
        }
    }

    /**
     * 可重入锁的概念：自己可以再次获取自己的内部锁。
     * 比如，有个线程已经获得了某个对象的锁，且这个对象锁还未被释放，当其还想要再次获取该对象的锁时仍然可以获取到，如果不可重入的话就会造成死锁。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedReentry01() throws IOException, InterruptedException {
        new SynchronizedReentryThread().start();
        System.in.read();
    }

    /**
     * 可重入锁也支持在父子类继承的环境中
     */
    class SynchronizedReentryFather {
        public int i = 10;

        public synchronized void methodFather() throws InterruptedException {
            i--;
            System.out.println("【methodFather()方法】i=" + i);
            Thread.sleep(100);
        }
    }

    /**
     * 可重入锁也支持在父子类继承的环境中
     */
    class SynchronizedReentrySon extends SynchronizedReentryFather {
        public synchronized void methodSon() throws InterruptedException {
            while (i > 0) {
                i--;
                System.out.println("【methodSon()方法】i=" + i);
                Thread.sleep(100);
                this.methodFather();
            }
        }
    }

    class SynchronizedReentryInheritThread extends Thread {
        @Override
        public void run() {
            try {
                new SynchronizedReentrySon().methodSon();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 该示例说明：当存在父子继承关系时，子类完全可以通过可重入锁调用父类的同步方法。
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedReentryInherit01() throws IOException {
        new SynchronizedReentryInheritThread().start();
        System.in.read();
    }
}
