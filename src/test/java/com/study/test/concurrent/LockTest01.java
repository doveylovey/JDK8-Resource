package com.study.test.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock 示例
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class LockTest01 {
    public static void main(String[] args) {
        //TicketNoLock ticket = new TicketNoLock();
        TicketWithLock ticket = new TicketWithLock();
        new Thread(ticket, "【窗口 A】").start();
        new Thread(ticket, "【窗口 B】").start();
        new Thread(ticket, "【窗口 C】").start();
    }

    static class TicketNoLock implements Runnable {
        private int tick = 100;

        public void run() {
            while (true) {
                if (tick > 0) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread().getName() + "完成售票，余票数：" + (--tick));
                }
            }
        }
    }

    // 使用 Lock
    static class TicketWithLock implements Runnable {
        private int tick = 100;
        private Lock lock = new ReentrantLock();

        public void run() {
            while (true) {
                // 上锁
                lock.lock();
                try {
                    if (tick > 0) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                        }
                        System.out.println(Thread.currentThread().getName() + "完成售票，余票数：" + --tick);
                    }
                } finally {
                    // 释放锁
                    lock.unlock();
                }
            }
        }
    }
}
