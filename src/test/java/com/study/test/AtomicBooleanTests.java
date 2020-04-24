package com.study.test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AtomicBoolean 类用于以原子方式更新 boolean 值。一般情况下，我们使用 AtomicBoolean 高效并发处理“只初始化一次”的功能要求
 */
public class AtomicBooleanTests {
    static class AtomicBooleanTest01 {
        public static volatile boolean initialized = false;

        public void init() {
            if (!initialized) {
                initialized = true;
                // 这里初始化代码....
                System.out.println("volatile 实现初始化逻辑……");
            }
        }
    }

    static class AtomicBooleanTest02 {
        private static AtomicBoolean initialized = new AtomicBoolean(false);

        public void init() {
            if (initialized.compareAndSet(false, true)) {
                // 这里放置初始化代码....
                System.out.println("AtomicBoolean 实现初始化逻辑……");
            }
        }
    }

    static class AtomicBooleanThread implements Runnable {
        private static AtomicBoolean flag = new AtomicBoolean(true);

        @Override
        public void run() {
            System.out.println("Thread: " + Thread.currentThread().getName() + ", flag = " + flag.get());
            if (flag.compareAndSet(true, false)) {
                System.out.println("修改: " + Thread.currentThread().getName() + ", flag = " + flag.get());
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                flag.set(true);
            } else {
                System.out.println("重试: " + Thread.currentThread().getName() + ", flag = " + flag.get());
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                run();
            }
        }

        public static void main(String[] args) {
            AtomicBooleanThread atomicBooleanThread = new AtomicBooleanThread();
            new Thread(atomicBooleanThread).start();
            new Thread(atomicBooleanThread).start();
        }
    }
}
