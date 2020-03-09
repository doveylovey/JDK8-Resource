package com.resource.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 参考 https://www.jianshu.com/p/ae25eb3cfb5d
 * <p>
 * CAS 即 Compare And Swap，中文的意思是“比较并替换”。
 * CAS 机制中使用了3个基本操作数：内存地址V，旧的预期值A，要修改的新值B。
 * 更新一个变量时，只有当变量的预期值A和内存地址V中的实际值相同时，才会将内存地址V对应的值修改为B。
 */
public class CasTests {
    static class CasTest01 {
        private static int count = 0;

        public static void main(String[] args) {
            for (int i = 0; i < 2; i++) {
                new Thread(() -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < 100; j++) {
                        // 每个线程让 count 自增 100。注意：如果不使用 synchronized 同步块，则结果很可能小于200，因为线程不安全
                        synchronized (CasTest01.class) {
                            count++;
                        }
                    }
                }).start();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(count);
        }
    }

    /**
     * CAS 即 Compare And Swap，中文的意思是“比较并替换”。
     * CAS 机制中使用了3个基本操作数：内存地址V，旧的预期值A，要修改的新值B。
     * 更新一个变量时，只有当变量的预期值A和内存地址V中的实际值相同时，才会将内存地址V对应的值修改为B。
     */
    static class CasTest02 {
        private static AtomicInteger count = new AtomicInteger(0);

        public static void main(String[] args) {
            for (int i = 0; i < 2; i++) {
                new Thread(() -> {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < 100; j++) {
                        // 每个线程让 count 自增 100
                        count.incrementAndGet();
                        //count.getAndIncrement();
                    }
                }).start();
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(count);
        }
    }
}
