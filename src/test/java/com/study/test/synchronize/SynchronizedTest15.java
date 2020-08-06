package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：Java 内部类与同步
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class SynchronizedTest15 {
    /**
     * 测试在内部类中有两个同步方法，但使用的是不同的锁，打印的结果也是异步的。
     * 由于持有不同的对象监视器，所以打印结果是乱序的。
     *
     * @throws IOException
     */
    @Test
    public void testInnerClassAndSynchronized101() throws IOException {
        final InnerClassAndSynchronized1.InnerClass11 innerClass11 = new InnerClassAndSynchronized1.InnerClass11();
        Thread thread1 = new Thread(() -> {
            try {
                innerClass11.method1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "A");
        Thread thread2 = new Thread(() -> {
            try {
                innerClass11.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "B");
        thread1.start();
        thread2.start();
        System.in.read();
    }

    /**
     * 测试同步代码块 synchronized (class22) 对 class22 上锁后，其他线程只能以同步的方式调用 class22 中的静态同步方法
     *
     * @throws IOException
     */
    @Test
    public void testInnerClassAndSynchronized201() throws IOException {
        final InnerClassAndSynchronized2.InnerClass21 innerClass21 = new InnerClassAndSynchronized2.InnerClass21();
        final InnerClassAndSynchronized2.InnerClass22 innerClass22 = new InnerClassAndSynchronized2.InnerClass22();
        Thread thread1 = new Thread(() -> {
            try {
                innerClass21.method1(innerClass22);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T1");
        Thread thread2 = new Thread(() -> {
            try {
                innerClass21.method2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T2");
        Thread thread3 = new Thread(() -> {
            try {
                innerClass22.method1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T3");
        thread1.start();
        thread2.start();
        thread3.start();
        System.in.read();
    }
}

/**
 * 测试在内部类中有两个同步方法，但使用的是不同的锁，打印的结果也是异步的。
 */
class InnerClassAndSynchronized1 {
    static class InnerClass11 {
        public void method1() throws InterruptedException {
            synchronized ("其他的锁") {
                for (int i = 1; i <= 10; i++) {
                    System.out.println("【InnerClass11 method1()方法】threadName：" + Thread.currentThread().getName() + "，i=" + i);
                    Thread.sleep(100);
                }
            }
        }

        public synchronized void method2() throws InterruptedException {
            for (int j = 11; j <= 20; j++) {
                System.out.println("【InnerClass11 method2()方法】threadName：" + Thread.currentThread().getName() + "，j=" + j);
                Thread.sleep(100);
            }
        }
    }
}

/**
 * 测试同步代码块 synchronized (class22) 对 class22 上锁后，其他线程只能以同步的方式调用 class22 中的静态同步方法
 */
class InnerClassAndSynchronized2 {
    static class InnerClass21 {
        public void method1(InnerClass22 class22) throws InterruptedException {
            String threadName = Thread.currentThread().getName();
            synchronized (class22) {
                System.out.println("【InnerClass21 method1()方法】threadName：" + threadName + "，begin time：" + System.currentTimeMillis());
                for (int i = 0; i < 10; i++) {
                    System.out.println("i=" + i);
                    Thread.sleep(100);
                }
                System.out.println("【InnerClass21 method1()方法】threadName：" + threadName + "，end   time：" + System.currentTimeMillis());
            }
        }

        public synchronized void method2() throws InterruptedException {
            String threadName = Thread.currentThread().getName();
            System.out.println("【InnerClass21 method2()方法】threadName：" + threadName + "，begin time：" + System.currentTimeMillis());
            for (int j = 0; j < 10; j++) {
                System.out.println("j=" + j);
                Thread.sleep(100);
            }
            System.out.println("【InnerClass21 method2()方法】threadName：" + threadName + "，end   time：" + System.currentTimeMillis());
        }
    }

    static class InnerClass22 {
        public synchronized void method1() throws InterruptedException {
            String threadName = Thread.currentThread().getName();
            System.out.println("【InnerClass22 method1()方法】threadName：" + threadName + "，begin time：" + System.currentTimeMillis());
            for (int k = 0; k < 10; k++) {
                System.out.println("k=" + k);
                Thread.sleep(100);
            }
            System.out.println("【InnerClass22 method1()方法】threadName：" + threadName + "，end   time：" + System.currentTimeMillis());
        }
    }
}