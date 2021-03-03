package com.study.test.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 设置 VM 参数
 * 使用默认的垃圾收集器：-XX:+PrintGCDetails -Xms16m -Xmx16m
 * 使用串行垃圾收集器：-XX:+UseSerialGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 使用 ParNew 进行新生代的垃圾回收：-XX:+UseParNewGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 使用 CMS 进行垃圾回收：-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 情形5：
 * <p>
 * 注意：控制台输出的 GC 信息中
 * DefNew —— 表示新生代使用 Serial 垃圾收集器，defNew 提供新生代空间信息
 * Tenured —— 表示老年代空间信息
 * ParNew —— 表示使用了 ParNew 收集器的新生代空间信息
 * PSYoungGen —— 表示使用了 Parallel 收集器的新生代空间信息
 * ParOldGen —— 表示使用了 Parallel Old 收集器的老年代空间信息
 */
public class GarbageCollectorTests {
    public static void main(String[] args) throws InterruptedException {
        List<Object> list = new ArrayList<>();
        while (true) {
            int sleep = new Random().nextInt(100);
            if (System.currentTimeMillis() % 2 == 0) {
                list.clear();
            } else {
                for (int i = 0; i < 10000; i++) {
                    Properties properties = new Properties();
                    properties.put("key_" + i, "value_" + System.currentTimeMillis() + i);
                    list.add(properties);
                }
            }
            Thread.sleep(sleep);
        }
    }
}
