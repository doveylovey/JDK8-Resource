package com.study.test.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 题目：3个线程循环打印ABC，其中A打印3次，B打印2次，C打印1次，循环打印2轮。
 * 解法：AtomicInteger 方法
 * 思路：AtomicInteger 是一个提供对 Integer 进行原子操作的类。在 Java 中，++i 和 i++ 操作并不是线程安全的，在使用的时候，不可避免的会用到 synchronized 关键字，而 AtomicInteger 则提供了一些线程安全的操作方法。
 * 参考 https://www.cnblogs.com/yaowen/p/10103196.html
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class LoopPrintlnAtomicInteger {
    public static void main(String[] args) throws InterruptedException {
        // A打印3次
        new PrintTask("Thread-A", 3).start();
        //TimeUnit.MILLISECONDS.sleep(500);
        // B打印2次
        new PrintTask("Thread-B", 2).start();
        //TimeUnit.MILLISECONDS.sleep(500);
        // C打印1次
        new PrintTask("Thread-C", 1).start();
    }

    static class PrintTask extends Thread {
        // 打印次数
        private int count;

        private final String str[] = {"A", "B", "C"};
        private final static AtomicInteger atomCount = new AtomicInteger();

        public PrintTask(String threadName, int count) {
            this.setName(threadName);
            this.count = count;
        }

        @Override
        public void run() {
            while (true) {
                // 循环满2轮退出打印
                if (atomCount.get() / 3 == 2) {
                    break;
                }
                synchronized (atomCount) {
                    // 顺序打印A、B、C
                    if (str[atomCount.get() % 3].equals(getName())) {
                        // 自增
                        atomCount.getAndIncrement();
                        // 对应打印几次
                        for (int i = 0; i < count; i++) {
                            System.out.println(getName());
                        }
                        // 表示一轮打印结束，方便观察打印下分隔符
                        if ("C".equals(getName())) {
                            System.out.println("================================");
                        }
                        // 当前线程打印打印完成后唤醒其它线程
                        atomCount.notifyAll();
                    } else {
                        // 非顺序线程 wait()
                        try {
                            atomCount.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}