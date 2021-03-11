package com.study.test.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * LockSupport 还可以用来给线程设置一个 Blocker 对象，便于调试和检测线程，其原理是使用 Unsafe 的
 * putObject() 直接设置 Thread 对象的 parkBlocker 属性，并在合适的时候读取这个 Blocker 对象
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年3月11日
 */
public class LockSupportTest02 {
    public static void main(String[] args) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("thread-1");
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " park 5 seconds");
                // park 5 seconds
                Object blocker = "blocker";
                LockSupport.parkUntil(blocker, System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS));
                System.out.println(threadName + " after park");
            }
        };
        thread1.start();

        try {
            Object blocker = null;
            while (blocker == null) {
                blocker = LockSupport.getBlocker(thread1);
                TimeUnit.MILLISECONDS.sleep(10);
            }
            System.out.println("blocker：" + blocker);
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
