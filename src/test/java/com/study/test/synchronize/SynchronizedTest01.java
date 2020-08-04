package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：synchronized 同步方法
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年07月26日
 */
public class SynchronizedTest01 {
    /**
     * 方法内的变量是线程安全的。非线程安全问题存在于实例变量中，如果是方法内部的私有变量，则不存在非线程安全问题，所得结果也就是线程安全的了
     */
    class VarInMethod {
        public void setUsername(String username) {
            try {
                int num = 0;
                if ("a".equals(username)) {
                    num = 100;
                    System.out.println("a set over");
                    Thread.sleep(2000);
                } else {
                    num = 200;
                    System.out.println("other set over");
                }
                System.out.println("username=" + username + "，num=" + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class VarInMethodThreadA extends Thread {
        private VarInMethod varInMethod;

        public VarInMethodThreadA(VarInMethod varInMethod) {
            super();
            this.varInMethod = varInMethod;
        }

        @Override
        public void run() {
            super.run();
            varInMethod.setUsername("a");
        }
    }

    class VarInMethodThreadB extends Thread {
        private VarInMethod varInMethod;

        public VarInMethodThreadB(VarInMethod varInMethod) {
            super();
            this.varInMethod = varInMethod;
        }

        @Override
        public void run() {
            super.run();
            varInMethod.setUsername("b");
        }
    }

    @Test
    public void VarInMethodThread01() throws IOException {
        // 由结果可知：方法中的变量不存在非线程安全问题，永远都是线程安全的，这是因为方法内部的变量是私有的特性造成的。
        VarInMethod varInMethod = new VarInMethod();
        new VarInMethodThreadA(varInMethod).start();
        new VarInMethodThreadB(varInMethod).start();
        System.in.read();
    }

    /**
     * 实例变量是非线充安全的：如果多个线程共同访问一个对象中的实例变量，则有可能出现非线程安全问题。
     * 用线程访问的对象中如果有多个实例变量，则运行结果可能出现交叉的情况；如果仅有一个实例变量，则运行的结果可能出现覆盖的情况。
     */
    class VarInClass {
        int num = 0;

        /**
         * 注意该方法有无 synchronized 关键字的区别：
         * 若无，则两个线程同时访问一个没有同步的方法，如果两个线程同时操作业务对象中的实例变量，则有可能出现非线程安全问题。
         * 若有，两个线程访问同一个对象中的同步方法时一定是线程安全的。
         *
         * @param username
         */
        public /*synchronized*/ void setUsername(String username) {
            try {
                if ("a".equals(username)) {
                    num = 100;
                    System.out.println("a set over");
                    Thread.sleep(2000);
                } else {
                    num = 200;
                    System.out.println("other set over");
                }
                System.out.println("username=" + username + "，num=" + num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class VarInClassThreadA extends Thread {
        private VarInClass varInClass;

        public VarInClassThreadA(VarInClass varInClass) {
            super();
            this.varInClass = varInClass;
        }

        @Override
        public void run() {
            super.run();
            varInClass.setUsername("a");
        }
    }

    class VarInClassThreadB extends Thread {
        private VarInClass varInClass;

        public VarInClassThreadB(VarInClass varInClass) {
            super();
            this.varInClass = varInClass;
        }

        @Override
        public void run() {
            super.run();
            varInClass.setUsername("b");
        }
    }

    @Test
    public void VarInClassThread01() throws IOException {
        // 注意 VarInClass#setUsername(String username) 方法有无 synchronized 关键字的区别
        VarInClass varInClass = new VarInClass();
        new VarInClassThreadA(varInClass).start();
        new VarInClassThreadB(varInClass).start();
        System.in.read();
    }
}
