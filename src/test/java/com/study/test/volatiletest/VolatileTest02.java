package com.study.test.volatiletest;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 作用描述：volatile 非原子的特性。
 * volatile 虽然增加了实例变量在多个线程之间的可见性，但它却不具备同步性，那么也就不具备原子性。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class VolatileTest02 {
    /**
     * volatile 不能保证原子性
     */
    static class VolatileAndAtomicThread1 extends Thread {
        public volatile static int count;

        private static void addCount() {
            for (int i = 0; i < 100; i++) {
                count++;
            }
            System.out.println("count=" + count);
        }

        @Override
        public void run() {
            super.run();
            addCount();
        }
    }

    @Test
    public void testVolatileAndAtomicThread101() throws IOException, InterruptedException {
        VolatileAndAtomicThread1[] array = new VolatileAndAtomicThread1[100];
        for (int i = 0; i < 100; i++) {
            array[i] = new VolatileAndAtomicThread1();
        }
        for (int i = 0; i < 100; i++) {
            array[i].start();
        }
        System.in.read();
    }

    /**
     * volatile 本身并不能保证原子性
     */
    static class VolatileAndAtomicThread2 extends Thread {
        /**
         * 由于在 {@link VolatileAndAtomicThread2#addCount()} 方法前加了 synchronized 同步关键字，所以此处没必要加 volatile 关键字了。
         * <p>
         * volatile 关键字的主要使用场合是：在多个线程中可以感知实例变量的值被更改了，并且可以获得最新的值以供使用，也就是用多线程读取共享变量时可以获得最新值使用。
         * <p>
         * volatile 关键字强制线程每次都从共享内存中读取变量值，而不是从线程私有内存中读取，这样就保证了同步数据的可见性。
         * 需要注意：如果修改实例变量中的数据，如 i++，即 i = i + 1，这样的操作并非原子操作，也就是非线程安全的。
         * i++ 的操作步骤可分解为：从内存中取出 i 的值；计算 i 的值；将 i 的值协会内存中。如果在第二步中计算值的时候，另外一个线程也修改了 i 的值，这时就会出现脏数据。
         * 解决办法就是使用 synchronized 关键字，所以说 volatile 本身并不保证数据的原子性，而是强制对数据的读写及时影响到主内存，因此在多线程中访问同一个实例变量时需要加锁同步。
         */
        public /*volatile*/ static int count;

        /**
         * 注意一定要加 static 关键字，这样 synchronized 与 static 锁的内容就是 VolatileAndAtomicThread2.class 类了，以此达到同步效果
         */
        private synchronized static void addCount() {
            for (int i = 0; i < 100; i++) {
                count++;
            }
            System.out.println("count=" + count);
        }

        @Override
        public void run() {
            super.run();
            addCount();
        }
    }

    @Test
    public void testVolatileAndAtomicThread201() throws IOException, InterruptedException {
        VolatileAndAtomicThread2[] array = new VolatileAndAtomicThread2[100];
        for (int i = 0; i < 100; i++) {
            array[i] = new VolatileAndAtomicThread2();
        }
        for (int i = 0; i < 100; i++) {
            array[i].start();
        }
        System.in.read();
    }

    /**
     * 使用原子类进行 i++ 操作
     */
    class AtomicClassThread extends Thread {
        private AtomicInteger atomicInteger = new AtomicInteger(0);

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++) {
                System.out.println("getAndIncrement()：" + atomicInteger.getAndIncrement());
                //System.out.println("incrementAndGet()：" + atomicInteger.incrementAndGet());
            }
        }
    }

    @Test
    public void testAtomicClassThread01() throws IOException {
        AtomicClassThread atomicClassThread = new AtomicClassThread();
        new Thread(atomicClassThread).start();
        new Thread(atomicClassThread).start();
        new Thread(atomicClassThread).start();
        new Thread(atomicClassThread).start();
        new Thread(atomicClassThread).start();
        System.in.read();
    }

    /**
     * 原子类也并非完全安全：原子类在具有有逻辑性的情况下输出结果也具有随机性。
     */
    static class AtomicClassNotSafe {
        public static AtomicLong atomicLong = new AtomicLong();

        /**
         * 注意有无 synchronized 关键字控制台的输出结果：
         * 若无，打印顺序错误，每次应该先加 100 然后再加 1。出现这样的情况是因为 addAndGet() 方法是原子的，但方法和方法之间的调用却不是原子的，解决这类问题必须要用同步。
         * 若有，答因顺序正确。
         */
        public /*synchronized*/ void addNum() {
            System.out.println("【addNum()方法】threadName=" + Thread.currentThread().getName() + "， 加 100 后为：" + atomicLong.addAndGet(100));
            atomicLong.addAndGet(1);
        }
    }

    class AtomicClassNotSafeThread extends Thread {
        private AtomicClassNotSafe atomicClassNotSafe;

        public AtomicClassNotSafeThread(AtomicClassNotSafe atomicClassNotSafe) {
            super();
            this.atomicClassNotSafe = atomicClassNotSafe;
        }

        @Override
        public void run() {
            atomicClassNotSafe.addNum();
        }
    }

    /**
     * 注意 {@link AtomicClassNotSafe#addNum()} 方法有无 synchronized 关键字控制台的输出结果：
     * 若无，打印顺序错误，每次应该先加 100 然后再加 1。出现这样的情况是因为 addAndGet() 方法是原子的，但方法和方法之间的调用却不是原子的，解决这类问题必须要用同步。
     * 若有，答因顺序正确。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testAtomicClassNotSafe01() throws IOException, InterruptedException {
        AtomicClassNotSafe atomicClassNotSafe = new AtomicClassNotSafe();
        AtomicClassNotSafeThread[] array = new AtomicClassNotSafeThread[10];
        for (int i = 0; i < array.length; i++) {
            array[i] = new AtomicClassNotSafeThread(atomicClassNotSafe);
        }
        for (int i = 0; i < array.length; i++) {
            array[i].start();
        }
        Thread.sleep(1000);
        System.out.println("最终结果：" + atomicClassNotSafe.atomicLong.get());
        System.in.read();
    }
}
