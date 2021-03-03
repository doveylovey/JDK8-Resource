package com.study.test.gc;

/**
 * 引用计数算法。虚拟机参数：-XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintGCDateStamps
 * -XX:+PrintTenuringDistribution -verbose:gc -Xloggc:gc/reference-counting-gc.log
 */
public class ReferenceCountingGC {
    public Object instance = null;
    private static final int _1MB = 1024 * 1024;
    // 这个成员属性的唯一意义就是占点内存，以便能在 GC 日志中看清楚是否被回收过
    private byte[] bigSize = new byte[20 * _1MB];

    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();
        objA.instance = objB;
        objB.instance = objA;

        objA = null;
        objB = null;

        // 假设在这行发生了 GC，objA 和 ojbB 是否被回收
        System.gc();
    }

    // 从 reference-counting-gc.log 日志中可以看出：
    // 虚拟机并没有因为 a 和 b 互相引用就不回收它们，这也说明虚拟机并不是通过引用计数算法来判断对象是否存在引用的
}
