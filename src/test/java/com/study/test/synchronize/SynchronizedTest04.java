package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：synchronized 方法与锁对象
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest04 {
    class SynchronizedMethodLockObject {
        /**
         * 有无 synchronized 关键字进行同步处理：若无，多个线程可一同进入 commonMethod() 方法；若有，多个线程需排队进入 commonMethod() 方法
         */
        public synchronized void commonMethod() {
            try {
                System.out.println("【commonMethod()方法】begin。threadName=" + Thread.currentThread().getName() + "，beginTime=" + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("【commonMethod()方法】end。endTime=" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public synchronized void methodA() {
            try {
                System.out.println("【methodA()方法】begin。threadName=" + Thread.currentThread().getName() + "，beginTime=" + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("【methodA()方法】end。endTime=" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public synchronized void methodB() {
            try {
                System.out.println("【methodB()方法】begin。threadName=" + Thread.currentThread().getName() + "，beginTime=" + System.currentTimeMillis());
                Thread.sleep(5000);
                System.out.println("【methodB()方法】end。endTime=" + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedMethodLockObjectThreadA extends Thread {
        private SynchronizedMethodLockObject synchronizedMethodLockObject;

        public SynchronizedMethodLockObjectThreadA(SynchronizedMethodLockObject synchronizedMethodLockObject) {
            super();
            this.synchronizedMethodLockObject = synchronizedMethodLockObject;
        }

        @Override
        public void run() {
            super.run();
            synchronizedMethodLockObject.commonMethod();
        }
    }

    class SynchronizedMethodLockObjectThreadB extends Thread {
        private SynchronizedMethodLockObject synchronizedMethodLockObject;

        public SynchronizedMethodLockObjectThreadB(SynchronizedMethodLockObject synchronizedMethodLockObject) {
            super();
            this.synchronizedMethodLockObject = synchronizedMethodLockObject;
        }

        @Override
        public void run() {
            super.run();
            synchronizedMethodLockObject.commonMethod();
        }
    }

    /**
     * 测试步骤：通过在 SynchronizedMethodLockObject#commonMethod() 方法前增删 synchronized 关键字分别运行
     * <p>
     * 通过本例可知：调用 synchronized 关键字声明的方法一定是排队运行的。
     * 另外要注意 “共享” 这个词，只有共享资源的读写访问才需要同步化，非共享资源就没有同步的必要。
     *
     * @throws IOException
     */
    @Test
    public void testManyObjectManyLock01() throws IOException {
        SynchronizedMethodLockObject synchronizedMethodLockObject = new SynchronizedMethodLockObject();
        SynchronizedMethodLockObjectThreadA threadA = new SynchronizedMethodLockObjectThreadA(synchronizedMethodLockObject);
        threadA.setName("A");
        SynchronizedMethodLockObjectThreadB threadB = new SynchronizedMethodLockObjectThreadB(synchronizedMethodLockObject);
        threadB.setName("B");
        threadA.start();
        threadB.start();
        System.in.read();
    }

    class SynchronizedMethodLockObjectThreadC extends Thread {
        private SynchronizedMethodLockObject synchronizedMethodLockObject;

        public SynchronizedMethodLockObjectThreadC(SynchronizedMethodLockObject synchronizedMethodLockObject) {
            super();
            this.synchronizedMethodLockObject = synchronizedMethodLockObject;
        }

        @Override
        public void run() {
            super.run();
            synchronizedMethodLockObject.methodA();
        }
    }

    class SynchronizedMethodLockObjectThreadD extends Thread {
        private SynchronizedMethodLockObject synchronizedMethodLockObject;

        public SynchronizedMethodLockObjectThreadD(SynchronizedMethodLockObject synchronizedMethodLockObject) {
            super();
            this.synchronizedMethodLockObject = synchronizedMethodLockObject;
        }

        @Override
        public void run() {
            super.run();
            synchronizedMethodLockObject.methodB();
        }
    }

    /**
     * 本示例是两个线程访问同一个对象的两个方法(一个方法有 synchronized 关键字，一个方法没有 synchronized 关键字)
     * 测试步骤：在 SynchronizedMethodLockObject#methodA() 方法前增加 synchronized 关键字，删除 SynchronizedMethodLockObject#methodB() 方法前的 synchronized 关键字后运行
     * <p>
     * 通过该测试结果可知：虽然线程 A 先持有了 Object 对象的锁，但线程 B 完全可以异步调用非 synchronized 类型的方法。
     *
     * @throws IOException
     */
    @Test
    public void testManyObjectManyLock02() throws IOException {
        SynchronizedMethodLockObject synchronizedMethodLockObject = new SynchronizedMethodLockObject();
        SynchronizedMethodLockObjectThreadC threadA = new SynchronizedMethodLockObjectThreadC(synchronizedMethodLockObject);
        threadA.setName("A");
        SynchronizedMethodLockObjectThreadD threadB = new SynchronizedMethodLockObjectThreadD(synchronizedMethodLockObject);
        threadB.setName("B");
        threadA.start();
        threadB.start();
        System.in.read();
    }

    /**
     * 本示例是两个线程访问同一个对象的两个同步方法(两个方法均有 synchronized 关键字)
     * 测试步骤：分别在 SynchronizedMethodLockObject#methodA() 和 SynchronizedMethodLockObject#methodB() 方法前增加 synchronized 关键字运行
     * <p>
     * 结论：
     * 1、线程 A 先持有了 Object 对象的 Lock 锁，线程 B 可以以异步的方式调用 Object 对象中的非 synchronized 类型的方法。
     * 2、线程 A 先持有了 Object 对象的 Lock 锁，线程 B 这时调用 Object 对象中的 synchronized 类型的方法则需等待，也就是同步。
     *
     * @throws IOException
     */
    @Test
    public void testManyObjectManyLock03() throws IOException {
        SynchronizedMethodLockObject synchronizedMethodLockObject = new SynchronizedMethodLockObject();
        SynchronizedMethodLockObjectThreadC threadA = new SynchronizedMethodLockObjectThreadC(synchronizedMethodLockObject);
        threadA.setName("A");
        SynchronizedMethodLockObjectThreadD threadB = new SynchronizedMethodLockObjectThreadD(synchronizedMethodLockObject);
        threadB.setName("B");
        threadA.start();
        threadB.start();
        System.in.read();
    }
}
