package com.study.test.number;

import org.junit.Test;

/**
 * 作用描述：TODO
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年12月03日
 */
public class FloatTests {
    @Test
    public void testFloat01() {
        Float f = new Float(-1.0 / 0.0);
        Float f0 = new Float(0.0 / 0.0);
        Float f1 = new Float(1.0 / 0.0);
        System.out.println("f=" + f + "，Float.isNaN(f)=" + Float.isNaN(f) + "，f.isNaN()=" + f.isNaN());
        System.out.println("f0=" + f0 + "，Float.isNaN(f0)=" + Float.isNaN(f0) + "，f0.isNaN()=" + f0.isNaN());
        System.out.println("f1=" + f1 + "，Float.isNaN(f1)=" + Float.isNaN(f1) + "，f1.isNaN()=" + f1.isNaN());
    }

    @Test
    public void testFloat02() {
        Float f11 = 1.0f;
        Float f12 = 1.0f;
        System.out.println(f11 == f12);
        System.out.println(f11 != f12);
        System.out.println(f11.equals(f12));

        System.out.println("==========");

        Float f21 = new Float(0.0f / 0.0f);
        Float f22 = new Float(0.0f / 0.0f);
        System.out.println(f21 == f22);
        System.out.println(f21 != f22);
        System.out.println(f21.equals(f22));

        System.out.println("==========");

        System.out.println((0.0f / 0.0) == (0.0f / 0.0));
        System.out.println((0.0f / 0.0) != (0.0f / 0.0));
    }
}
