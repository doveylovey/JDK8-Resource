package com.study.test.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch(闭锁)：一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class CountDownLatchTest01 {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(10);
        CountDownLatchDemo demo = new CountDownLatchDemo(latch);
        long start = System.currentTimeMillis();
        // 创建10个线程
        for (int i = 0; i < 10; i++) {
            new Thread(demo, "线程" + i).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {

        }
        long end = System.currentTimeMillis();
        System.out.println("耗费时间为：" + (end - start));
    }

    static class CountDownLatchDemo implements Runnable {
        private CountDownLatch latch;

        public CountDownLatchDemo(CountDownLatch latch) {
            this.latch = latch;
        }

        public void run() {
            synchronized (this) {
                try {
                    // 打印100以内的偶数
                    for (int i = 0; i < 100; i++) {
                        if (i % 2 == 0) {
                            System.out.println(Thread.currentThread().getName() + " ===》" + i);
                        }
                    }
                } finally {
                    // 线程数量递减
                    latch.countDown();
                }
            }
        }
    }
}