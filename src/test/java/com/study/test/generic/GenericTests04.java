package com.study.test.generic;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 作用描述：类型擦除。泛型类或者泛型方法中，不接受 8 种基本数据类型，需要使用它们对应的包装类。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月12日
 */
public class GenericTests04 {
    class GenericErasure01<T> {
        T object;

        public GenericErasure01(T object) {
            this.object = object;
        }

        // 如果要在反射中找到 add 对应的 Method，则应该调用 getDeclaredMethod("add",Object.class)，
        // 否则程序会报错，提示没有这个方法，原因就是类型擦除的时候，T 被替换成 Object 类型了。
        public void add(T object) {
        }
    }

    class GenericErasure02<T extends String> {
        T object;

        public GenericErasure02(T object) {
            this.object = object;
        }

        // 如果要在反射中找到 add 对应的 Method，则应该调用 getDeclaredMethod("add",String.class)。
        public void add(T object) {
        }
    }

    /**
     * 通过测试可以得出结论：在泛型类被类型擦除时，若泛型类中的类型参数没有指定上限，如 <T> 则会被转译成普通的 Object 类型，
     * 如果指定了上限，如 <T extends String> 则类型参数就被替换成类型上限。
     */
    @Test
    public void testGenericErasure01() {
        List<String> strList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();
        // 结果为 true，因为 List<String> 和 List<Integer> 在 jvm 中的 Class 都是 List.class，泛型信息被擦除了。
        // 可能有人会问，那么类型 String 和 Integer 怎么办？答案是泛型转译。
        System.out.println(strList.getClass() == intList.getClass());

        System.out.println("=================================================================================================");

        GenericErasure01<String> genericErasure01 = new GenericErasure01<>("hello");
        Class clazz = genericErasure01.getClass();
        // GenericErasure01 是一个泛型类，通过反射可以查看它在运行时的状态信息。
        System.out.println("泛型类 GenericErasure<T> 擦除泛型后的全限定名：" + clazz.getName());
        // Class 的类型仍然是 GenericErasure01 并不是 GenericErasure01<T> 这种形式，那我们再看看泛型类中 T 的类型在 jvm 中是什么具体类型。
        Field[] fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            System.out.println("字段名称：" + f.getName() + "，类型：" + f.getType().getName());
        }

        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("方法名称：" + m.toString());
        }

        System.out.println("=================================================================================================");

        GenericErasure02<String> genericErasure02 = new GenericErasure02<>("hello");
        clazz = genericErasure02.getClass();
        System.out.println("泛型类 GenericErasure<T> 擦除泛型后的全限定名：" + clazz.getName());

        fs = clazz.getDeclaredFields();
        for (Field f : fs) {
            System.out.println("字段名称：" + f.getName() + "，类型：" + f.getType().getName());
        }

        methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            System.out.println("方法名称：" + m.toString());
        }
    }

    /**
     * 类型擦除，是泛型能够与之前的 java 版本代码兼容共存的原因。但也因为类型擦除，它会抹掉很多继承相关的特性，这是它带来的局限性。
     * 理解类型擦除有利于我们绕过开发当中可能遇到的雷区，同样理解类型擦除也能让我们绕过泛型本身的一些限制。
     */
    @Test
    public void testGenericErasureLimited01() {
        List<Integer> list = new ArrayList<>();
        list.add(12345);
        //list.add("String");// 编译都通不过
        try {
            Method method = list.getClass().getDeclaredMethod("add", Object.class);
            // 利用类型擦除的原理，用反射的手段可以绕过正常开发中编译器不允许的操作限制。
            method.invoke(list, "String");
            method.invoke(list, 9.9F);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        list.forEach(System.out::println);
    }

    @Test
    public void testGenericAttention01() {
        // 1、泛型类或者泛型方法中，不接受 8 种基本数据类型，需要使用它们对应的包装类。如下：
        //List<int> intList0 = new ArrayList<>();// 编译都通不过
        List<Integer> intList = new ArrayList<>();

        // 2、对泛型方法的困惑
        // 例如，有个泛型方法：public <T> T test(T t) { return null; }
        // 有人可能会对其中的两个 T 感到困惑，其实 <T> 是为了说明类型参数，是声明，而后面不带尖括号的 T 是方法的返回值类型。
        // 假设 test() 被这样调用：test("123");，那么实际上相当于：public String test(String t);

        // 3、Java 不能创建具体类型的泛型数组。以下两行代码是无法通过编译的，原因还是类型擦除带来的影响。
        //List<Integer>[] intList = new ArrayList<Integer>[];
        //List<Boolean> boolList = new ArrayList<Boolean>[];
        // List<Integer> 和 List<Boolean> 在 jvm 中等同于 List<Object>，所有的类型信息都被擦除，
        // 程序也无法分辨一个数组中的元素类型具体是 List<Integer> 类型还是 List<Boolean> 类型。
        // 但借助于无限定通配符却可以，? 代表未知类型，所以它涉及的操作基本上与类型无关，因此 jvm 不需要针对它对类型作判断，因此它能编译通过，但它只能读，不能写。
        List<?>[] list = new ArrayList<?>[10];
        list[1] = new ArrayList<String>();
        List<?> generic = list[1];
        // 比如，对于局部变量 generic 只能进行 get() 操作，不能进行 add() 操作。
    }
}
