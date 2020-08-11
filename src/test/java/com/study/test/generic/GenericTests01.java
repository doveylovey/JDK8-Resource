package com.study.test.generic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用描述：Java 泛型。
 * 什么是泛型：泛型就是为了参数化类型，或者说可以将类型当作参数传递给一个类或方法。
 * <p>
 * 使用泛型的好处：
 * 1、与用 Object 代替一切类型的方式相比较而言，泛型使得数据的类别可以像参数一样由外部传递进来，它提供了一种扩展能力，更符合面向抽象开发的软件编程宗旨。
 * 2、当类型确定后，泛型提供了一种类型检测机制，只有匹配类型的数据才能正常赋值，否则编译器就不通过。因此，泛型也是一种类型安全检测机制，一定程度上提高了软件的安全性，防止出现低级错误。
 * 3、泛型提高了程序代码的可读性，不必等到运行时才去强制转换，在定义或实例化阶段时程序员就能够看出代码要操作的数据类型。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月12日
 */
public class GenericTests01 {
    @Test
    public void test01() {
        List<String> strList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        // 输出结果是 true，因为泛型擦除，即 List<String> 和 List<Integer> 在 jvm 中的 Class 都是 List.class，泛型信息被擦除了。
        // 那么类型 String 和 Integer 怎么办？答案是泛型转译。
        System.out.println(strList.getClass() == intList.getClass());
    }

    /**
     * 不使用泛型时，取值时就需要强制转换成需要的类型
     */
    class NotUseGeneric {
        Object value;

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    /**
     * 使用泛型时，取值时无需进行强制转换。使用泛型的好处：
     * 1、与用 Object 代替一切类型的方式相比较而言，泛型使得数据的类别可以像参数一样由外部传递进来，它提供了一种扩展能力，更符合面向抽象开发的软件编程宗旨。
     * 2、当类型确定后，泛型提供了一种类型检测机制，只有匹配类型的数据才能正常赋值，否则编译器就不通过。因此，泛型也是一种类型安全检测机制，一定程度上提高了软件的安全性，防止出现低级错误。
     * 3、泛型提高了程序代码的可读性，不必等到运行时才去强制转换，在定义或实例化阶段时程序员就能够看出代码要操作的数据类型。
     */
    class UseGeneric<T> {
        T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

    @Test
    public void test02() {
        // 不使用泛型时，取值时就需要强制转换成需要的类型
        NotUseGeneric notUseGeneric = new NotUseGeneric();
        notUseGeneric.setValue(123456);
        Integer intValue1 = (Integer) notUseGeneric.getValue();
        notUseGeneric.setValue("string");
        String strValue1 = (String) notUseGeneric.getValue();

        // 使用泛型时，存值时编译器就会检查，取值时也无需强制转换
        UseGeneric<String> useGeneric = new UseGeneric<>();
        // useGeneric.setValue(123456);// 编译都通不过
        useGeneric.setValue("string");
        String strValue2 = useGeneric.getValue();
    }
}
