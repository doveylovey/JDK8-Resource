package com.study.test.communication;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作用描述：线程与线程之间不是独立的个体，他们彼此之间可以相互通信协作。
 * <p>
 * <p>
 * wait() 的作用：使当前执行代码的线程进行等待，它是 Object 类中的方法，用于将当前线程放入 “预执行队列” 中，并在 wait() 所在的代码行处停止执行，直到接到通知或被中断为止。
 * 执行 wait() 后当前线程会释放锁，在从 wait() 返回前，线程会与其他线程竞争重新获得锁。
 * 如果调用 wait() 时没有持有适当的锁，则会抛出 IllegalMonitorStateException 异常，它是 RuntimeException 的一个子类，因此不需要 try-catch 语进行异常捕捉。
 * <p>
 * notify() 的作用：用于通知那些可能等待该对象的对象锁的其他线程，如果有多个线程在等待，则由线程规划器随机挑选出其中一个呈 wait 状态的线程，对其发出通知 notify，并使它等待获取该对象的对象锁。
 * 需要说明的是，在执行 notify() 后，当前线程不会马上释放该对象锁，呈 wait 状态的线程也并不能马上获取该对象锁，而要等到执行 notify() 的线程将程序执行完(即：退出 synchronized 代码块后，当前线程才会释放锁，而呈 wait 状态所在的线程才可以获取该对象锁)。
 * 当第一个获得了该对象锁的 wait 线程运行完毕以后，它会释放掉该对象锁，此时如果该对象没有再次使用 notify 语句，则即便该对象已经空闲，其他 wait 状态等待的线程由于没有得到该对象的通知，还会继续阻塞在 wait 状态，直到这个对象发出 notify 或 notifyAll。
 * 该方法也需要在同步方法或同步代码块中使用(即：在调用前，线程也必须获得该对象的对象级别锁)。如果调用 notify() 时没有持有适当的锁，则也会抛出 IllegalMonitorStateException 异常。
 * <p>
 * 一句话总结 wait/notify：wait 使线程停止运行，而 notify 使停止的线程继续运行。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月07日
 */
public class CommunicationTest01 {
    /**
     * 不使用等待/通知机制实现线程间通信
     */
    class NotUseWaitAndNotify {
        private volatile List<String> list = new ArrayList<>();

        public void add() {
            list.add("不使用等待/通知机制实现线程间通信");
        }

        public int size() {
            return list.size();
        }
    }

    class NotUseWaitAndNotifyThreadA extends Thread {
        private NotUseWaitAndNotify notUseWaitAndNotify;

        public NotUseWaitAndNotifyThreadA(NotUseWaitAndNotify notUseWaitAndNotify) {
            super();
            this.notUseWaitAndNotify = notUseWaitAndNotify;
        }

        @Override
        public void run() {
            super.run();
            try {
                for (int i = 0; i < 10; i++) {
                    notUseWaitAndNotify.add();
                    System.out.println("添加了 " + (i + 1) + " 个元素！");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class NotUseWaitAndNotifyThreadB extends Thread {
        private NotUseWaitAndNotify notUseWaitAndNotify;

        public NotUseWaitAndNotifyThreadB(NotUseWaitAndNotify notUseWaitAndNotify) {
            super();
            this.notUseWaitAndNotify = notUseWaitAndNotify;
        }

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    if (notUseWaitAndNotify.size() == 5) {
                        System.out.println("size() == 5，线程 B 要退出了！");
                        throw new InterruptedException();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 虽然两个线程间实现了通信，但有一个弊端：线程 B 不断通过 while 语句轮询机制来检测某一个条件，这样会浪费 CPU 资源。
     * 如果轮询的时间间隔很小，更浪费 CPU 资源；如果轮询的时间间隔很大，有可能会取不到想要得到的数据。
     * 因此就需要一种机制来实现减少 CPU 的资源浪费，还可以在多个线程间通信，它就是 “wait/notify” 机制
     *
     * @throws IOException
     */
    @Test
    public void testNotUseWaitAndNotify01() throws IOException {
        NotUseWaitAndNotify notUseWaitAndNotify = new NotUseWaitAndNotify();
        NotUseWaitAndNotifyThreadA threadA = new NotUseWaitAndNotifyThreadA(notUseWaitAndNotify);
        threadA.setName("A");
        threadA.start();
        NotUseWaitAndNotifyThreadB threadB = new NotUseWaitAndNotifyThreadB(notUseWaitAndNotify);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 如果调用 wait() 时没有持有适当的锁，则会抛出 IllegalMonitorStateException 异常，它是 RuntimeException 的一个子类，因此不需要 try-catch 语进行异常捕捉。
     *
     * @throws IOException
     */
    @Test
    public void testHoldLock01() throws IOException {
        try {
            String newString = new String("lock");
            // 出现异常的原因是没有 "对象监视器"，也就是没有加锁同步。
            newString.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.in.read();
    }

    /**
     * 从结果可以看出：wait() 下面的代码不会执行。但线程不能永远等待下去，那样程序就停止不前，不能继续向下运行了。
     * 那么，如何使呈 wait 等待状态的线程继续运行呢？答案就是使用 notify() 方法。
     *
     * @throws IOException
     */
    @Test
    public void testNotHoldLock01() throws IOException {
        try {
            String newString = new String("lock");
            System.out.println("synchronized 同步代码块前面的代码");
            synchronized (newString) {
                System.out.println("synchronized 中的第一行代码：wait() 之前");
                newString.wait();
                System.out.println("synchronized 中的第一行代码：wait() 之后");
            }
            System.out.println("synchronized 同步代码块后面的代码");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.in.read();
    }

    class UseWaitAndNotifySimpleThreadA extends Thread {
        private Object lock;

        public UseWaitAndNotifySimpleThreadA(Object lock) {
            super();
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronized (lock) {
                    System.out.println("wait()方法开始，time=" + System.currentTimeMillis());
                    lock.wait();
                    System.out.println("wait()方法结束，time=" + System.currentTimeMillis());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class UseWaitAndNotifySimpleThreadB extends Thread {
        private Object lock;

        public UseWaitAndNotifySimpleThreadB(Object lock) {
            super();
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            synchronized (lock) {
                System.out.println("notify()方法开始，time=" + System.currentTimeMillis());
                lock.notify();
                System.out.println("notify()方法结束，time=" + System.currentTimeMillis());
            }
        }
    }

    /**
     * 从控制台打印的结果来看，3秒后线程被 notify 通知唤醒
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testUseWaitAndNotify01() throws IOException, InterruptedException {
        Object lock = new Object();
        new UseWaitAndNotifySimpleThreadA(lock).start();
        Thread.sleep(3000);
        new UseWaitAndNotifySimpleThreadB(lock).start();
        System.in.read();
    }

    static class UseWaitAndNotifySize {
        private static List<String> list = new ArrayList<>();

        public static void add() {
            list.add("不使用等待/通知机制实现线程间通信");
        }

        public static int size() {
            return list.size();
        }
    }

    class UseWaitAndNotifySizeThreadA extends Thread {
        private Object lock;

        public UseWaitAndNotifySizeThreadA(Object lock) {
            super();
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronized (lock) {
                    if (UseWaitAndNotifySize.size() != 5) {
                        System.out.println("wait()方法开始，time=" + System.currentTimeMillis());
                        lock.wait();
                        System.out.println("wait()方法结束，time=" + System.currentTimeMillis());
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class UseWaitAndNotifySizeThreadB extends Thread {
        private Object lock;

        public UseWaitAndNotifySizeThreadB(Object lock) {
            super();
            this.lock = lock;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronized (lock) {
                    for (int i = 0; i < 10; i++) {
                        UseWaitAndNotifySize.add();
                        if (UseWaitAndNotifySize.size() == 5) {
                            lock.notify();
                            System.out.println("已发出通知！");
                        }
                        System.out.println("添加了" + (i + 1) + "个元素！");
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testUseWaitAndNotifySize01() throws IOException, InterruptedException {
        Object lock = new Object();
        new UseWaitAndNotifySizeThreadA(lock).start();
        Thread.sleep(3000);
        new UseWaitAndNotifySizeThreadB(lock).start();
        System.in.read();
    }
}
