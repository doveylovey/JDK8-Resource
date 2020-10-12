package com.study.test.temp;

import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Optional 测试类
 */
public class OptionalTests {
    @Test
    public void testOf() {
        // of()：为非 null 值创建一个 Optional。of() 方法通过工厂方法创建 Optional 类
        // 注意：创建对象时传入的参数不能为 null，如果传入参数为 null 则抛 NullPointerException 异常
        Optional<String> optional1 = Optional.of("test");
        System.out.println(optional1);
        Optional<Object> optional2 = Optional.of(null);
        System.out.println(optional2);
    }

    @Test
    public void testOfNullable() {
        // ofNullable()：为指定值创建一个 Optional，如果指定的值为 null 则返回一个空的 Optional
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1);
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2);
    }

    @Test
    public void testIsPresent() {
        // isPresent()：存在值则返回 true，否则返回 false
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.isPresent());
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.isPresent());
    }

    @Test
    public void testGet() {
        // get()：存在值则返回该值，否则抛出 NoSuchElementException
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.get());
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.get());
    }

    @Test
    public void testIfPresent() {
        // ifPresent()：存在值则调用 consumer 处理，否则不处理
        Optional<String> optional1 = Optional.ofNullable("test");
        optional1.ifPresent(s -> System.out.println(s));
        Optional<Object> optional2 = Optional.ofNullable(null);
        optional2.ifPresent(s -> System.out.println(s));
    }

    @Test
    public void testOrElse() {
        // orElse()：存在值则返回，否则返回指定的其它值
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.orElse("值1不存在！"));
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.orElse("值2不存在！"));
    }

    @Test
    public void testOrElseGet() {
        // orElseGet()：orElseGet() 与 orElse() 类似，区别在于得到的默认值。
        // orElse() 将传入的字符串作为默认值，orElseGet() 可以接受 Supplier 接口的实现用来生成默认值
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.orElseGet(() -> "值1不存在！"));
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.orElseGet(() -> "值2不存在！"));
    }

    class Product implements Serializable {
        private String name;
        private BigDecimal price;

        public Product(String name, BigDecimal price) {
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return "name=" + name + ", price=" + price;
        }
    }

    public Product createProduct() {
        System.out.println("调用 createProduct() 创建 Product 对象");
        return new Product("文具盒", BigDecimal.TEN);
    }

    @Test
    public void testOrElseGet2() {
        // 注意：orElse() 和 orElseGet() 看似差不多，其实有很大不同：
        // 当 Optional 为空时，orElse() 和 orElseGet() 区别不大；
        // 当 Optional 有值时，orElse() 仍会调用方法创建对象，而 orElseGet() 不会再调用方法；
        // 在处理的业务数据量很大时，两者的性能存在很大差异。
        Product product = null;
        Optional<Product> optional1 = Optional.ofNullable(product);
        System.out.println("orElse()结果：" + optional1.orElse(createProduct()));
        System.out.println("orElseGet()结果：" + optional1.orElseGet(() -> createProduct()));
        System.out.println("==========华丽的分割线==========");
        product = new Product("衣服", new BigDecimal("100"));
        Optional<Product> optional2 = Optional.ofNullable(product);
        System.out.println("orElse()结果：" + optional2.orElse(createProduct()));
        System.out.println("orElseGet()结果：" + optional2.orElseGet(() -> createProduct()));
    }


    @Test
    public void testOrElseThrow() {
        // orElseThrow()：如果有值则将其返回，否则抛出 Supplier 接口创建的异常。
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.orElseThrow(() -> new NullPointerException("值1不存在！")));
        Optional<Object> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.orElseThrow(() -> new NullPointerException("值1不存在！")));
    }

    @Test
    public void testMap() {
        // map()：如果有值，则对其执行调用 mapping 函数得到返回值。如果返回值不为 null，则创建包含 mapping 返回值的 Optional 作为 map() 返回值，否则返回空的 Optional
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.map(e -> e.toUpperCase()));
        Optional<String> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.map(e -> e.toUpperCase()));
    }

    @Test
    public void testFlatMap() {
        // flatMap()：如果有值，则对其执行 mapping 函数返回 Optional 类型返回值，否则返回空Optional。
        // 与 map() 不同的是，flatMap() 的返回值必须是 Optional，而 map() 的返回值可以是任意类型 T
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.flatMap(e -> Optional.of(e.toUpperCase())).get());
        Optional<String> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.flatMap(e -> Optional.of(e.toUpperCase())).get());
    }

    @Test
    public void testFilter() {
        // filter()：如果有值且该值与给定的 predicate 匹配，则返回描述该值的 Optional，否则返回空的 Optional
        Optional<String> optional1 = Optional.ofNullable("test");
        System.out.println(optional1.filter(e -> e.contains("t")).get());
        Optional<String> optional2 = Optional.ofNullable(null);
        System.out.println(optional2.filter(e -> e.contains("t")).get());
    }
}
