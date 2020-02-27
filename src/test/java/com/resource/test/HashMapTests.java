package com.resource.test;

public class HashMapTests {
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * Returns a power of two size for the given target capacity.
     * 该函数的功能：调整 HashMap 的容量，结果为2的整数幂且大于 cap 的最小整数。
     * 测试该方法：该算法的作用让最高位的1后面的位全变为1，然后再加1，最后得到的就是2的n次幂
     */
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        /**
         * 5 的二进制形式：0101
         * 最高位1后面的位全变为1得：0111
         * 再加1得：1000，即：8
         */
        int size = tableSizeFor(5);
        System.out.println(size);
    }
}
