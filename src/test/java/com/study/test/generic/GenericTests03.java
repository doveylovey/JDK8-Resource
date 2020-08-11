package com.study.test.generic;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 作用描述：泛型通配符。除了用 <T> 表示泛型外，还有 <?> 这种形式，其中 ? 被称为通配符。
 * <p>
 * 通配符的出现是为了指定泛型中的类型范围，通配符有 3 种形式。
 * 1、<?> 被称作无限定的通配符。
 * 2、<? extends T> 被称作有上限的通配符。
 * 3、<? super T> 被称作有下限的通配符。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月12日
 */
public class GenericTests03 {
    class Base {
    }

    class Sub extends Base {
    }

    @Test
    public void testBaseAndSub() {
        // Base 是 Sub 的父类，它们之间是继承关系，所以 Sub 的实例可以给一个 Base 引用赋值
        Sub sub = new Sub();
        Base base = sub;

        List<Sub> subList = new ArrayList<>();
        // List<Base> baseList = subList;// 编译都通不过。原因：虽然 Sub 是 Base 的子类，但不代表 List<Sub> 和 List<Base> 有继承关系。

        // 由于在现实编码中确实有这样的需求，希望泛型能够处理某一范围内的数据类型，比如某个类和它的子类，对此 Java 引入了通配符这个概念。
    }

    class NoLimitWildcard {
        // 无限定通配符：该方法内的参数是被无限定通配符修饰的 Collection 对象，它隐含了一个意图(或者说限定)，
        // 那就是 testWildcard() 方法内部无需关心 Collection 中的真实数据类型，因为它是未知的。因此也就只能调用 Collection 中与类型无关的方法。
        public void testWildcard(Collection<?> collection) {
            // add() 方法与 Collection 中的真实数据类型有关，故以下几行代码编译都通不过
            //collection.add(123);
            //collection.add("wildcard");
            //collection.add(new Object());

            // 以下方法与 Collection 中的真实数据类型无关，所以可以正常通过编译并使用
            collection.iterator().next();
            collection.size();

            // 可以看到，当 <?> 存在时，Collection 对象丧失了 add() 方法的功能，编译器不通过。
        }
    }

    @Test
    public void testWildcard01() {
        List<?> wildcardList = new ArrayList<String>();
        //wildcardList.add(123);// 编译都通不过。
        // 可以认为 <?> 提供了只读的功能，即：它失去了增加具体类型元素的能力，只保留与具体数据类型无关的功能。它不管装载在这个容器内的元素是什么类型，它只关心元素的数量、容器是否为空等。
        // 有人会问：既然 <?> 作用有局限性，那为什么还要使用它呢？我认为：它提高了代码的可读性，使程序员看到这段代码时就能快速推断源码作者的意图。
    }

    class ExtendsSuperWildcard {
        // <? extends T> 中的 ? 代表类型未知，但如果需要对数据类型的描述更精确一点，比如希望确定在一个范围内的数据类型，比如某个类及其子类都可以。
        public void testExtends(Collection<? extends Base> collection) {
            // collection 接受 Base 及其子类，但它仍然没有写操作的能力，即下面的代码仍然编译不通过。虽然不能知道具体的数据类型，但至少清楚了数据类型的范围。
            //collection.add(new Sub());
            //collection.add(new Base());
        }

        // <? super T> 和 <? extends T> 相对应，代表 T 及其超类。<? super T> 神奇之处在于：它拥有一定程度的写操作能力。
        public void testSuper(Collection<? super Sub> collection) {
            collection.add(new Sub());// 编译通过
            //collection.add(new Base());// 编译不通过
        }

        // 通配符与类型参数的区别：一般而言，通配符能干的事情都可以用类型参数替换。
        // 比如 public void test(Collection<?> collection) {} 可以被 public <T> void test(Collection<T> collection) {} 取代。
        // 需要注意的是：如果用泛型方法来取代通配符，那么上面代码中 collection 是能够进行写操作的。只不过要进行强制转换。
        public <T> void test(Collection<T> collection) {
            collection.add((T) new Integer(12));
            collection.add((T) "123");
        }
    }

    // 特别注意：类型参数适用于参数之间的类别依赖关系。
    public class Test2<T, E extends T> {
        T value1;
        E value2;

        // 特别注意：类型参数适用于参数之间的类别依赖关系。
        public <D, S extends D> void testMethod1(D d, S s) {
        }

        // E 类型是 T 类型的子类，显然这种情况类型参数更适合。有一种情况是，通配符和类型参数一起使用
        public <T> void testMethod2(T t, Collection<? extends T> collection) {
        }

        // 如果一个方法的返回类型依赖于参数的类型，那么通配符也无能为力
        public T testMethod3(T t) {
            return value1;
        }
    }
}
