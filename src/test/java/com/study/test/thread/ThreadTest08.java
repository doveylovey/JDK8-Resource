package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：暂停线程。暂停的线程意味着它还可以恢复运行，在 Java 多线程中，可以使用 suspend() 方法暂停线程，使用 resume() 方法恢复线程的执行。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月04日
 */
public class ThreadTest08 {
    class MyThread01 extends Thread {
        private long counter = 0;

        public long getCounter() {
            return counter;
        }

        public void setCounter(long counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            while (true) {
                counter++;
            }
        }
    }

    @Test
    public void testMyThread01() throws InterruptedException {
        // 从控制台打印来看：线程的确被暂停了，而且还可以恢复成运行状态。
        MyThread01 myThread01 = new MyThread01();
        myThread01.start();
        Thread.sleep(5000);
        // A 段
        myThread01.suspend();
        System.out.println("【A 段】timer=" + System.currentTimeMillis() + "，i=" + myThread01.getCounter());
        Thread.sleep(5000);
        System.out.println("【A 段】timer=" + System.currentTimeMillis() + "，i=" + myThread01.getCounter());
        // B 段
        myThread01.resume();
        Thread.sleep(5000);
        // C 段
        myThread01.suspend();
        System.out.println("【C 段】timer=" + System.currentTimeMillis() + "，i=" + myThread01.getCounter());
        Thread.sleep(5000);
        System.out.println("【C 段】timer=" + System.currentTimeMillis() + "，i=" + myThread01.getCounter());
    }

    /**
     * suspend() 方法与 resume() 方法的缺点一：独占。
     * 在使用 suspend() 和 resume() 方法时，如果使用不当，极易造成公共的同步对象的独占，使得其他线程无法访问公共同步对象。
     */
    class ExclusiveObject {
        synchronized public void printString() {
            System.out.println("ExclusiveObject#printString() begin");
            if (Thread.currentThread().getName().equals("a")) {
                System.out.println("a 线程永远 suspend 了！");
                Thread.currentThread().suspend();
            }
            System.out.println("ExclusiveObject#printString() end");
        }
    }

    /**
     * suspend() 方法与 resume() 方法的缺点一：独占
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testMyThread02() throws InterruptedException, IOException {
        final ExclusiveObject synchronizedObject = new ExclusiveObject();
        Thread thread1 = new Thread(() -> synchronizedObject.printString());
        thread1.setName("a");
        thread1.start();
        Thread.sleep(1000);

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 启动了，但进入不了 ExclusiveObject#printString() 方法！因为 ExclusiveObject#printString() 方法被 a 线程锁定并且永远 suspend 了！");
            synchronizedObject.printString();
        });
        thread2.start();
        System.in.read();
    }

    class MyThread02 extends Thread {
        private long i = 0;

        @Override
        public void run() {
            while (true) {
                i++;
            }
        }
    }

    class MyThread03 extends Thread {
        private long i = 0;

        @Override
        public void run() {
            while (true) {
                i++;
                System.out.println("i=" + i);
            }
        }
    }

    /**
     * suspend() 方法与 resume() 方法的缺点一：独占
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testMyThread03() throws InterruptedException, IOException {
        // 若用 MyThread02 类，则控制台只会打印 main end.
        //MyThread02 myThread03 = new MyThread02();

        // 若用 MyThread03 类，则控制台不会打印 main end.
        MyThread03 myThread03 = new MyThread03();
        // 使用 MyThread02 和 MyThread03 出现不同结果的原因是：当程序运行到 System.out.println() 方法内部停止时，同步锁未被释放，请参考 println() 源码。
        // 这导致当前 PrintStream 对象的 println() 方法一直呈暂停状态，并且不释放锁，导致 main 方法中的 System.out.println("main end."); 迟迟不能打印。
        myThread03.start();
        Thread.sleep(1000);
        // 虽然 suspend() 方法是过期作废的方法，但研究到它过期作废的原因是很有意义的。
        myThread03.suspend();
        System.out.println("main end.");
        System.in.read();
    }

    /**
     * suspend() 方法与 resume() 方法的缺点二：不同步。
     * 在使用 suspend() 和 resume() 方法时也容易出现因为线程的暂停而导致数据不同步的情况。
     */
    class SynchronizedObject {
        private String username = "a";
        private String password = "aa";

        public void setValue(String username, String password) {
            this.username = username;
            if (Thread.currentThread().getName().equals("a")) {
                System.out.println("停止 a 线程！");
                Thread.currentThread().suspend();
            }
            this.password = password;
        }

        public void printString() {
            System.out.println("username=" + username + "，password=" + password);
        }
    }

    /**
     * suspend() 方法与 resume() 方法的缺点二：不同步。
     *
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void testMyThread04() throws InterruptedException, IOException {
        // 程序运行结果出现值不同步的情况，所以在程序中使用 suspend() 方法要格外注意。
        final SynchronizedObject synchronizedObject = new SynchronizedObject();
        Thread thread1 = new Thread(() -> synchronizedObject.setValue("b", "bb"));
        thread1.setName("a");
        thread1.start();
        Thread.sleep(500);

        Thread thread2 = new Thread(() -> synchronizedObject.printString());
        thread2.start();
        System.in.read();
    }
}
