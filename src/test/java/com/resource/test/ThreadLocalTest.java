package com.resource.test;

import org.junit.Test;

public class ThreadLocalTest {
    static class MyThread extends Thread {
        private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        @Override
        public void run() {
            super.run();
            for (int i = 1; i < 6; i++) {
                threadLocal.set(i);
                Integer val = threadLocal.get();
                System.out.println(this.getName() + ": threadLocal.get() = " + val);
            }
        }
    }

    @Test
    public void test() {
        MyThread threadA = new MyThread();
        threadA.setName("ThreadA");
        threadA.start();
        MyThread threadB = new MyThread();
        threadB.setName("ThreadB");
        threadB.start();
    }
}
