package com.study.test.temp;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常处理中，try、catch、finally 的执行顺序为：如果 try 中没有异常，则顺序为 try、finally，如果 try 中有异常，则顺序为 try、catch、finally。但是当 try、catch、finally 中加入 return 后，就会出现不同情况。请看示例
 * <p>
 * try、catch、finally 中带 return 时的执行顺序总结：
 * 1、finally 中的代码总会被执行。
 * 2、当 try、catch 中有 return 时，也会执行 finally。return 的时候，要注意返回值的类型，是否受到 finally 中代码的影响。
 * 3、当 finally 中有 return 时，会直接在 finally 中退出，导致 try、catch 中的 return 失效。
 */
public class TryCatchFinallyReturnTests {
    public static void main(String[] args) {
        // try 中带有 return：基本类型 int
        System.out.println(TryCatchFinallyReturnTests.test01());
        // try 中带有 return：基本类型的包装类型 Integer
        System.out.println(TryCatchFinallyReturnTests.test01_1());
        // try 中带有 return：引用类型 List
        System.out.println(TryCatchFinallyReturnTests.test02());

        // catch 中带有 return：基本类型 int
        System.out.println(TryCatchFinallyReturnTests.test03());
        // catch 中带有 return：引用类型 List
        System.out.println(TryCatchFinallyReturnTests.test04());

        // finally 中带有 return：基本类型 int
        System.out.println(TryCatchFinallyReturnTests.test05());
        // finally 中带有 return：引用类型 List
        System.out.println(TryCatchFinallyReturnTests.test06());
    }

    /**
     * 当 try 中有 return 时，会先执行 return 前的代码，然后暂时保存需要 return 的信息，再执行 finally 中的代码，
     * 最后再通过 return 返回之前保存的信息，所以这个方法返回的是 try 中计算后的值，而非 finally 中计算后的值。注意，请看 {@link #test02()}
     *
     * @return
     */
    public static int test01() {
        int a = 1;
        try {
            a++;
            System.out.println("【try 中带有 return，int 类型】=====>try：" + a);
            return a;
        } catch (Exception e) {
            a++;
            System.out.println("【try 中带有 return，int 类型】=====>catch：" + a);
        } finally {
            a++;
            System.out.println("【try 中带有 return，int 类型】=====>finally：" + a);
        }
        return a;
    }

    public static Integer test01_1() {
        Integer a = new Integer(129);// 此处注意 a 的值[-128, 127]
        try {
            a++;
            System.out.println("【try 中带有 return，Integer 类型】=====>try：" + a);
            return a;
        } catch (Exception e) {
            a++;
            System.out.println("【try 中带有 return，Integer 类型】=====>catch：" + a);
        } finally {
            a++;
            System.out.println("【try 中带有 return，Integer 类型】=====>finally：" + a);
        }
        return a;
    }

    /**
     * 通过该示例就会发现问题，在 {@link #test01()} 中提到 return 时会临时保存需要返回的信息，不受 finally 中的影响，那为什么这里会有变化？
     * 其实问题出在参数类型上，在 {@link #test01()} 中用的是基本类型，而这里用的是引用类型。
     * List 里存的不是变量本身，而是变量的地址，所以当 finally 通过地址改变了变量，还是会影响方法返回值的。
     *
     * @return
     */
    public static List<String> test02() {
        List<String> list = new ArrayList<>();
        try {
            list.add("try");
            System.out.println("【try 中带有 return，List 类型】=====>try：" + list);
            return list;
        } catch (Exception e) {
            list.add("catch");
            System.out.println("【try 中带有 return，List 类型】=====>catch：" + list);
        } finally {
            list.add("finally");
            System.out.println("【try 中带有 return，List 类型】=====>finally：" + list);
        }
        return list;
    }

    /**
     * catch 中的 return 与 try 中的一样，会先执行 return 前的代码，然后暂时保存需要 return 的信息，再执行 finally 中的代码，最后再通过 return 返回之前保存的信息。
     *
     * @return
     */
    public static int test03() {
        int a = 1;
        try {
            a++;
            System.out.println("【catch 中带有 return，int 类型】=====>try：" + a);
            int result = a / 0;
        } catch (Exception e) {
            a++;
            System.out.println("【catch 中带有 return，int 类型】=====>catch：" + a);
            return a;
        } finally {
            a++;
            System.out.println("【catch 中带有 return，int 类型】=====>finally：" + a);
        }
        return a;
    }

    public static List<String> test04() {
        List<String> list = new ArrayList<>();
        try {
            list.add("try");
            System.out.println("【catch 中带有 return，List 类型】=====>try：" + list);
            int result = 1 / 0;
        } catch (Exception e) {
            list.add("catch");
            System.out.println("【catch 中带有 return，List 类型】=====>catch：" + list);
            return list;
        } finally {
            list.add("finally");
            System.out.println("【catch 中带有 return，List 类型】=====>finally：" + list);
        }
        return list;
    }

    /**
     * 当 finally 中有 return 时，try 中的 return 会失效，在执行完 finally 的 return 后，就不会再执行 try 中的 return。
     * 这种写法，编译是可以通过的，但编译器会给予警告，所以不推荐在 finally 中写 return，这会破坏程序的完整性，而且一旦 finally 里出现异常，会导致 catch 中的异常被覆盖。
     *
     * @return
     */
    public static int test05() {
        int a = 1;
        try {
            a++;
            System.out.println("【finally 中带有 return，int 类型】=====>try：" + a);
            return a;
        } catch (Exception e) {
            a++;
            System.out.println("【finally 中带有 return，int 类型】=====>catch：" + a);
            return a;
        } finally {
            a++;
            System.out.println("【finally 中带有 return，int 类型】=====>finally：" + a);
            return a;
        }
    }

    public static List<String> test06() {
        List<String> list = new ArrayList<>();
        try {
            list.add("try");
            System.out.println("【finally 中带有 return，List 类型】=====>try：" + list);
            return list;
        } catch (Exception e) {
            list.add("catch");
            System.out.println("【finally 中带有 return，List 类型】=====>catch：" + list);
            return list;
        } finally {
            list.add("finally");
            System.out.println("【finally 中带有 return，List 类型】=====>finally：" + list);
            return list;
        }
    }
}
