package com.study.test.reference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import java.time.LocalDate;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 方法引用通过一对双冒号 :: 来表示，是函数式接口的另一种书写方式：
 * 1、静态方法引用：通过类名::静态方法名， 如 Integer::parseInt。
 * 2、实例方法引用：通过实例对象::实例方法，如 str::substring。
 * 3、构造方法引用：通过类名::new， 如 User::new。
 * <p>
 * 通过方法引用，可以将方法的引用赋值给一个变量，通过赋值给 Function，说明方法引用也是一种函数式接口的书写方式，
 * Lambda 表达式也是一种函数式接口，Lambda 表达式一般用于自己提供方法体，而方法引用一般直接引用现成的方法。
 */
public class MethodReferenceTests {
    @Test
    public void test01() {
        // 使用双冒号::实现静态方法引用
        Function<String, Integer> fun = Integer::parseInt;
        Integer value = fun.apply("123");
        System.out.println("使用双冒号::实现静态方法引用：" + value);

        // 使用双冒号::实现实例方法引用
        String content = "Hello JDK8";
        Function<Integer, String> func = content::substring;
        String result = func.apply(1);
        System.out.println("使用双冒号::实现实例方法引用：" + result);

        // 使用双冒号::实现构造方法引用
        BiFunction<String, LocalDate, User> biFunction = User::new;
        User user = biFunction.apply("admin", LocalDate.of(1991, 10, 20));
        System.out.println("使用双冒号::实现构造方法引用：" + user.toString());

        // 方法引用也是一种函数式接口，因此也可以将方法引用作为方法的参数
        myFunction(String::toUpperCase, "hello");
    }

    public static void myFunction(Function<String, String> func, String parameter) {
        String result = func.apply(parameter);
        System.out.println(result);
    }
}

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
class User {
    private String username;
    private LocalDate birthday;

    // 现在好像很流行 of() 方法：提供一个 static 方法，方法名为 of()，方法返回值为当前类，并且把构造函数设置为 private，用静态 of() 来代替构造函数。

    public static User of() {
        return new User();
    }

    public static User of(String username, LocalDate birthday) {
        return new User(username, birthday);
    }
}