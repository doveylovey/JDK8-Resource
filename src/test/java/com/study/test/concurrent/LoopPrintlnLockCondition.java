package com.study.test.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 题目：3个线程循环打印ABC，其中A打印3次，B打印2次，C打印1次，循环打印2轮。
 * 解法：ReentrantLock 结合 Condition。
 * 思路：Condition 中的 await() 方法相当于 Object中的 wait() 方法，Condition 中的 signal() 方法相当于 Object 中的 notify() 方法。
 * Condition 强大之处在于：能够更加精细的控制多线程的休眠与唤醒。对于同一个锁可以创建多个 Condition，在不同的情况下使用不同的 Condition。
 * 参考 https://www.cnblogs.com/yaowen/p/10103196.html
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class LoopPrintlnLockCondition {
    public static void main(String[] args) {
        LockDemo demo = new LockDemo();

        new Thread(() -> {
            for (int i = 1; i < 11; i++) {
                demo.loopA(i);
            }
        }, "Thread-A").start();

        new Thread(() -> {
            for (int i = 1; i < 11; i++) {
                demo.loopB(i);
            }
        }, "Thread-B").start();

        new Thread(() -> {
            for (int i = 1; i < 11; i++) {
                demo.loopC(i);
                System.out.println("==================================================");
            }
        }, "Thread-C").start();
    }

    static class LockDemo {
        // 标记当前正在执行的线程：1表示Thread-A打印，2表示Thread-B打印，3表示Thread-C打印
        private int number = 1;

        private Lock lock = new ReentrantLock();
        // 三个线程遵循 Thread-A 唤醒 Thread-B，Thread-B 唤醒 Thread-C，Thread-C 唤醒 Thread-A 的规则
        private Condition condition1 = lock.newCondition();
        private Condition condition2 = lock.newCondition();
        private Condition condition3 = lock.newCondition();

        // loopCount 表示循环第几轮
        // 线程A
        public void loopA(int loopCount) {
            // 上锁
            lock.lock();
            try {
                // 1. 判断
                if (number != 1) {
                    condition1.await();
                }
                // 2. 打印
                for (int i = 1; i <= 2; i++) {
                    System.out.println("第 " + loopCount + " 轮，" + Thread.currentThread().getName() + " 打印：" + i);
                }
                // 3. 唤醒线程B
                number = 2;
                condition2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }

        // 线程B
        public void loopB(int loopCount) {
            // 上锁
            lock.lock();
            try {
                // 1. 判断
                if (number != 2) {
                    condition2.await();
                }
                // 2. 打印
                for (int i = 1; i <= 3; i++) {
                    System.out.println("第 " + loopCount + " 轮，" + Thread.currentThread().getName() + " 打印：" + i);
                }
                // 3. 唤醒线程C
                number = 3;
                condition3.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }

        // 线程C
        public void loopC(int loopCount) {
            // 上锁
            lock.lock();
            try {
                // 1. 判断
                if (number != 3) {
                    condition3.await();
                }
                // 2. 打印
                for (int i = 1; i <= 5; i++) {
                    System.out.println("第 " + loopCount + " 轮，" + Thread.currentThread().getName() + " 打印：" + i);
                }
                // 3. 唤醒线程A
                number = 1;
                condition1.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 释放锁
                lock.unlock();
            }
        }
    }
}
