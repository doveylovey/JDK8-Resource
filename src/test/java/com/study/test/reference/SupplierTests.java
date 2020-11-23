package com.study.test.reference;

import org.junit.Test;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 参考：https://segmentfault.com/a/1190000019953045
 * <p>
 * Supplier 接口是一个供给型的接口，说白了就是一个容器，可以用来存储数据供其他方法使用。
 * Supplier 接口可以理解为一个容器，用于装数据的。
 * Supplier 接口有一个 get() 方法，可以返回值。
 */
public class SupplierTests {
    @Test
    public void test01() {
        // 使用 Supplier 接口实现方法，只有一个 get() 方法，无参数，返回一个值
        // 通过创建一个 Supplier 对象，实现了一个 get 方法，这个方法无参数，返回一个值；
        // 所以，每次使用这个接口的时候都会返回一个值，并且保存在这个接口中，所以说是一个容器。
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                // 返回一个随机值
                return new Random().nextInt();
            }
        };
        System.out.println(supplier.get());

        System.out.println("********************");

        // 使用 lambda 表达式
        supplier = () -> new Random().nextInt();
        System.out.println(supplier.get());
        System.out.println("********************");

        // 使用方法引用
        Supplier<Double> supplier2 = Math::random;
        System.out.println(supplier2.get());
    }

    @Test
    public void test02() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);
        // 返回一个 Optional 对象，Optional 对象有需要 Supplier 接口的方法
        Optional<Integer> first = stream.filter(i -> i > 4).findFirst();

        // orElse()：如果 first 中存在数，就返回这个数，否则就放回传入的数
        System.out.println(first.orElse(1));
        System.out.println(first.orElse(7));

        System.out.println("********************");

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                // 返回一个随机值
                return new Random().nextInt();
            }
        };

        // orElseGet()：如果 first 中存在数，就返回这个数，否则就返回 supplier 返回的值
        System.out.println(first.orElseGet(supplier));
    }
}
