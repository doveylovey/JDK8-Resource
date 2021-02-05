package com.study.test.concurrent;

import java.util.concurrent.Semaphore;

/**
 * 题目：3个线程循环打印ABC，其中A打印3次，B打印2次，C打印1次，循环打印2轮。
 * 解法：Semaphore 信号量方式。
 * 思路：信号量(Semaphore)有时也被称为信号灯，是在多线程环境下使用的一种设施，它负责协调各个线程以保证它们能够正确合理的使用公共资源。
 * Semaphore 线程同步机制，当调用 acquire() 方法时，内部计数器数值增加；调用 release() 方法时，内部计数器数值递减；
 * 计数器的值不能小于0，如果等于0，则 acquire() 方法被阻塞，需要等待其他线程调用 release() 方法。
 * 参考 https://www.cnblogs.com/yaowen/p/10103196.html
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class LoopPrintlnSemaphore {
    public static void main(String[] args) throws InterruptedException {
        // A打印3次
        new PrintThreadA("Thread-A", 3).start();
        //TimeUnit.MILLISECONDS.sleep(500);
        // B打印2次
        new PrintThreadB("Thread-B", 2).start();
        //TimeUnit.MILLISECONDS.sleep(500);
        // C打印1次
        new PrintThreadC("Thread-C", 1).start();
    }

    // 以A开始的信号量，初始信号量数量为1
    private static Semaphore A = new Semaphore(1);
    // B、C信号量，A完成后开始，初始信号数量为0
    private static Semaphore B = new Semaphore(0);
    private static Semaphore C = new Semaphore(0);

    static class PrintThreadA extends Thread {
        private int count;

        public PrintThreadA(String threadName, int count) {
            this.setName(threadName);
            this.count = count;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 2; i++) {
                    // A获取信号执行，A信号量减1，当A为0时将无法继续获得该信号量
                    A.acquire();
                    for (int j = 0; j < count; j++) {
                        System.out.print("A");
                    }
                    // B释放信号，B信号量加1(初始为0)，此时可以获取B信号量
                    B.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class PrintThreadB extends Thread {
        private int count;

        public PrintThreadB(String threadName, int count) {
            this.setName(threadName);
            this.count = count;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 2; i++) {
                    B.acquire();
                    for (int j = 0; j < count; j++) {
                        System.out.print("B");
                    }
                    C.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class PrintThreadC extends Thread {
        private int count;

        public PrintThreadC(String threadName, int count) {
            this.setName(threadName);
            this.count = count;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 2; i++) {
                    C.acquire();
                    for (int j = 0; j < count; j++) {
                        System.out.println("C");
                    }
                    A.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}