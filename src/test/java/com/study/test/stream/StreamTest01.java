package com.study.test.stream;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest01 {
    @Test
    public void test01() {
        // stream() 为 list 集合创建串行流
        List<Integer> list = Arrays.asList(100, 200, 300, 400, 500);
        Stream<Integer> map = list.stream().map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer t) {
                return t / 2;
            }
        });
        // 遍历数组
        map.forEach(System.out::println);

        // 简化
        Arrays.asList(100, 200, 300, 400, 500).stream().map((i) -> i / 2).forEach(System.out::println);

        Integer num[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        // Stream.of() 管道流：从 123456789 中取奇数偶数，filter() 过滤方法
        // 过滤 转化的过程：归并操作。将转化的数据流放到集合或集合元素中。Collectors返回列表或字符串
        Stream.of(num).filter(n -> n % 2 == 0).forEach(x -> System.out.print(x + " "));
        Stream.of(num).filter(n -> n % 2 != 0).forEach(x -> System.out.print(x + " "));

        // 筛选出所有偶数后放到一个数组中
        Integer[] array = Stream.of(num).filter(n -> n % 2 == 0).toArray(Integer[]::new);
        for (Integer every : array) {
            System.out.println(every);
        }

        // 筛选出所有奇数后放到一个 list 中
        List<Integer> collect = Stream.of(num).filter(n -> n % 2 != 0).collect(Collectors.toList());
        collect.forEach(System.out::println);

        // reduce()：求和、求最大、求最小、聚合操作
        Integer[] nums = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer sum1 = Stream.of(nums).reduce(0, (a, b) -> a + b);
        Integer sum2 = Stream.of(nums).reduce(0, Integer::sum);
        Integer max = Stream.of(nums).reduce(0, Integer::max);
        Integer min = Stream.of(nums).reduce(0, Integer::min);
        System.out.println("和1：" + sum1 + "，和2：" + sum2 + "，最大值：" + max + "，最小值：" + min);

        // flatMap() 扁平化处理
        Stream<List<Integer>> inputStream = Stream.of(Arrays.asList(1, 2, 3), Arrays.asList(4, 5, 6), Arrays.asList(7, 8, 9));
        Stream<Integer> outputStream = inputStream.flatMap(x -> x.stream());
        outputStream.forEach(a -> System.out.print(a + " "));

        // 首先分割用,隔开的字符串，然后将每个字母转换成大写存至 list 容器
        String str = "hello,java,stream,study";
        List<String> stringList = Arrays.asList(str.split(",")).stream().map(String::toUpperCase).collect(Collectors.toList());
        stringList.forEach(System.out::println);
    }
}
