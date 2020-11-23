package com.study.test.reference;

import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 参考：https://segmentfault.com/a/1190000019953045
 * <p>
 * Function 接口是一个功能型接口，它的作用就是转换，将输入数据转换成另一种形式的输出数据。
 * Function 接口是一个功能型接口，是一个转换数据的作用。
 * Function 接口实现 apply() 方法来做转换。
 */
public class FunctionTests {
    @Test
    public void test01() {
        // 使用 map() 方法，泛型的第一个参数是转换前的类型，第二个参数是转换后的类型
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                // 获取每个字符串的长度并返回
                return s.length();
            }
        };

        Stream<String> stream = Stream.of("aaa", "bbbbb", "ccccccv");
        Stream<Integer> stream1 = stream.map(function);
        stream1.forEach(System.out::println);
        System.out.println("********************");
    }
}
