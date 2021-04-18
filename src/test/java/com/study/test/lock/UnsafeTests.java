package com.study.test.lock;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * Unsafe 类的 park() 和 unpark()：
 * public native void park(boolean var1, long var2)：用来阻塞一个线程，第一个参数用来指示后面的参数是绝对时间还是相对时间(true-绝对时间、false-从此刻开始后的相对时间)，调用 park() 的线程就阻塞在此处
 * public native void unpark(Object var1)：用来释放某个线程的阻塞，线程用参数 var1 表示
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年3月11日
 */
public class UnsafeTests {
    private static Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Thread thread1 = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("thread-1");
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " before park");
                // park 100 seconds
                unsafe.park(false, TimeUnit.NANOSECONDS.convert(100, TimeUnit.SECONDS));
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
                unsafe.unpark(thread1);
            }
        };

        Thread thread3 = new Thread() {
            @Override
            public void run() {
                Thread.currentThread().setName("thread-3");
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + " park 5 seconds");
                // park 5 seconds
                unsafe.park(true, System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(5, TimeUnit.SECONDS));
                System.out.println(threadName + " after park");
            }
        };

        thread1.start();
        thread2.start();
        thread3.start();
    }
}
