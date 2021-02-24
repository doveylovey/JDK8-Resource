package com.study.test.string;

import org.junit.Test;

import java.util.StringJoiner;
import java.util.stream.IntStream;

/**
 * StringBuilder、StringJoiner 类的测试用例
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月24日
 */
public class StringTest03 {
    @Test
    public void test01() {
        String[] names = {"Bob", "Alice", "Grace"};
        StringBuilder sb = new StringBuilder();
        sb.append("Hello ");
        for (String name : names) {
            sb.append(name).append(", ");
        }
        // 注意去掉最后的", "
        sb.delete(sb.length() - 2, sb.length());
        sb.append("!");
        System.out.println(sb.toString());
    }

    @Test
    public void test02() {
        String[] array1 = {"Bob", "Alice", "Grace"};
        StringJoiner joiner1 = new StringJoiner(", ");
        for (String name : array1) {
            joiner1.add(name);
        }
        System.out.println(joiner1.toString());


        String[] array2 = {"Bob", "Alice", "Grace"};
        StringJoiner joiner2 = new StringJoiner(", ", "Hello ", "!");
        for (String name : array2) {
            joiner2.add(name);
        }
        System.out.println(joiner2.toString());
    }

    @Test
    public void test03() {
        StringJoiner joiner10 = new StringJoiner(",");
        IntStream.range(1, 10).forEach(i -> joiner10.add(i + ""));
        System.out.println("仅带分隔符：" + joiner10.toString());

        StringJoiner joiner20 = new StringJoiner(",", "开始【", "】结束");
        IntStream.range(1, 10).forEach(i -> joiner20.add(i + ""));
        System.out.println("带分隔符、前后缀：" + joiner20.toString());
    }

    @Test
    public void test04() {
        String[] array = {"Bob", "Alice", "Grace"};
        // String 提供了一个静态方法 join()，这个方法在内部使用了 StringJoiner 来拼接字符串，在不需要指定“开头”和“结尾”的时候，用 String.join() 更方便
        String result = String.join(", ", array);
        System.out.println(result);
    }

    @Test
    public void test05() {
        // 使用 StringJoiner 构造一个 SELECT 语句
        String[] fields = {"name", "position", "salary"};
        String table = "employee";
        StringJoiner joiner = new StringJoiner(", ", "SELECT ", " FROM " + table);
        for (String field : fields) {
            joiner.add(field);
        }
        System.out.println(joiner.toString());
        String targetSql = "SELECT name, position, salary FROM employee";
        System.out.println(targetSql.equals(joiner.toString()) ? "测试成功" : "测试失败");
    }
}
