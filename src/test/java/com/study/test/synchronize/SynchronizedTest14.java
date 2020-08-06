package com.study.test.synchronize;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：多线程的死锁。
 * Java 线程死锁是一个经典的多线程问题，因为不同线程都在等待不可能被释放的锁，从而导致所有任务都无法继续进行。
 * 在多线程技术中，“死锁” 是必须要避免的，因为这会造成线程 “假死”。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class SynchronizedTest14 {
    class DeadLockThread implements Runnable {
        public String param;
        public Object lock1 = new Object();
        public Object lock2 = new Object();

        public void setParam(String param) {
            this.param = param;
        }

        @SneakyThrows
        @Override
        public void run() {
            if ("a".equalsIgnoreCase(param)) {
                synchronized (lock1) {
                    System.out.println("time：" + System.currentTimeMillis() + "，param：" + param);
                    Thread.sleep(3000);
                    synchronized (lock2) {
                        System.out.println("按 lock1 -> lock2 的代码顺序执行了！");
                    }
                }
            }
            if ("b".equalsIgnoreCase(param)) {
                synchronized (lock2) {
                    System.out.println("time：" + System.currentTimeMillis() + "，param：" + param);
                    Thread.sleep(3000);
                    synchronized (lock1) {
                        System.out.println("按 lock2 -> lock1 的代码顺序执行了！");
                    }
                }
            }
        }
    }

    /**
     * 可以使用 JDK 自带的工具来检测是否有死锁发生，大致步骤如下：
     * 1、打开 CMD 命令行，进入到 JDK 安装目录下的 bin 目录中，执行 jps 命令可以得到线程的 id 值。
     * 2、根据第 1 步中得到的线程的 id 值，执行命令 jstack -l 线程的id值。
     * 3、查看是否有死锁出现。
     * <p>
     * 死锁是程序设计的 bug，在设计程序时就要避免双方互相持有对方的锁的情况。
     * 需要说明的是，本示例使用 synchronized 嵌套的代码结构来实现死锁，其实不使用嵌套的 synchronized 代码结构也可能会出现死锁，
     * 和嵌套与否没有任何关系，不要被代码结构所误导。记住：只要互相等待对方释放锁就有可能出现死锁。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedInfiniteWait01() throws IOException, InterruptedException {
        DeadLockThread deadLockThread1 = new DeadLockThread();
        deadLockThread1.setParam("a");
        new Thread(deadLockThread1).start();
        Thread.sleep(100);
        deadLockThread1.setParam("b");
        new Thread(deadLockThread1).start();
        System.in.read();
    }
}
