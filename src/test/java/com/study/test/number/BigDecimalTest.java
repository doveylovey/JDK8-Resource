package com.study.test.number;

import org.junit.Test;

import java.math.BigDecimal;

/**
 * BigDecimal 常用方法：加法 add()、减法 subtract()、乘法 multiply ()、除法 divide()、绝对值 abs() 等
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年01月20日
 */
public class BigDecimalTest {
    @Test
    public void test01() {
        // 参考 https://blog.csdn.net/haiyinshushe/article/details/82721234
        // BigDecimal 有两种初始化形式：第一种用数字来表示 value，第二种用 string 来表示 value。尽量用字符串的形式初始化
        BigDecimal num1 = new BigDecimal(0.005);
        BigDecimal num2 = new BigDecimal(1000000);
        BigDecimal num3 = new BigDecimal(-1000000);
        // 尽量用字符串的形式初始化
        BigDecimal num12 = new BigDecimal("0.005");
        BigDecimal num22 = new BigDecimal("1000000");
        BigDecimal num32 = new BigDecimal("-1000000");

        // 加法
        BigDecimal result1 = num1.add(num2);
        BigDecimal result12 = num12.add(num22);
        System.out.println("加法用value结果：" + result1);
        System.out.println("加法用string结果：" + result12);

        // 减法
        BigDecimal result2 = num1.subtract(num2);
        BigDecimal result22 = num12.subtract(num22);
        System.out.println("减法value结果：" + result2);
        System.out.println("减法用string结果：" + result22);

        // 乘法
        BigDecimal result3 = num1.multiply(num2);
        BigDecimal result32 = num12.multiply(num22);
        System.out.println("乘法用value结果：" + result3);
        System.out.println("乘法用string结果：" + result32);

        // 除法：使用 divide() 函数时要设置各种参数(如：精确的小数位数、舍入模式)，不然会出现报错。
        // divide(BigDecimal divisor, int scale, int roundingMode) 方法中参数含义为：除数，精确小数位，舍入模式。
        // BigDecimal 的 8 种舍入模式解释如下：
        // java.math.BigDecimal.ROUND_UP：舍入远离零的舍入模式。在丢弃非零部分之前始终增加数字(始终对非零舍弃部分前面的数字加1)。注意，此舍入模式始终不会减少计算值的大小。
        // java.math.BigDecimal.ROUND_DOWN：接近零的舍入模式。在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1，即截短)。注意，此舍入模式始终不会增加计算值的大小。
        // java.math.BigDecimal.ROUND_CEILING：接近正无穷大的舍入模式。如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同；如果为负，则舍入行为与 ROUND_DOWN 相同。注意，此舍入模式始终不会减少计算值。
        // java.math.BigDecimal.ROUND_FLOOR：接近负无穷大的舍入模式。如果 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同；如果为负，则舍入行为与 ROUND_UP 相同。注意，此舍入模式始终不会增加计算值。
        // java.math.BigDecimal.ROUND_HALF_UP：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同；否则舍入行为与 ROUND_DOWN 相同。注意，这是我们大多数人在小学时就学过的舍入模式(四舍五入)。
        // java.math.BigDecimal.ROUND_HALF_DOWN：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为上舍入的舍入模式。如果舍弃部分 > 0.5，则舍入行为与 ROUND_UP 相同；否则舍入行为与 ROUND_DOWN 相同(五舍六入)。
        // java.math.BigDecimal.ROUND_HALF_EVEN：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同；如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
        // ROUND_HALF_EVEN 舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。如果前一位为奇数，则入位，否则舍去。以下例子为保留小数点1位，那么这种舍入方式下的结果：1.15>1.2、1.25>1.2
        // java.math.BigDecimal.ROUND_UNNECESSARY：断言请求的操作具有精确的结果，因此不需要舍入。如果对获得精确结果的操作指定此舍入模式，则抛出 ArithmeticException。
        BigDecimal result5 = num2.divide(num1, 20, BigDecimal.ROUND_HALF_UP);
        BigDecimal result52 = num22.divide(num12, 20, BigDecimal.ROUND_HALF_UP);
        System.out.println("除法用value结果：" + result5);
        System.out.println("除法用string结果：" + result52);

        // 绝对值
        BigDecimal result4 = num3.abs();
        BigDecimal result42 = num32.abs();
        System.out.println("绝对值用value结果：" + result4);
        System.out.println("绝对值用string结果：" + result42);
    }

    @Test
    public void test02() {
        // java.math.BigDecimal.ROUND_HALF_EVEN：向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同；如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
        // ROUND_HALF_EVEN 舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。如果前一位为奇数，则入位，否则舍去。以下例子为保留小数点1位，那么这种舍入方式下的结果：1.15>1.2、1.25>1.2
        // “银行家舍入法”即四舍六入五考虑，五后非零就进一，五后为零看奇偶，五前为偶应舍去，五前为奇要进一。
        BigDecimal t1 = new BigDecimal("1.15");
        System.out.println(t1.toPlainString() + " ===> " + t1.setScale(1, BigDecimal.ROUND_HALF_EVEN));
        BigDecimal t2 = new BigDecimal("1.25");
        System.out.println(t2.toPlainString() + " ===> " + t2.setScale(1, BigDecimal.ROUND_HALF_EVEN));
        BigDecimal t11 = new BigDecimal("1.151");
        System.out.println(t11.toPlainString() + " ===> " + t11.setScale(1, BigDecimal.ROUND_HALF_EVEN));
        BigDecimal t22 = new BigDecimal("1.252");
        System.out.println(t22.toPlainString() + " ===> " + t22.setScale(1, BigDecimal.ROUND_HALF_EVEN));

        System.out.println("------------------------------------");

        // 如果第2位是奇数，则做 ROUND_HALF_UP
        BigDecimal p1 = new BigDecimal("31.1150");
        System.out.println(p1.toPlainString() + " ===> " + p1.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        // 如果第2位是偶数，则做 ROUND_HALF_DOWN
        BigDecimal p2 = new BigDecimal("31.1250");
        System.out.println(p2.toPlainString() + " ===> " + p2.setScale(2, BigDecimal.ROUND_HALF_EVEN));
    }

    @Test
    public void test03() {
        BigDecimal src = new BigDecimal("1000.05");

        BigDecimal left = src.movePointLeft(2);
        System.out.println("小数点左移：" + left.toString());
        System.out.println("小数点左移：" + left.toPlainString());
        System.out.println("小数点左移：" + left.toEngineeringString());

        BigDecimal right = src.movePointRight(2);
        System.out.println("小数点右移：" + right.toString());
        System.out.println("小数点右移：" + right.toPlainString());
        System.out.println("小数点右移：" + right.toEngineeringString());
    }
}
