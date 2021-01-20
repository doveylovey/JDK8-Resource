package com.study.test.temp;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java 正则表达式。参考：
 * https://blog.csdn.net/weixin_43860260/article/details/91417485
 * https://www.jianshu.com/p/3c076c6b2dc8
 * http://51gjie.com/java/758.html
 */
public class RegexTests {
    /**
     * 正则表达式的匹配方式1
     */
    @Test
    public void regexMatch1() {
        String input = "113";
        String regex = "^-?\\d+$";
        boolean result = input.matches(regex);
        System.out.println(result);
    }

    /**
     * 正则表达式的匹配方式2
     */
    @Test
    public void regexMatch2() {
        String input = "113";
        String regex = "^-?\\d+$";
        boolean result = Pattern.matches(regex, input);
        System.out.println(result);
    }

    /**
     * 正则表达式的匹配方式3
     */
    @Test
    public void regexMatch3() {
        String input = "113";
        String regex = "^-?\\d+$";
        // 将给定的正则表达式编译为模式。如果匹配需求较多，且需用同相同的regex去匹配，就可将这句写到静态模块里面，用的时候直接使用实例p
        Pattern pattern = Pattern.compile(regex);
        // 创建一个匹配器，匹配给定的输入与此模式。
        Matcher matcher = pattern.matcher(input);
        boolean result = matcher.matches();
        System.out.println(result);
    }

    /**
     * 正则表达式的应用：替换
     */
    @Test
    public void regexReplace() {
        String input = "12a3B456Cde78";
        String regex = "[a-zA-Z]+";
        String regex2 = "\\d+";
        // 将字符串中英文字母替换为&
        String result1 = input.replaceAll(regex, "&");
        System.out.println(result1);
        // 将字符串中单个数字或连续的数字替换为0
        String result2 = input.replaceAll(regex2, "0");
        System.out.println(result2);
    }

    /**
     * 正则表达式的应用：切割
     */
    @Test
    public void regexSplit() {
        String input = "OneTwoThreeFourFive";
        String regex = "[A-Z]";
        String[] result = input.split(regex);
        List<String> list = Arrays.asList(result);
        list.forEach(System.out::println);
    }

    @Test
    public void volumeFormatter() {
        // 参考 https://blog.csdn.net/liuxuejin/article/details/8643036
        // 销量
        String volume = "100000000";
        // 匹配加号 +
        Pattern add = Pattern.compile("\\+$");
        // 匹配中文
        Pattern chinese = Pattern.compile("[\\u4e00-\\u9fa5]$");
        if (add.matcher(volume).find() || chinese.matcher(volume).find()) {
            System.out.println(volume);
        }
        BigDecimal bigDecimal = new BigDecimal(volume);
        if (bigDecimal.doubleValue() < 10000) {
            System.out.println(volume);
        }
        // 转换为以万为单位的数字(除以10000)，并保留2位小数
        BigDecimal decimal = bigDecimal.divide(new BigDecimal("10000")).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println("格式化结果：" + decimal);
        // 对格式化的结果去除尾部零，再拼接上万字
        String concat = decimal.stripTrailingZeros().toPlainString().concat("万");
        System.out.println(concat);
    }

    @Test
    public void testMatchesAndFind() {
        // 当正则只能匹配字符串中的部分内容，matches() 方法是 false，find() 方法是 true
        // 当正则完全匹配字符串，从头到尾正好匹配上字符串，matches() 方法是 true，find() 方法为 false；
        Pattern pattern1 = Pattern.compile("Hallo");
        Matcher matcher1 = pattern1.matcher("Halloween");
        System.out.println("matches() 部分：" + matcher1.matches());
        System.out.println("find() 部分：" + matcher1.find());

        Pattern pattern2 = Pattern.compile("Hallo");
        Matcher matcher2 = pattern2.matcher("Hallo");
        System.out.println("matches() 全部：" + matcher2.matches());
        System.out.println("find() 全部：" + matcher2.find());
    }

    /**
     * 【附】验证数字的正则表达式集
     * 验证数字：^[0-9]*$
     * 验证n位的数字：^\d{n}$
     * 验证至少 n 位数字：^\d{n,}$
     * 验证 m-n 位的数字：^\d{m,n}$
     * 验证零和非零开头的数字：^(0|[1-9][0-9]*)$
     * 验证有两位小数的正实数：^[0-9]+(.[0-9]{2})?$
     * 验证有 1-3 位小数的正实数：^[0-9]+(.[0-9]{1,3})?$
     * 验证非零的正整数：^\+?[1-9][0-9]*$
     * 验证非零的负整数：^\-[1-9][0-9]*$
     * 验证非负整数(正整数+0)：^\d+$
     * 验证非正整数(负整数+0)：^((-\d+)|(0+))$
     * 验证长度为 3 的字符：^.{3}$
     * 验证由 26 个英文字母组成的字符串：^[A-Za-z]+$
     * 验证由 26 个大写英文字母组成的字符串：^[A-Z]+$
     * 验证由 26 个小写英文字母组成的字符串：^[a-z]+$
     * 验证由数字和 26 个英文字母组成的字符串：^[A-Za-z0-9]+$
     * 验证由数字、26 个英文字母或者下划线组成的字符串：^\w+$
     * 验证用户密码：^[a-zA-Z]\w{5,17}$。正确格式为：以字母开头，长度在 6-18 之间，只能包含字符、数字和下划线。
     * 验证是否含有 ^%&',;=?$\" 等字符：[^%&',;=?$\x22]+
     * 验证汉字：^[\u4e00-\u9fa5],{0,}$
     * 验证 Email 地址：/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
     * 验证 Internet URL：^http://([\w-]+\.)+[\w-]+(/[\w-./?%&=]*)?$；^[a-zA-z]+://(w+(-w+)*)(.(w+(-w+)*))*(?S*)?$
     * 验证电话号码：^(\(\d{3,4}\)|\d{3,4}-)?\d{7,8}$。正确格式为：XXXX-XXXXXXX，XXXX-XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX。
     * 验证身份证号(15 位或 18 位数字)：^\d{15}|\d{}18$
     * 验证一年的 12 个月：^(0?[1-9]|1[0-2])$。正确格式为：“01”-“09”和“1”“12”
     * 验证一个月的 31 天：^((0?[1-9])|((1|2)[0-9])|30|31)$。正确格式为：01、09和1、31。
     * 整数：^-?\d+$
     * 非负浮点数(正浮点数+0)：^\d+(\.\d+)?$
     * 正浮点数：^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$
     * 非正浮点数(负浮点数+0)：^((-\d+(\.\d+)?)|(0+(\.0+)?))$
     * 负浮点数：^(-(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*)))$
     * 浮点数：^(-?\d+)(\.\d+)?$
     */
}
