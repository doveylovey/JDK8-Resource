package com.study.test.gc;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 参考 https://www.cnblogs.com/yjc1605961523/p/12427658.html
 * <p>
 * 设置 VM 参数
 * 1、使用默认的垃圾收集器：-XX:+PrintGCDetails -Xms16m -Xmx16m
 * 结果：PSYoungGen + ParOldGen，即：Parallel Scavenge + Parallel Old 组合
 * 2、使用串行垃圾收集器：-XX:+UseSerialGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 结果：DefNew + Tenured，即：Serial + Serial Old 组合
 * 3、使用并行垃圾收集器(jdk8默认)：-XX:+UseParallelGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 结果：PSYoungGen + ParOldGen，即：Parallel Scavenge + Parallel Old 组合
 * 4、使用 ParNew 进行新生代的垃圾回收：-XX:+UseParNewGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 结果：ParNew + Tenured，即：ParNew + Serial Old 组合
 * 5、使用 CMS 进行垃圾回收：-XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * 6、使用 G1 进行垃圾回收：-XX:+UseG1GC -XX:+PrintGCDetails -Xms16m -Xmx16m
 * <p>
 * 注意：控制台输出的 GC 信息中
 * DefNew —— 表示新生代使用 Serial 垃圾收集器
 * ParNew —— 表示新生代使用 ParNew 垃圾收集器
 * PSYoungGen —— 表示新生代使用 Parallel Scavenge 垃圾收集器
 * Tenured —— 表示老年代使用 Serial Old 收集器
 * ParOldGen —— 表示老年代使用 Parallel Old 收集器
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
