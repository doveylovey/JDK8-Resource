package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：锁对象的改变。
 * 在将任何数据类型作为同步锁时，需要注意是否有多个线程同时持有锁对象：如果同时持有相同的锁对象，则这些线程之间就是同步的；如果分别持有锁对象，则这些线程之间就是异步的。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月06日
 */
public class SynchronizedTest16 {
    class StringTwoLock {
        private String lock = "123";

        public void method() throws InterruptedException {
            synchronized (lock) {
                System.out.println("【method()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                lock = "456";
                Thread.sleep(2000);
                System.out.println("【method()方法】threadName：" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            }
        }
    }

    class StringTwoLockThreadA extends Thread {
        private StringTwoLock stringTwoLock;

        public StringTwoLockThreadA(StringTwoLock stringTwoLock) {
            super();
            this.stringTwoLock = stringTwoLock;
        }

        @Override
        public void run() {
            super.run();
            try {
                stringTwoLock.method();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class StringTwoLockThreadB extends Thread {
        private StringTwoLock stringTwoLock;

        public StringTwoLockThreadB(StringTwoLock stringTwoLock) {
            super();
            this.stringTwoLock = stringTwoLock;
        }

        @Override
        public void run() {
            super.run();
            try {
                stringTwoLock.method();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 若有 Thread.sleep(50);，则运行是异步的，因为 50ms 后线程 B 取得的锁是 "456"。
     * 若无 Thread.sleep(50);，则运行是同步的。线程 A 和线程 B 持有的锁都是 "123"，虽然将锁改成了 "456"，但结果还是同步的，因为线程 A 和线程 B 争抢的锁是 "123"。
     * 特别注意：只要对象不变，即使对象的属性改变了，运行结果还是同步的。参考示例 {@link SynchronizedTest16#testNewPropertiesOneLock01()}
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testStringTwoLock01() throws IOException, InterruptedException {
        StringTwoLock stringTwoLock = new StringTwoLock();
        StringTwoLockThreadA threadA = new StringTwoLockThreadA(stringTwoLock);
        threadA.setName("A");
        StringTwoLockThreadB threadB = new StringTwoLockThreadB(stringTwoLock);
        threadB.setName("B");
        threadA.start();
        //Thread.sleep(50);// 存在 50ms 时间差
        threadB.start();
        System.in.read();
    }

    /**
     * 只要对象不变，即使对象的属性改变了，运行结果还是同步的。
     */
    class NewPropertiesOneLock {
        public void method(UserInfo userInfo) throws InterruptedException {
            synchronized (userInfo) {
                System.out.println("【method()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                userInfo.setUsername("changed");
                Thread.sleep(3000);
                System.out.println("【method()方法】threadName：" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            }
        }
    }

    class UserInfo {
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
    }

    class NewPropertiesOneLockThreadA extends Thread {
        private UserInfo userInfo;
        private NewPropertiesOneLock newPropertiesOneLock;

        public NewPropertiesOneLockThreadA(UserInfo userInfo, NewPropertiesOneLock newPropertiesOneLock) {
            super();
            this.userInfo = userInfo;
            this.newPropertiesOneLock = newPropertiesOneLock;
        }

        @Override
        public void run() {
            super.run();
            try {
                newPropertiesOneLock.method(userInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class NewPropertiesOneLockThreadB extends Thread {
        private UserInfo userInfo;
        private NewPropertiesOneLock newPropertiesOneLock;

        public NewPropertiesOneLockThreadB(UserInfo userInfo, NewPropertiesOneLock newPropertiesOneLock) {
            super();
            this.userInfo = userInfo;
            this.newPropertiesOneLock = newPropertiesOneLock;
        }

        @Override
        public void run() {
            super.run();
            try {
                newPropertiesOneLock.method(userInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试只要对象不变，即使对象的属性改变了，运行结果还是同步的。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testNewPropertiesOneLock01() throws IOException, InterruptedException {
        UserInfo userInfo = new UserInfo();
        NewPropertiesOneLock newPropertiesOneLock = new NewPropertiesOneLock();
        NewPropertiesOneLockThreadA threadA = new NewPropertiesOneLockThreadA(userInfo, newPropertiesOneLock);
        threadA.setName("A");
        threadA.start();
        Thread.sleep(50);// 存在 50ms 时间差
        NewPropertiesOneLockThreadB threadB = new NewPropertiesOneLockThreadB(userInfo, newPropertiesOneLock);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
