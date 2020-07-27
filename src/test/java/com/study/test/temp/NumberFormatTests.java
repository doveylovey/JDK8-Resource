package com.study.test.temp;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * NumberFormat：
 * NumberFormat 是所有数值格式的抽象基类，此类提供格式化和解析数值的接口。
 * NumberFormat 还提供了一些方法来确定哪些语言环境具有数值格式，以及它们的名称是什么。
 * NumberFormat 可用于格式化和解析任何语言环境的数值。使代码能够完全独立于小数点、千位分隔符甚至所用特定小数位数的语言环境约定，并与数值格式是否为偶小数无关。
 * <p>
 * 使用 getInstance() 或 getNumberInstance() 获取正常的数字格式。
 * 使用 getIntegerInstance() 得到的整数格式。
 * 使用 getCurrencyInstance() 来获取货币数字格式。
 * 使用 getPercentInstance() 获取显示百分比的格式。
 * <p>
 * 数值格式化：
 * 1、getInstance()、getNumberInstance()：返回当前默认语言环境的通用数值格式。
 * 2、getInstance(Locale)、getNumberInstance(Locale)：返回指定语言环境的通用数值格式。
 * 3、NumberFormat.setMinimumIntegerDigits(int)：设置整数部分所允许的最小位数。
 * 4、NumberFormat.setMaximumIntegerDigits(int)：设置整数部分所允许的最大位数。
 * 5、NumberFormat.setMinimumFractionDigits(int)：设置小数部分所允许的最小位数。
 * 6、NumberFormat.setMaximumFractionDigits(int)：设置小数部分所允许的最大位数。
 */
public class NumberFormatTests {
    @Test
    public void numberFormatTest() {
        double d1 = 12345.676688000;
        NumberFormat nf = NumberFormat.getNumberInstance();
        System.out.println("默认只保留到小数点后三位：" + nf.format(d1));
        nf.setMinimumIntegerDigits(7);
        System.out.println("设置整数部分最小2位：" + nf.format(d1));
        nf.setMaximumIntegerDigits(3);
        System.out.println("设置整数部分最大3位：" + nf.format(d1));

        double d2 = 12345.6766;
        nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        System.out.println("小数部分大于设置的1位，则按默认的3位：" + nf.format(d2));
        nf.setMinimumFractionDigits(5);
        System.out.println("小数部分不足设置的5位，则补0：" + nf.format(d2));
        nf.setMaximumFractionDigits(1);
        System.out.println("设置小数部分最大1位：" + nf.format(d2));

        double d3 = 12345.6789;
        nf = NumberFormat.getNumberInstance(Locale.US);
        System.out.println("设置Locale.US：" + nf.format(d3));
        nf = NumberFormat.getNumberInstance(Locale.FRANCE);
        System.out.println("设置Locale.FRANCE：" + nf.format(d3));
    }

    @Test
    public void decimalFormatTest01() {
        double d = 123456.7891;
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
        // 保留小数点后面三位，不足的补0，整数部分每隔4位用","隔开
        decimalFormat.applyPattern("#,####.000");
        // 设置舍入模式为 DOWN，默认的是 HALF_EVEN
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        // 设置要格式化的数是正数时，加前缀
        decimalFormat.setPositivePrefix("Prefix  ");
        System.out.println("正数加前缀：" + decimalFormat.format(d));
        // 设置要格式化的数是正数时，加后缀
        decimalFormat.setPositiveSuffix("  Suffix");
        System.out.println("正数加后缀：" + decimalFormat.format(d));
        // 设置整数部分的最大位数
        decimalFormat.setMaximumIntegerDigits(3);
        System.out.println("设置整数部分最大3位：" + decimalFormat.format(d));
        // 设置整数部分的最小位数
        decimalFormat.setMinimumIntegerDigits(10);
        System.out.println("设置整数部分最小10位：" + decimalFormat.format(d));
        // 设置小数部分的最大位数
        decimalFormat.setMaximumFractionDigits(2);
        System.out.println("设置小数部分最大2位：" + decimalFormat.format(d));
        // 设置小数部分的最小位数
        decimalFormat.setMinimumFractionDigits(6);
        System.out.println("设置小数部分最小6位：" + decimalFormat.format(d));
    }

    /**
     * DecimalFormat 数字格式化：
     * 1、"0" 表示1位数值，如果没有则显示0，如 "0000.0000"，整数位或小数位 >4 时按实际输出，<4 时整数位前面补0小数位后面补0，凑足4位。
     * 2、"#" 表示任意位数的整数。如果没有则不显示。在小数点位使用，只表示一位小数，超出部分四舍五入。如："#"——无小数，小数部分四舍五入；".#"——整数部分不变，1位小数，四舍五入；".##"——整数部分不变，2位小数，四舍五入。
     * 3、"." 表示小数点。
     * 4、"," 表示逗号，与模式 "0" 一起使用。注意 "," 不能放在小数部分。
     */
    @Test
    public void decimalFormatTest02() {
        double d1 = 123456.36987, d2 = 12.3698;
        DecimalFormat nf = new DecimalFormat("0000.000");
        System.out.println("d1=" + nf.format(d1) + " d2=" + nf.format(d2));
        nf = new DecimalFormat("#");
        System.out.println("d1=" + nf.format(d1));
        nf = new DecimalFormat(".####");
        System.out.println("d1=" + nf.format(d1));
        nf = new DecimalFormat("0000,0000.00000");
        System.out.println("d1=" + nf.format(d1));
    }

    /**
     * 百分比格式化：
     * getPercentInstance()：静态方法，创建一个 NumberFormat 类的对象并返回其引用。该对象指定百分比格式为系统预设格式。
     * getPercentInstance(Locale)：静态方法，创建一个 NumberFormat 类的对象并返回引用。该对象的百分比格式由 Locale 来指定。
     */
    @Test
    public void percentFormatTest02() {
        double d1 = 123456.36987;
        // 按系统预设百分比格式输出
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        // 设置小数部分最小位数为2
        percentFormat.setMinimumFractionDigits(2);
        // 设置小数部分最大位数为4
        percentFormat.setMaximumFractionDigits(4);
        System.out.println(percentFormat.format(d1));

        // 按指定百分比格式输出
        percentFormat = NumberFormat.getPercentInstance(Locale.US);
        System.out.println(percentFormat.format(d1));
    }

    /**
     * 货币格式
     */
    @Test
    public void currencyFormatTest02() {
        double d1 = 123456.3698700001;
        // 按系统预设货币格式输出
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        // 设置小数部分最小位数为2
        //currencyFormat.setMinimumFractionDigits(2);
        // 设置小数部分最大位数为4
        //currencyFormat.setMaximumFractionDigits(4);
        System.out.println(currencyFormat.format(d1));

        // 按指定货币格式输出
        currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        System.out.println(currencyFormat.format(d1));
    }

    @Test
    public void bigDecimal2Percent() {
        DecimalFormat df = new DecimalFormat("0.00%");
        BigDecimal d = new BigDecimal("0.666");
        System.out.println(df.format(d));
    }
}
