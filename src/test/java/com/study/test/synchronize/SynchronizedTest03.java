package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：多个对象多个锁
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest03 {
    class ManyObjectManyLock {
        private int num = 0;

        /**
         * 方法为同步方法，说明其应该被顺序调用
         *
         * @param username
         */
        public synchronized void setUsername(String username) {
            try {
                if ("a".equals(username)) {
                    num = 100;
                    System.out.println("a set over");
                    Thread.sleep(2000);
                } else {
                    num = 200;
                    System.out.println("other set over");
                }
                System.out.println("username=" + username + "，num=" + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class ManyObjectManyLockThreadA extends Thread {
        private ManyObjectManyLock manyObjectManyLock;

        public ManyObjectManyLockThreadA(ManyObjectManyLock manyObjectManyLock) {
            super();
            this.manyObjectManyLock = manyObjectManyLock;
        }

        @Override
        public void run() {
            super.run();
            manyObjectManyLock.setUsername("a");
        }
    }

    class ManyObjectManyLockThreadB extends Thread {
        private ManyObjectManyLock manyObjectManyLock;

        public ManyObjectManyLockThreadB(ManyObjectManyLock manyObjectManyLock) {
            super();
            this.manyObjectManyLock = manyObjectManyLock;
        }

        @Override
        public void run() {
            super.run();
            manyObjectManyLock.setUsername("b");
        }
    }

    /**
     * 本例中两个线程分别访问同一个类的两个不同实例的相同名称的同步方法，效果却是以异步方式运行的。
     * 本示例由于创建了2个业务对象，在系统中产生2个锁，所以运行结果是异步的，打印效果就是先打印 b 再打印 a。
     * <p>
     * 从运行结果来看，虽然 ManyObjectManyLock.setUsername(String username) 方法使用了 synchronized 关键字，但打印顺序却不是同步的，而是交叉的，为什么呢？
     * 关键字 synchronized 取得的锁都是对象锁，而不是把一段代码或方法(函数)当作锁，所以本例中哪个线程先执行带 synchronized 关键字的方法，
     * 哪个线程就持有该方法所属对象的锁 Lock，而其他线程只能等待，前提是多个线程访问的是同一个对象。
     * <p>
     * 如果是多个线程访问多个对象，则 JVM 会创建多个锁。本示例就是创建了2个 ManyObjectManyLock 类对象，所以就会产生2个锁。
     *
     * @throws IOException
     */
    @Test
    public void testManyObjectManyLock01() throws IOException {
        ManyObjectManyLock manyObjectManyLock1 = new ManyObjectManyLock();
        new ManyObjectManyLockThreadA(manyObjectManyLock1).start();
        ManyObjectManyLock manyObjectManyLock2 = new ManyObjectManyLock();
        new ManyObjectManyLockThreadB(manyObjectManyLock2).start();
        System.in.read();
    }
}
