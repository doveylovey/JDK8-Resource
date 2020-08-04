package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：守护线程。在 Java 中有两种线程：用户线程、守护线程。
 * 守护线程是一种特殊的线程，它的特性有 “陪伴” 的含义。当进程中不存在任何非守护线程的时候，守护线程就会自动销毁。
 * 典型的守护线程就是垃圾回收线程，当进程中没有非守护线程了，则垃圾回收线程也就没有存在的必要了，会自动销毁。
 * <p>
 * 用个通俗的例子来解释下守护线程：
 * 任何一个守护线程都是整个 JVM 中所有非守护线程的 “保姆”，只要当前 JVM 实例中存在一个非守护线程没有结束，
 * 守护线程就会一直工作，只有当最后一个非守护线程结束时，守护线程才随着 JVM 一同结束工作。
 * Daemon 的作用是为其他线程的运行提供便利服务，守护线程最典型的应用就是 GC(垃圾回收器)，它就是一个很称职的守护者。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月04日
 */
public class ThreadTest11 {
    class MyDaemonThread extends Thread {
        private int i = 0;

        @Override
        public void run() {
            try {
                while (true) {
                    i++;
                    System.out.println("i=" + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMyInheritThread01() throws IOException, InterruptedException {
        MyDaemonThread myDaemonThread = new MyDaemonThread();
        myDaemonThread.setDaemon(true);
        myDaemonThread.start();
        Thread.sleep(5000);
        System.out.println("我离开后，MyDaemonThread 线程也就不会再有输出打印了，也就是停止了！");
        //System.in.read();
    }
}
