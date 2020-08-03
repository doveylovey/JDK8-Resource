package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 作用描述：TODO
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年07月26日
 */
public class ThreadTest03 {
    /**
     * 模拟一个 Servlet 类
     */
    static class LoginServlet {
        private static String usernameRef;
        private static String passwordRef;

        /**
         * 注意 doPost() 方法中有无 synchronized 关键字的区别。若无 synchronized 则可能会出现如下情况：
         * username = a, password = aa
         * username = b, password = aa
         *
         * @param username
         * @param password
         */
        public static /*synchronized*/ void doPost(String username, String password) {
            try {
                usernameRef = username;
                if ("a".equals(username)) {
                    TimeUnit.SECONDS.sleep(5);
                }
                passwordRef = password;
                System.out.println("username = " + usernameRef + ", password = " + passwordRef);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class LoginThreadA extends Thread {
        @Override
        public void run() {
            LoginServlet.doPost("a", "aa");
        }
    }

    class LoginThreadB extends Thread {
        @Override
        public void run() {
            LoginServlet.doPost("b", "bb");
        }
    }

    @Test
    public void testLoginThread01() throws IOException {
        new LoginThreadA().start();
        new LoginThreadB().start();
        System.in.read();
    }
}
