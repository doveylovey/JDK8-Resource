package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：yield() 方法。
 * yield() 方法的作用是放弃当前的 CPU 资源，将它让给其他任务去占用 CPU 执行时间。
 * 但放弃的时间不确定，有可能刚刚放弃，马上就又获得了 CPU 时间片。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月04日
 */
public class ThreadTest09 {
    class MyThread01 extends Thread {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            int count = 0;
            for (int j = 0; j < 50000000; j++) {
                // 注意有无下面这一行代码的耗时差异：有——将 CPU 让给其他资源导致速度变慢；无——独占 CPU 时间片
                Thread.yield();
                count = count + (j + 1);
            }
            long endTime = System.currentTimeMillis();
            System.out.println("耗时：" + (endTime - startTime) + "毫秒！");
        }
    }

    @Test
    public void testMyThread01() throws IOException {
        new MyThread01().start();
        System.in.read();
    }
}