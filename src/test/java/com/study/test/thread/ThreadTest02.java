package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：实例变量与线程安全
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年07月26日
 */
public class ThreadTest02 {
    /**
     * 情况一：不共享数据
     */
    class NotSharedThread01 extends Thread {
        private int count = 5;

        public NotSharedThread01(String name) {
            super();
            // 设置线程名称
            this.setName(name);
        }

        @Override
        public void run() {
            super.run();
            while (count > 0) {
                count--;
                System.out.println("由线程：" + Thread.currentThread().getName() + " 计算，count = " + count);
            }
        }
    }

    @Test
    public void testNotSharedThread01() throws IOException {
        // 共创建了 3 个线程，每个线程都有自己的 count 变量，自己减少自己 count 变量的值。此时不存在多个线程访问同一个实例变量
        new NotSharedThread01("A").start();
        new NotSharedThread01("B").start();
        new NotSharedThread01("C").start();
        System.in.read();
    }

    /**
     * 情况二：共享数据
     */
    class SharedThread01 extends Thread {
        private int count = 5;

        /**
         * 注意 run() 方法中有无 synchronized 关键字的区别。若无 synchronized 则可能会出现不同线程对应同一个 count 值。
         * <p>
         * 通过在 run() 方法前加入 synchronized 关键字，使多个线程在执行 run() 方法时以排队的方式进行处理。
         * <p>
         * 当一个线程在调用 run() 方法前，先判断 run() 方法有没有被上锁：
         * 如果上锁，说明有其他线程正在调用 run() 方法，此时必须等其他线程对 run() 方法调用结束后才可以执行run() 方法。
         * 这样就实现了排队调用 run() 方法的目的，也就达到了按顺序对 count 变量减 1 的效果了。
         * <p>
         * synchronized 可以在任意对象或方法上加锁，加锁的这段代码称为 "互斥区" 或 "临界区"。
         * <p>
         * 当一个线程想要执行同步方法里面的代码时，线程首先尝试去拿这把锁：
         * 如果能够拿到，那么这个线程就可以执行同步方法里面的代码；
         * 如果不能拿到，那么这个线程就会不断尝试去拿这把锁，直到能够拿到为止，而且是有多个线程同时去抢夺这把锁。
         */
        @Override
        public synchronized void run() {
            super.run();
            // 此处就不能用循环语句了，因为使用同步后其他线程就得不到运行的机会，会一直由一个线程进行减法运算
            if (count > 0) {
                count--;
                System.out.println("由线程：" + Thread.currentThread().getName() + " 计算，count = " + count);
            } else {
                System.out.println("线程：" + Thread.currentThread().getName() + " 计算，count > 0 不成立！");
            }
        }
    }

    @Test
    public void testSharedThread01() throws IOException {
        SharedThread01 sharedThread01 = new SharedThread01();
        new Thread(sharedThread01, "A").start();
        new Thread(sharedThread01, "B").start();
        new Thread(sharedThread01, "C").start();
        new Thread(sharedThread01, "D").start();
        new Thread(sharedThread01, "E").start();
        new Thread(sharedThread01, "F").start();
        System.in.read();
    }
}
