package com.study.test.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 直接使用 Unsafe 类还是有诸多不便之处，因此 lock 包提供了一个辅助类 LockSupport 封装了 park()、unpark()
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年3月11日
 */
public class LockSupportTest01 {
    public static void main(String[] args) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("thread-1");
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " before park");
                // park 100 seconds
                LockSupport.parkNanos(false, TimeUnit.NANOSECONDS.convert(100, TimeUnit.SECONDS));
                System.out.println(threadName + " after park");
            }
        };

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("thread-2");
                String threadName = Thread.currentThread().getName();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadName + " unpark thread-1");
                LockSupport.unpark(thread1);
            }
        };

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("thread-3");
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " park 5 seconds");
                // park 5 seconds
                LockSupport.parkUntil(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS));
                System.out.println(threadName + " after park");
            }
        };

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
