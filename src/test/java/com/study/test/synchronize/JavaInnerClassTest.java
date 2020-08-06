package com.study.test.synchronize;

import org.junit.Test;

/**
 * 作用描述：内部类与静态内部类。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class JavaInnerClassTest {
    /**
     * 注意：如果 {@link OuterClass01} 和 {@link JavaInnerClassTest} 不在同一个包中，则需要将 {@link OuterClass01.InnerClass} 声明成 public 的。
     * 实例化内部类的代码必须是：OuterClass.InnerClass innerClass = outerClass.new InnerClass();
     */
    @Test
    public void testOuterClass01() {
        OuterClass01 outerClass01 = new OuterClass01();
        outerClass01.setUsername("admin");
        outerClass01.setPassword("123456");
        System.out.println("外部类的属性值：username=" + outerClass01.getUsername() + "，password=" + outerClass01.getPassword());
        OuterClass01.InnerClass innerClass = outerClass01.new InnerClass();
        innerClass.setAge(18);
        innerClass.setAddress("四川省成都市");
        System.out.println("内部类的属性值：age=" + innerClass.getAge() + "，address=" + innerClass.getAddress());
        innerClass.printOuterClassProperty();
    }

    /**
     * 打印结果与 {@link JavaInnerClassTest#testOuterClass01()} 一样
     */
    @Test
    public void testStaticInnerClass01() {
        OuterClass02 outerClass02 = new OuterClass02();
        outerClass02.setUsername("admin");
        outerClass02.setPassword("123456");
        System.out.println("外部类的属性值：username=" + outerClass02.getUsername() + "，password=" + outerClass02.getPassword());
        OuterClass02.StaticInnerClass staticInnerClass = new OuterClass02.StaticInnerClass();
        staticInnerClass.setAge(18);
        staticInnerClass.setAddress("四川省成都市");
        System.out.println("内部类的属性值：age=" + staticInnerClass.getAge() + "，address=" + staticInnerClass.getAddress());
        staticInnerClass.printOuterClassProperty();
    }
}

/**
 * 外部类、普通内部类
 */
class OuterClass01 {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    class InnerClass {
        private int age;
        private String address;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void printOuterClassProperty() {
            System.out.println("在内部类中打印外部类的属性值：username=" + username + "，password=" + password);
        }
    }
}

/**
 * 外部类、静态内部类
 */
class OuterClass02 {
    private static String username;
    private static String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    static class StaticInnerClass {
        private int age;
        private String address;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void printOuterClassProperty() {
            System.out.println("在内部类中打印外部类的属性值：username=" + username + "，password=" + password);
        }
    }
}

