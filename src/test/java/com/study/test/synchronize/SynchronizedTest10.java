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
        public void testMethod1(MyObject1 myObject) throws InterruptedException {
            synchronized (myObject) {
                System.out.println("【testMethod1()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("【testMethod1()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }
    }

    class MyObject1 {
    }

    class VerifyConclusion1ThreadA extends Thread {
        private MyObject1 myObject;
        private VerifyConclusion1 verifyConclusion;

        public VerifyConclusion1ThreadA(MyObject1 myObject, VerifyConclusion1 verifyConclusion) {
            super();
            this.myObject = myObject;
            this.verifyConclusion = verifyConclusion;
        }

        @Override
        public void run() {
            super.run();
            try {
                verifyConclusion.testMethod1(myObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class VerifyConclusion1ThreadB extends Thread {
        private MyObject1 myObject;
        private VerifyConclusion1 verifyConclusion;

        public VerifyConclusion1ThreadB(MyObject1 myObject, VerifyConclusion1 verifyConclusion) {
            super();
            this.myObject = myObject;
            this.verifyConclusion = verifyConclusion;
        }

        @Override
        public void run() {
            super.run();
            try {
                verifyConclusion.testMethod1(myObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 该示例结果是同步的，原因是：多个线程使用的是同一个对象监视器。
     * 若多个线程使用不同的对象监视器会是什么效果呢？请看 {@link SynchronizedTest10#testVerifyConclusion102()}
     *
     * @throws IOException
     */
    @Test
    public void testVerifyConclusion101() throws IOException {
        MyObject1 myObject = new MyObject1();
        VerifyConclusion1 verifyConclusion1 = new VerifyConclusion1();
        VerifyConclusion1ThreadA threadA = new VerifyConclusion1ThreadA(myObject, verifyConclusion1);
        threadA.setName("A");
        threadA.start();
        VerifyConclusion1ThreadB threadB = new VerifyConclusion1ThreadB(myObject, verifyConclusion1);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 该示例结果是异步的，原因是：多个线程使用的不同的对象监视器。
     * 若多个线程使用同一个对象监视器会是什么效果呢？请看 {@link SynchronizedTest10#testVerifyConclusion101()}
     *
     * @throws IOException
     */
    @Test
    public void testVerifyConclusion102() throws IOException {
        MyObject1 myObject1 = new MyObject1();
        MyObject1 myObject2 = new MyObject1();
        VerifyConclusion1 verifyConclusion1 = new VerifyConclusion1();
        VerifyConclusion1ThreadA threadA = new VerifyConclusion1ThreadA(myObject1, verifyConclusion1);
        threadA.setName("A");
        threadA.start();
        VerifyConclusion1ThreadB threadB = new VerifyConclusion1ThreadB(myObject2, verifyConclusion1);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 验证结论二：当其他线程执行非 this 对象中 synchronized 同步方法时呈同步效果。
     */
    class VerifyConclusion2 {
        public void testMethod2(MyObject2 myObject) throws InterruptedException {
            synchronized (myObject) {
                System.out.println("【testMethod2()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("【testMethod2()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }
    }

    class MyObject2 {
        public synchronized void methodInMyObject2() {
            System.out.println("【methodInMyObject2()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            System.out.println("【methodInMyObject2()方法】==============================");
            System.out.println("【methodInMyObject2()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }
    }

    class VerifyConclusion2ThreadA extends Thread {
        private MyObject2 myObject;
        private VerifyConclusion2 verifyConclusion;

        public VerifyConclusion2ThreadA(MyObject2 myObject, VerifyConclusion2 verifyConclusion) {
            super();
            this.myObject = myObject;
            this.verifyConclusion = verifyConclusion;
        }

        @Override
        public void run() {
            super.run();
            try {
                verifyConclusion.testMethod2(myObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class VerifyConclusion2ThreadB extends Thread {
        private MyObject2 myObject;

        public VerifyConclusion2ThreadB(MyObject2 myObject) {
            super();
            this.myObject = myObject;
        }

        @Override
        public void run() {
            super.run();
            myObject.methodInMyObject2();
        }
    }

    @Test
    public void testVerifyConclusion201() throws IOException {
        MyObject2 myObject2 = new MyObject2();
        VerifyConclusion2 verifyConclusion2 = new VerifyConclusion2();
        VerifyConclusion2ThreadA threadA = new VerifyConclusion2ThreadA(myObject2, verifyConclusion2);
        threadA.setName("A");
        threadA.start();
        VerifyConclusion2ThreadB threadB = new VerifyConclusion2ThreadB(myObject2);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 验证结论三：当其他线程执行非 this 对象中 synchronized(this){同步代码块} 时呈同步效果。
     */
    class VerifyConclusion3 {
        public void testMethod3(MyObject3 myObject) throws InterruptedException {
            synchronized (myObject) {
                System.out.println("【testMethod3()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("【testMethod3()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }
    }

    class MyObject3 {
        public void methodInMyObject3() {
            synchronized (this) {
                System.out.println("【methodInMyObject3()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                System.out.println("【methodInMyObject3()方法】==============================");
                System.out.println("【methodInMyObject3()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }
    }

    class VerifyConclusion3ThreadA extends Thread {
        private MyObject3 myObject;
        private VerifyConclusion3 verifyConclusion;

        public VerifyConclusion3ThreadA(MyObject3 myObject, VerifyConclusion3 verifyConclusion) {
            super();
            this.myObject = myObject;
            this.verifyConclusion = verifyConclusion;
        }

        @Override
        public void run() {
            super.run();
            try {
                verifyConclusion.testMethod3(myObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class VerifyConclusion3ThreadB extends Thread {
        private MyObject3 myObject;

        public VerifyConclusion3ThreadB(MyObject3 myObject) {
            super();
            this.myObject = myObject;
        }

        @Override
        public void run() {
            super.run();
            myObject.methodInMyObject3();
        }
    }

    @Test
    public void testVerifyConclusion301() throws IOException {
        MyObject3 myObject3 = new MyObject3();
        VerifyConclusion3 verifyConclusion3 = new VerifyConclusion3();
        VerifyConclusion3ThreadA threadA = new VerifyConclusion3ThreadA(myObject3, verifyConclusion3);
        threadA.setName("A");
        threadA.start();
        VerifyConclusion3ThreadB threadB = new VerifyConclusion3ThreadB(myObject3);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
