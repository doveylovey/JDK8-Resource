package com.study.test.generic;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 泛型按使用情况可分为 3 种：泛型类、泛型方法、泛型接口。
 * 出于规范的目的，Java 中建议用单个大写字母来代表类型参数，常见的如：T 代表一般的任何类；E 代表 Element 或 Exception；K 代表 Key；V 代表 Value，通常与 K 一起配合使用；S 代表 Subtype。
 */
public class GenericTests02 {
    /**
     * 泛型类。注意：泛型类不仅可以指定一个类型参数，也可以指定多个类型参数。
     * 出于规范的目的，Java 中建议用单个大写字母来代表类型参数，常见的如：
     * T 代表一般的任何类；E 代表 Element 或 Exception；K 代表 Key；V 代表 Value，通常与 K 一起配合使用；S 代表 Subtype。
     */
    class GenericClass<T> {
        // 尖括号 <> 中的 T 被称作是类型参数，用于指代任何类型。实际上 T 只是一种习惯性的写法，如果你愿意甚至可以这样写：public class Test<Hello> { Hello field1; }
        T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    /**
     * 泛型类不仅可以指定一个类型参数，也可以指定多个类型参数。
     */
    class GenericClassMultiple<T, E> {
        T value;
        E element;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public E getElement() {
            return element;
        }

        public void setElement(E element) {
            this.element = element;
        }
    }

    @Test
    public void testGenericClass() {
        // 泛型类的使用：创建泛型类实例时，在尖括号中指定目标类型即可。泛型 T 就会被替换成对应的类型，如 String 或 Integer。
        GenericClass<String> stringGenericClass = new GenericClass<>();
        stringGenericClass.setValue("String");
        String strValue = stringGenericClass.getValue();
        System.out.println("String：" + strValue);

        GenericClass<Integer> integerGenericClass = new GenericClass<>();
        integerGenericClass.setValue(123456);
        Integer intValue = integerGenericClass.getValue();
        System.out.println("Integer：" + intValue);

        System.out.println("====================");

        GenericClassMultiple<String, Map<String, Integer>> genericClassMultiple = new GenericClassMultiple<>();
        genericClassMultiple.setValue("test");
        String value = genericClassMultiple.getValue();
        System.out.println("T -> String：" + value);

        genericClassMultiple.setElement(new HashMap<String, Integer>() {{
            put("admin", 1);
            put("visitor", 10000);
        }});
        Map<String, Integer> element = genericClassMultiple.getElement();
        System.out.println("E -> Map<String, Integer>：" + element);
    }

    /**
     * 泛型方法
     */
    class GenericMethod {
        // 泛型方法与泛型类的不同之处：类型参数是写在返回值前面的。<T> 中的 T 被称为类型参数，而方法中的 T 被称为参数化类型，它不是运行时真正的参数。
        public <T> void testMethod1(T t) {
            System.out.println(t.getClass().getName());
        }

        // 声明的类型参数也可以当作返回值的类型
        public <T> T testMethod2(T t) {
            return t;
        }
    }

    @Test
    public void testGenericMethod() {
        GenericMethod genericMethod = new GenericMethod();
        genericMethod.testMethod1(new StringBuilder());

        String string = genericMethod.testMethod2("泛型方法");
        System.out.println(string);
    }

    /**
     * 泛型类与泛型方法共存
     */
    class GenericClassAndMethod<T> {
        // 普通方法
        public void testMethod1(T t) {
            System.out.println(t.getClass().getName());
        }

        // 泛型方法。注意：泛型类中的类型参数与泛型方法中的类型参数没有一点关系，泛型方法始终以自己定义的类型参数为准。
        public <T> T testMethod2(T t) {
            return t;
        }
    }

    @Test
    public void testGenericClassAndMethod() {
        GenericClassAndMethod<String> stringGenericClassAndMethod = new GenericClassAndMethod<>();
        stringGenericClassAndMethod.testMethod1("普通方法……");

        // 泛型类的实际类型参数是 String，而传递给泛型方法的类型参数是 HashMap<String, Integer>，两者互不相干。
        // 但为了避免混淆，如果一个泛型类中存在泛型方法，那么两者的类型参数最好不要同名。比如可以将 GenericClassAndMethod 类中 testMethod2() 方法的泛型改为 E：public <E> E testMethod2(E e) { return e; }
        HashMap<String, Integer> hashMap = stringGenericClassAndMethod.testMethod2(new HashMap<String, Integer>() {{
            put("admin", 1);
            put("visitor", 10000);
        }});
        System.out.println(hashMap);
    }

    /**
     * 泛型接口：与泛型类差不多。
     */
    interface GenericInterface {
    }
}
