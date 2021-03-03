package com.study.test.gc;

/**
 * 新生代 MInor GC：通过此例可以分析 JVM 的内存分配和回收策略：对象优先在 Eden 分配。
 * 虚拟机参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 * 参数解释：在运行时通过 -Xms20M、-Xmx20M、-Xmn10M 这三个参数限制了 Java 堆的大小为 20M，不可扩展，其中 10MB 分配给新生代，剩下的 10MB 分配给老年代。
 * -XX:SurvivorRatio=8 决定了新生代的中 Eden 区和2个 Survivor 区的空间比例为 8:1:1。
 */
public class AllocationAtEden {
    private static final int _1MB = 1024 * 1024;

    public static void testAllocation() {
        byte[] allocation1, allocation2, allocation3, allocation4;
        allocation1 = new byte[2 * _1MB];
        allocation2 = new byte[2 * _1MB];
        allocation3 = new byte[2 * _1MB];
        // 出现一次 Minor GC
        allocation4 = new byte[4 * _1MB];
    }

    public static void main(String[] args) {
        testAllocation();
    }
}
