package com.study.test.reference;

import org.junit.Test;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 参考：https://segmentfault.com/a/1190000019953045
 * <p>
 * Predicate 接口是一个谓词型接口，实际上就是一个类似于 boolean 类型的判断接口。
 * Predicate 是一个谓词型接口，其实只是起到一个判断作用。
 * Predicate 通过实现一个 test() 方法做判断。
 */
public class PredicateTests {
    @Test
    public void test01() {
        // 使用 Predicate 接口实现方法，只有一个 test() 方法，传入一个参数，返回一个 boolean 值
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                if (integer > 5) {
                    return true;
                }
                return false;
            }
        };
        System.out.println(predicate.test(6));

        System.out.println("********************");

        // 使用 lambda 表达式
        predicate = (t) -> t > 5;
        System.out.println(predicate.test(1));
        System.out.println("********************");
    }

    @Test
    public void test02() {
        // 将 Predicate 作为 filter 接口，Predicate 起到一个判断的作用
        Predicate<Integer> predicate = new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) {
                if (integer > 5) {
                    return true;
                }
                return false;
            }
        };

        Stream<Integer> stream = Stream.of(1, 23, 3, 4, 5, 56, 6, 6);
        List<Integer> list = stream.filter(predicate).collect(Collectors.toList());
        list.forEach(System.out::println);
        System.out.println("********************");
    }
}
