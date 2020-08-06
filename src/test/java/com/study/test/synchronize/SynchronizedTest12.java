package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：数据类型 String 的常量池特性。
 * JVM 中具有 String 常量池缓存的功能，所以 String a = "test"; String b = "test"; a == b 的结果为 true。
 * 将 synchronized(string) 同步代码块与 String 联合使用时，要留意常量池带来的一些例外。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class SynchronizedTest12 {
    /**
     * 使用 String 作为 synchronized 同步代码块的锁对象
     */
    static class StringAndSynchronized {
        public static void printParam(String param) throws InterruptedException {
            synchronized (param) {
                while (true) {
                    System.out.println("【printParam()方法】threadName：" + Thread.currentThread().getName() + "，time：" + System.currentTimeMillis() + "，param：" + param);
                    Thread.sleep(1000);
                }
            }
        }
    }

    class StringAndSynchronizedThreadA extends Thread {
        private StringAndSynchronized stringAndSynchronized;

        public StringAndSynchronizedThreadA(StringAndSynchronized stringAndSynchronized) {
            super();
            this.stringAndSynchronized = stringAndSynchronized;
        }

        @Override
        public void run() {
            super.run();
            try {
                // 注意此处的参数和 StringAndSynchronizedThreadB#run() 方法中的参数一样
                stringAndSynchronized.printParam("test");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class StringAndSynchronizedThreadB extends Thread {
        private StringAndSynchronized stringAndSynchronized;

        public StringAndSynchronizedThreadB(StringAndSynchronized stringAndSynchronized) {
            super();
            this.stringAndSynchronized = stringAndSynchronized;
        }

        @Override
        public void run() {
            super.run();
            try {
                // 注意此处的参数和 StringAndSynchronizedThreadA#run() 方法中的参数一样
                stringAndSynchronized.printParam("test");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 运行结果中只有线程 A 输出：因为两个 String 的值都是 test，即两个线程持有相同的锁，从而导致线程 B 不能运行。这就是 String 常量池带来的问题。
     * 因此在大多数情况下，synchronized 同步代码块都不使用 String 作为锁对象，而用其他对象，比如用 new Object() 实例化一个 Object 对象，但它并不放入缓存中。
     *
     * @throws IOException
     */
    @Test
    public void testStringAndSynchronized01() throws IOException {
        StringAndSynchronized stringAndSynchronized = new StringAndSynchronized();
        StringAndSynchronizedThreadA threadA = new StringAndSynchronizedThreadA(stringAndSynchronized);
        threadA.setName("A");
        threadA.start();
        StringAndSynchronizedThreadB threadB = new StringAndSynchronizedThreadB(stringAndSynchronized);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 使用 Object(非 String) 作为 synchronized 同步代码块的锁对象
     */
    static class ObjectAndSynchronized {
        public static void printParam(Object param) throws InterruptedException {
            synchronized (param) {
                while (true) {
                    System.out.println("【printParam()方法】threadName：" + Thread.currentThread().getName() + "，time：" + System.currentTimeMillis() + "，param：" + param);
                    Thread.sleep(1000);
                }
            }
        }
    }

    class ObjectAndSynchronizedThreadA extends Thread {
        private ObjectAndSynchronized objectAndSynchronized;

        public ObjectAndSynchronizedThreadA(ObjectAndSynchronized objectAndSynchronized) {
            super();
            this.objectAndSynchronized = objectAndSynchronized;
        }

        @Override
        public void run() {
            super.run();
            try {
                objectAndSynchronized.printParam(new Object());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class ObjectAndSynchronizedThreadB extends Thread {
        private ObjectAndSynchronized objectAndSynchronized;

        public ObjectAndSynchronizedThreadB(ObjectAndSynchronized objectAndSynchronized) {
            super();
            this.objectAndSynchronized = objectAndSynchronized;
        }

        @Override
        public void run() {
            super.run();
            try {
                objectAndSynchronized.printParam(new Object());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 交替打印的原因是：持有的锁不是同一个。
     *
     * @throws IOException
     */
    @Test
    public void testObjectAndSynchronized01() throws IOException {
        ObjectAndSynchronized objectAndSynchronized = new ObjectAndSynchronized();
        ObjectAndSynchronizedThreadA threadA = new ObjectAndSynchronizedThreadA(objectAndSynchronized);
        threadA.setName("A");
        threadA.start();
        ObjectAndSynchronizedThreadB threadB = new ObjectAndSynchronizedThreadB(objectAndSynchronized);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
