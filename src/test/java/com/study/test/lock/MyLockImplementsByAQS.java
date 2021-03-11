package com.study.test.lock;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 使用 AQS 手写排它锁，该例子实现了一个最简单的排它锁
 * 当有线程获得锁时，其他线程只能等待；当这个线程释放锁时，其他线程可以竞争获取锁
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年3月11日
 */
public class MyLockImplementsByAQS {
    public static void main(String[] args) {
        MyLockImplementsByAQS myLock = new MyLockImplementsByAQS();
        boolean lock = myLock.lock();
        System.out.println("解锁结果：" + lock);

        boolean unlock = myLock.unlock();
        System.out.println("解锁结果：" + unlock);
    }

    private final MySync mySync = new MySync();

    public boolean lock() {
        boolean acquire = mySync.tryAcquire(1);
        return acquire;
    }

    public boolean unlock() {
        boolean release = mySync.tryRelease(0);
        return release;
    }

    public static class MySync extends AbstractQueuedSynchronizer {
        protected MySync() {
            super();
        }

        @Override
        protected boolean tryAcquire(int arg) {
            boolean state = compareAndSetState(0, 1);
            return state;
        }

        @Override
        protected boolean tryRelease(int arg) {
            setState(0);
            return true;
        }
    }
}
