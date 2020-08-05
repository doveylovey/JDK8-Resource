package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：“synchronized(非 this 对象)同步代码块” 是将非 this 对象本身作为对象监视器，这样就可以得出以下3个结论：
 * 1、当多个线程同时执行 synchronized(非 this 对象){同步代码块} 时呈同步效果。
 * 2、当其他线程执行非 this 对象中 synchronized 同步方法时呈同步效果。
 * 3、当其他线程执行非 this 对象中 synchronized(this){同步代码块} 时呈同步效果。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest10 {
    /**
     * 验证结论一：当多个线程同时执行 synchronized(非 this 对象){同步代码块} 时呈同步效果。
     */
    class VerifyConclusion1 {
        public void testMethod1(MyObject myObject) throws InterruptedException {
            synchronized (myObject) {
                System.out.println("【testMethod1()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("【testMethod1()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }
    }

    class MyObject {
    }

    class VerifyConclusion1ThreadA extends Thread {
        private MyObject myObject;
        private VerifyConclusion1 verifyConclusion1;

        public VerifyConclusion1ThreadA(MyObject myObject, VerifyConclusion1 verifyConclusion1) {
            super();
            this.myObject = myObject;
            this.verifyConclusion1 = verifyConclusion1;
        }

        @Override
        public void run() {
            super.run();
            try {
                verifyConclusion1.testMethod1(myObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class VerifyConclusion1ThreadB extends Thread {
        private MyObject myObject;
        private VerifyConclusion1 verifyConclusion1;

        public VerifyConclusion1ThreadB(MyObject myObject, VerifyConclusion1 verifyConclusion1) {
            super();
            this.myObject = myObject;
            this.verifyConclusion1 = verifyConclusion1;
        }

        @Override
        public void run() {
            super.run();
            try {
                verifyConclusion1.testMethod1(myObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testVerifyConclusion101() throws IOException {
        MyObject myObject = new MyObject();
        VerifyConclusion1 verifyConclusion1 = new VerifyConclusion1();
        VerifyConclusion1ThreadA threadA = new VerifyConclusion1ThreadA(myObject, verifyConclusion1);
        threadA.setName("A");
        threadA.start();
        VerifyConclusion1ThreadB threadB = new VerifyConclusion1ThreadB(myObject, verifyConclusion1);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    @Test
    public void testVerifyConclusion102() throws IOException {
        MyObject myObject1 = new MyObject();
        MyObject myObject2 = new MyObject();
        VerifyConclusion1 verifyConclusion1 = new VerifyConclusion1();
        VerifyConclusion1ThreadA threadA = new VerifyConclusion1ThreadA(myObject1, verifyConclusion1);
        threadA.setName("A");
        threadA.start();
        VerifyConclusion1ThreadB threadB = new VerifyConclusion1ThreadB(myObject2, verifyConclusion1);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
