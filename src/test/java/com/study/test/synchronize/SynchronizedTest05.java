package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：脏读。
 * 多个线程调用同一个方法时，为避免数据出现交叉的情况，可以使用 synchronized 关键字来进行同步。
 * 虽然在赋值时进行了同步，但在取值时也有可能出现一些意想不到的结果，这种情况就是脏读。
 * 发生脏读的情况是：在读取实例变量的值时，此值已经被其他线程更改过了。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest05 {
    class DirtyRead {
        private String username = "A";
        private String password = "AA";

        /**
         * 同步方法 setValue() 的锁属于 DirtyRead 类的实例
         *
         * @param username
         * @param password
         * @throws InterruptedException
         */
        public synchronized void setValue(String username, String password) throws InterruptedException {
            this.username = username;
            Thread.sleep(5000);
            this.password = password;
            System.out.println("【setValue()方法】threadName=" + Thread.currentThread().getName() + "，username=" + username + "，password=" + password);
        }

        public synchronized void getValue() {
            System.out.println("【getValue()方法】threadName=" + Thread.currentThread().getName() + "，username=" + username + "，password=" + password);
        }
    }

    class DirtyReadThread extends Thread {
        private DirtyRead dirtyRead;

        public DirtyReadThread(DirtyRead dirtyRead) {
            this.dirtyRead = dirtyRead;
        }

        @Override
        public void run() {
            super.run();
            try {
                dirtyRead.setValue("B", "BB");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注意：当 getValue() 方法前没有 synchronized 关键字时，打印结果可能会出现 username=B，password=AA。
     * 出现脏读的原因是 getValue() 方法不是同步的，所以可以在任一时刻调用，解决方法就是在 getValue() 方法前加上 synchronized 关键字。
     * <p>
     * 通过这个示例，不仅要知道脏读是通过 synchronized 关键字解决的，还须明白：
     * 1、当线程 A 调用 Object 对象中加了 synchronized 关键字的 X 方法时，线程 A 就获得了 X 方法锁，更准确的说是获得了对象的锁，
     * 所以线程 B 必须等线程 A 执行完毕后才可以调用 X 方法，但线程 B 可以随意调用 Object 对象中的其他非 synchronized 同步方法。
     * 2、当线程 A 调用 Object 对象中加了 synchronized 关键字的 X 方法时，线程 A 就获得了 X 方法所在对象的锁，所以线程 B 必须等线程 A 执行完毕后才可以调用 X 方法。
     * 如果线程 B 调用被 synchronized 声明的非 X 方法时，也必须等线程 A 将 X 方法执行完，也就是释放对象锁后才可以调用。
     * 这时线程 A 已经执行了一个完整的任务，也就是说 username 和 password 已经被同时赋值，就不会存在脏读的情况了。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testDirtyRead01() throws IOException, InterruptedException {
        DirtyRead dirtyRead = new DirtyRead();
        new DirtyReadThread(dirtyRead).start();
        Thread.sleep(5000);// 打印结果会受此值大小影响
        dirtyRead.getValue();
        System.in.read();
    }
}
