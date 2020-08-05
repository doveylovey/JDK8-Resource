package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：方法内的变量是线程安全的
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
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

    /**
     * 由结果可知：方法中的变量不存在非线程安全问题，永远都是线程安全的，这是因为方法内部的变量是私有的特性造成的。
     *
     * @throws IOException
     */
    @Test
    public void testVarInMethod01() throws IOException {
        VarInMethod varInMethod = new VarInMethod();
        new VarInMethodThreadA(varInMethod).start();
        new VarInMethodThreadB(varInMethod).start();
        System.in.read();
    }
}
