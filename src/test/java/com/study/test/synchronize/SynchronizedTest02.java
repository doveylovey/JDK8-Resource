package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：实例变量是非线充安全的
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest02 {
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

    /**
     * 注意 VarInClass#setUsername(String username) 方法有无 synchronized 关键字的区别
     *
     * @throws IOException
     */
    @Test
    public void testVarInClass01() throws IOException {
        VarInClass varInClass = new VarInClass();
        new VarInClassThreadA(varInClass).start();
        new VarInClassThreadB(varInClass).start();
        System.in.read();
    }
}
