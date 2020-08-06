package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：静态 synchronized 同步方法与 synchronized(class) 同步代码块。
 * synchronized 关键字还可以用在 static 静态方法上，如果这样写，则是对当前的 *.java 文件对应的 Class 类进行持锁。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class SynchronizedTest11 {
    /**
     * 使用 synchronized 关键字修饰 static 静态方法
     */
    static class SynchronizedUsedInStaticMethod {
        public synchronized static void methodA() throws InterruptedException {
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }

        public synchronized static void methodB() {
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }
    }

    class SynchronizedUsedInStaticMethodThreadA extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                SynchronizedUsedInStaticMethod.methodA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedUsedInStaticMethodThreadB extends Thread {
        @Override
        public void run() {
            super.run();
            SynchronizedUsedInStaticMethod.methodB();
        }
    }

    /**
     * 从输出结果来看，并没有什么特别之处，都是同步的效果，和将 synchronized 关键字加到非 static 静态方法上的使用效果一样。
     * 其实本质是不同的：将 synchronized 关键字加到 static 静态方法上是给 Class 类加锁，而将 synchronized 关键字加到非 static 静态方法上是给对象加锁。
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedUsedInStaticMethod01() throws IOException {
        SynchronizedUsedInStaticMethodThreadA threadA = new SynchronizedUsedInStaticMethodThreadA();
        threadA.setName("A");
        threadA.start();
        SynchronizedUsedInStaticMethodThreadB threadB = new SynchronizedUsedInStaticMethodThreadB();
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 验证 synchronized 在 static 静态方法上和在非 static 静态方法上是不同的锁
     */
    static class SynchronizedStaticMethodTwoLock {
        public synchronized static void methodA() throws InterruptedException {
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }

        public synchronized static void methodB() {
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }

        public synchronized void methodC() {
            System.out.println("【methodC()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            System.out.println("【methodC()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }
    }

    class SynchronizedStaticMethodTwoLockThreadA extends Thread {
        private SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock;

        public SynchronizedStaticMethodTwoLockThreadA(SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock) {
            super();
            this.synchronizedStaticMethodTwoLock = synchronizedStaticMethodTwoLock;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronizedStaticMethodTwoLock.methodA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedStaticMethodTwoLockThreadB extends Thread {
        private SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock;

        public SynchronizedStaticMethodTwoLockThreadB(SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock) {
            super();
            this.synchronizedStaticMethodTwoLock = synchronizedStaticMethodTwoLock;
        }

        @Override
        public void run() {
            super.run();
            synchronizedStaticMethodTwoLock.methodB();
        }
    }

    class SynchronizedStaticMethodTwoLockThreadC extends Thread {
        private SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock;

        public SynchronizedStaticMethodTwoLockThreadC(SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock) {
            super();
            this.synchronizedStaticMethodTwoLock = synchronizedStaticMethodTwoLock;
        }

        @Override
        public void run() {
            super.run();
            synchronizedStaticMethodTwoLock.methodC();
        }
    }

    /**
     * 从输出结果可知 {@link SynchronizedStaticMethodTwoLock#methodC()} 方法是异步运行的。
     * 异步的原因是：一个是 Class 锁，一个是对象锁。而 Class 锁对类的所有对象实例均起作用。
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedStaticMethodTwoLock01() throws IOException {
        SynchronizedStaticMethodTwoLock synchronizedStaticMethodTwoLock = new SynchronizedStaticMethodTwoLock();
        SynchronizedStaticMethodTwoLockThreadA threadA = new SynchronizedStaticMethodTwoLockThreadA(synchronizedStaticMethodTwoLock);
        threadA.setName("A");
        threadA.start();
        SynchronizedStaticMethodTwoLockThreadB threadB = new SynchronizedStaticMethodTwoLockThreadB(synchronizedStaticMethodTwoLock);
        threadB.setName("B");
        threadB.start();
        SynchronizedStaticMethodTwoLockThreadC threadC = new SynchronizedStaticMethodTwoLockThreadC(synchronizedStaticMethodTwoLock);
        threadC.setName("C");
        threadC.start();
        System.in.read();
    }

    /**
     * 比较 synchronized(class) 同步代码块和 synchronized static 方法的效果。对比 {@link SynchronizedClass} 类
     */
    static class SynchronizedStaticMethod {
        public synchronized static void methodA() throws InterruptedException {
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            Thread.sleep(3000);
            System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }

        public synchronized static void methodB() {
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
            System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
        }
    }

    class SynchronizedStaticMethodOneLockThreadA extends Thread {
        private SynchronizedStaticMethod synchronizedStaticMethod;

        public SynchronizedStaticMethodOneLockThreadA(SynchronizedStaticMethod synchronizedStaticMethod) {
            super();
            this.synchronizedStaticMethod = synchronizedStaticMethod;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronizedStaticMethod.methodA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedStaticMethodOneLockThreadB extends Thread {
        private SynchronizedStaticMethod synchronizedStaticMethod;

        public SynchronizedStaticMethodOneLockThreadB(SynchronizedStaticMethod synchronizedStaticMethod) {
            super();
            this.synchronizedStaticMethod = synchronizedStaticMethod;
        }

        @Override
        public void run() {
            super.run();
            synchronizedStaticMethod.methodB();
        }
    }

    /**
     * 通过比较 {@link SynchronizedTest11#testSynchronizedStaticMethod01()} 和 {@link SynchronizedTest11#testSynchronizedClass01()} 的输出结果可知：
     * synchronized(class) 同步代码块的作用和 synchronized static 方法的作用一样。
     * 虽然是不同对象，但静态同步方法还是同步运行的。
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedStaticMethod01() throws IOException {
        SynchronizedStaticMethod synchronizedStaticMethod1 = new SynchronizedStaticMethod();
        SynchronizedStaticMethod synchronizedStaticMethod2 = new SynchronizedStaticMethod();
        SynchronizedStaticMethodOneLockThreadA threadA = new SynchronizedStaticMethodOneLockThreadA(synchronizedStaticMethod1);
        threadA.setName("A");
        threadA.start();
        SynchronizedStaticMethodOneLockThreadB threadB = new SynchronizedStaticMethodOneLockThreadB(synchronizedStaticMethod2);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 比较 synchronized(class) 同步代码块和 synchronized static 方法的效果。对比 {@link SynchronizedStaticMethod} 类
     */
    static class SynchronizedClass {
        public static void methodA() throws InterruptedException {
            synchronized (SynchronizedClass.class) {
                System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                Thread.sleep(3000);
                System.out.println("【methodA()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }

        public static void methodB() {
            synchronized (SynchronizedClass.class) {
                System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，lock   time：" + System.currentTimeMillis());
                System.out.println("【methodB()方法】threadName：" + Thread.currentThread().getName() + "，unlock time：" + System.currentTimeMillis());
            }
        }
    }

    class SynchronizedStaticMethodOneBlockLockThreadA extends Thread {
        private SynchronizedClass synchronizedClass;

        public SynchronizedStaticMethodOneBlockLockThreadA(SynchronizedClass synchronizedClass) {
            super();
            this.synchronizedClass = synchronizedClass;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronizedClass.methodA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedStaticMethodOneBlockLockThreadB extends Thread {
        private SynchronizedClass synchronizedClass;

        public SynchronizedStaticMethodOneBlockLockThreadB(SynchronizedClass synchronizedClass) {
            super();
            this.synchronizedClass = synchronizedClass;
        }

        @Override
        public void run() {
            super.run();
            synchronizedClass.methodB();
        }
    }

    /**
     * 通过比较 {@link SynchronizedTest11#testSynchronizedStaticMethod01()} 和 {@link SynchronizedTest11#testSynchronizedClass01()} 的输出结果可知：
     * synchronized(class) 同步代码块的作用和 synchronized static 方法的作用一样。
     * 虽然是不同对象，但静态同步方法还是同步运行的。
     *
     * @throws IOException
     */
    @Test
    public void testSynchronizedClass01() throws IOException {
        SynchronizedClass synchronizedClass1 = new SynchronizedClass();
        SynchronizedClass synchronizedClass2 = new SynchronizedClass();
        SynchronizedStaticMethodOneBlockLockThreadA threadA = new SynchronizedStaticMethodOneBlockLockThreadA(synchronizedClass1);
        threadA.setName("A");
        threadA.start();
        SynchronizedStaticMethodOneBlockLockThreadB threadB = new SynchronizedStaticMethodOneBlockLockThreadB(synchronizedClass2);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
