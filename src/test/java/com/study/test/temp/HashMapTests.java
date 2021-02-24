package com.study.test.temp;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
         * 例如：
         * 5(即：0101) ===> 0111 ===> 1000(即：8)
         * 27(即：11011) ===> 11111 ===> 100000(即：32)
         * 53(即：110101) ===> 111111 ===> 1000000(即：64)
         */
        int size5 = tableSizeFor(5);
        System.out.println("5 =====> " + size5);
        int size27 = tableSizeFor(27);
        System.out.println("27 =====> " + size27);
        int size53 = tableSizeFor(53);
        System.out.println("53 =====> " + size53);
    }

    @Test
    public void test01() {
        Map<String, Object> hashMap = new HashMap();
        hashMap.put("a", "12345");
        hashMap.put("a", true);
        hashMap.put("b", BigDecimal.ZERO);
        hashMap.forEach((key, value) -> System.out.println("key: " + key + ", value: " + value));
    }

    @Test
    public void test02() {
        Map<String, Object> hashtable = new Hashtable<>();
        hashtable.put(null, 12345);
        //hashtable.put("null", null);
        hashtable.forEach((key, value) -> System.out.println("key: " + key + ", value: " + value));
    }

    @Test
    public void test03() {
        Map<String, String> hashMap = new HashMap();
        String old = hashMap.put("a", "old");
        System.out.println(old);
        old = hashMap.put("a", "new");
        System.out.println(old);
        old = hashMap.put("b", "old");
        System.out.println(old);
        old = hashMap.put("b", "new");
        System.out.println(old);
    }
}