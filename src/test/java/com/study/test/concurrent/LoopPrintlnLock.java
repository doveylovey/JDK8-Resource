package com.study.test.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：3个线程循环打印ABC，其中A打印3次，B打印2次，C打印1次，循环打印2轮。
 * 解法：Lock 锁方法。
 * 思路：Lock 锁机制是 JDK5 之后新增的锁机制，不同于内置锁，Lock 锁必须显式声明，并在合适的位置释放锁。Lock 是一个接口，通过 ReentrantLock 具体实现进行显式的锁操作(即获取锁和释放锁)
 * 参考 https://www.cnblogs.com/yaowen/p/10103196.html
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class LoopPrintlnLock {
    public static void main(String[] args) throws InterruptedException {
        // 当前正在执行线程的标记
        int num = 1;
        PrintTask print = new PrintTask(num);
        new Thread(new PrintThreadA(print), "Thread-A").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(new PrintThreadB(print), "Thread-B").start();
        TimeUnit.MILLISECONDS.sleep(500);
        new Thread(new PrintThreadC(print), "Thread-C").start();
    }

    static class PrintThreadA implements Runnable {
        private PrintTask print;

        public PrintThreadA(PrintTask print) {
            this.print = print;
        }

        @Override
        public void run() {
            print.printA();
        }
    }

    static class PrintThreadB implements Runnable {
        private PrintTask print;

        public PrintThreadB(PrintTask print) {
            this.print = print;
        }

        @Override
        public void run() {
            print.printB();
        }
    }

    static class PrintThreadC implements Runnable {
        private PrintTask print;

        public PrintThreadC(PrintTask print) {
            this.print = print;
        }

        @Override
        public void run() {
            print.printC();
        }
    }

    static class PrintTask {
        // 通过 JDK5 中的 Lock 锁来保证线程访问的互斥
        private static final Lock lock = new ReentrantLock();
        // 当前正在执行线程的标记
        private int num;

        public PrintTask(int num) {
            this.num = num;
        }

        public void printA() {
            for (int j = 0; j < 2; ) {
                // 表示循环打印2轮
                lock.lock();
                try {
                    while (num == 1) {
                        for (int i = 0; i < 3; i++) {
                            // 表示打印3次
                            System.out.println(Thread.currentThread().getName() + " 第 " + j + " 轮，第 " + i + " 次：A");
                        }
                        // 打印A线程执行完，通知打印B线程
                        num = 2;
                        j++;
                    }
                } finally {
                    // 调用了 lock() 方法后，需在 finally 语句块(确保一定会执行，除非执行了 exit 方法)里调用 unlock() 方法，否则可能造成死锁等问题
                    lock.unlock();
                }
            }
        }

        public void printB() {
            for (int j = 0; j < 2; ) {
                // 表示循环打印2轮
                lock.lock();
                try {
                    while (num == 2) {
                        for (int i = 0; i < 2; i++) {
                            // 表示打印2次
                            System.out.println(Thread.currentThread().getName() + " 第 " + j + " 轮，第 " + i + " 次：B");
                        }
                        // 打印B线程执行完 ，通知打印C线程
                        num = 3;
                        j++;
                    }
                } finally {
                    // 调用了 lock() 方法后，需在 finally 语句块(确保一定会执行，除非执行了 exit 方法)里调用 unlock() 方法，否则可能造成死锁等问题
                    lock.unlock();
                }
            }
        }

        public void printC() {
            for (int j = 0; j < 2; ) {
                // 表示循环打印2轮
                lock.lock();
                try {
                    while (num == 3) {
                        System.out.println(Thread.currentThread().getName() + " 第 " + j + " 轮，第 1 次：C");
                        // 打印C线程执行完 ，通知打印A线程
                        num = 1;
                        j++;
                    }
                } finally {
                    // 调用了 lock() 方法后，需在 finally 语句块(确保一定会执行，除非执行了 exit 方法)里调用 unlock() 方法，否则可能造成死锁等问题
                    lock.unlock();
                }
            }
        }
    }
}
