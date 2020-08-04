package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：停止线程。Java 中有以下3种方法可以终止正在运行的线程：
 * 1、使用退出标志，使线程正常退出，也就是当 run() 方法执行完成后线程终止。
 * 2、使用 stop() 方法强行终止线程，但是不推荐使用这个方法。因为 stop() 和 suspend()、resume() 一样都是作废过期方法，使用它们可能产生不可预料的结果。
 * 3、使用 interrupt() 方法中断线程。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月04日
 */
public class ThreadTest07 {
    class MyThread01 extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 500000; i++) {
                System.out.println("i=" + (i + 1));
            }
        }
    }

    /**
     * 停止不了的线程
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread01() throws IOException, InterruptedException {
        MyThread01 myThread01 = new MyThread01();
        myThread01.start();
        Thread.sleep(2000);
        // 从运行结果可以看出：interrupt() 方法并不会立即停止线程。
        // 调用 interrupt() 方法来停止线程，但其效果并不像 for+break 语句那样立马就能停止。它仅仅是在当前线程中打了一个停止的标记，而非真正停止线程。
        myThread01.interrupt();
        System.in.read();
    }

    /**
     * 判断线程是否是停止状态：
     * this.interrupted()：测试当前线程是否已经中断。当前线程是指运行 this.interrupted() 方法的线程。
     * this.isInterrupted()：测试线程是否已经中断。
     * <p>
     * 两个方法的区别如下：
     * this.interrupted()：测试当前线程是否已经是中断状态，调用该方法后将会清除状态标志。
     * this.isInterrupted()：测试线程 Thread 对象是否已经是中断状态，调用该方法后不会清除状态标志。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread02() throws IOException, InterruptedException {
        MyThread01 myThread02 = new MyThread01();
        myThread02.start();
        Thread.sleep(1000);
        myThread02.interrupt();
        // 以下两行均打印 false，因为当前线程是 main 线程，而它从未中断过
        System.out.println("是否停止1 => " + myThread02.interrupted());
        System.out.println("是否停止2 => " + myThread02.interrupted());
        System.in.read();
    }

    /**
     * 让 main 线程产生中断效果
     */
    @Test
    public void testMyThread03() {
        Thread.currentThread().interrupt();
        // 注意，第二行输出的是 false，为什么呢？官方帮助文档中对 interrupted() 方法的解释如下：
        // 测试当前线程是否已经中断。线程的中断状态由该方法清除。换句话说，如果连续两次调用该方法，则第二次打印将返回 false(在第一次调用已清除其中断状态后、第二次调用检验完中断状态前，当前线程再次中断的情况除外)。
        // 因此，由于 interrupted() 方法具有清除状态的功能，所以第二次调用interrupted() 方法返回的值是 false。
        System.out.println("是否停止1 => " + Thread.interrupted());
        System.out.println("是否停止2 => " + Thread.interrupted());
    }

    @Test
    public void testMyThread04() throws IOException, InterruptedException {
        MyThread01 myThread04 = new MyThread01();
        myThread04.start();
        Thread.sleep(1000);
        myThread04.interrupt();
        // 从结果中可以看到，isInterrupted() 方法并未清除状态标志，所以下面两行均打印 true。
        System.out.println("是否停止1 => " + myThread04.isInterrupted());
        System.out.println("是否停止2 => " + myThread04.isInterrupted());
        System.in.read();
    }

    class MyThread02 extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 500000; i++) {
                // 在线程中用 for 语句来判断一下线程是否是停止状态，若是，则后面的代码不在运行。
                if (this.interrupted()) {
                    System.out.println("已经是停止状态了！我要退出了！");
                    break;
                }
                System.out.println("i=" + (i + 1));
            }
        }
    }

    @Test
    public void testMyThread05() throws IOException, InterruptedException {
        MyThread02 myThread05 = new MyThread02();
        myThread05.start();
        Thread.sleep(2000);
        myThread05.interrupt();
        System.in.read();
    }

    class MyThread03 extends Thread {
        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 500000; i++) {
                // 在线程中用 for 语句来判断一下线程是否是停止状态，若是，则后面的代码不在运行。
                // 但如果 for 下面还有语句，则会继续执行 for 下面的语句。
                if (this.interrupted()) {
                    System.out.println("已经是停止状态了！我要退出了！");
                    break;
                }
                System.out.println("i=" + (i + 1));
            }
            System.out.println("我被输出，如果此代码是 for 又继续运行，线程并未停止！");
        }
    }

    @Test
    public void testMyThread06() throws IOException, InterruptedException {
        MyThread03 myThread06 = new MyThread03();
        myThread06.start();
        Thread.sleep(2000);
        myThread06.interrupt();
        System.in.read();
    }

    class MyThread04 extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                for (int i = 0; i < 500000; i++) {
                    // 在线程中用 for 语句来判断一下线程是否是停止状态，若是，则后面的代码不在运行。
                    // 但如果 for 下面还有语句，则会继续执行 for 下面的语句，可以使用异常阻止其运行。
                    if (this.interrupted()) {
                        System.out.println("已经是停止状态了！我要退出了！");
                        throw new InterruptedException();
                    }
                    System.out.println("i=" + (i + 1));
                }
                System.out.println("我被输出，如果此代码是 for 又继续运行，线程并未停止！");
            } catch (InterruptedException e) {
                System.out.println("进入 MyThread04 类 run() 方法中的 catch 块了！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过异常停止线程
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread07() throws IOException, InterruptedException {
        MyThread04 myThread07 = new MyThread04();
        myThread07.start();
        Thread.sleep(2000);
        myThread07.interrupt();
        System.in.read();
    }

    class MyThread05 extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                System.out.println("【run()方法】begin");
                Thread.sleep(200000);
                System.out.println("【run()方法】end");
            } catch (InterruptedException e) {
                System.out.println("线程在 sleep() 中被停止，进入了 catch 块！this.isInterrupted()=" + this.isInterrupted());
                e.printStackTrace();
            }
        }
    }

    /**
     * 线程在 sleep() 中被停止
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread08() throws IOException, InterruptedException {
        // 从执行结果来看，如果在 sleep 状态下停止某一线程，则会进入 catch 语句块，并清除停止状态值，使之变成 false。
        MyThread05 myThread08 = new MyThread05();
        myThread08.start();
        Thread.sleep(200);
        myThread08.interrupt();
        System.in.read();
    }

    class MyThread06 extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                for (int i = 0; i < 100000; i++) {
                    System.out.println("i=" + (i + 1));
                }
                System.out.println("【run()方法】begin");
                Thread.sleep(200000);
                System.out.println("【run()方法】end");
            } catch (InterruptedException e) {
                System.out.println("先停止，再遇到了 sleep，进入catch 块！");
                e.printStackTrace();
            }
        }
    }

    /**
     * 线程在 sleep() 中被停止
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread09() throws IOException {
        MyThread06 myThread09 = new MyThread06();
        myThread09.start();
        myThread09.interrupt();
        System.in.read();
    }

    class MyThread07 extends Thread {
        private int i = 0;

        @Override
        public void run() {
            super.run();
            try {
                while (true) {
                    i++;
                    System.out.println("i=" + i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 暴力停止线程
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread10() throws InterruptedException {
        MyThread07 myThread10 = new MyThread07();
        myThread10.start();
        myThread10.sleep(8000);
        myThread10.stop();
    }

    class MyThread08 extends Thread {
        @Override
        public void run() {
            try {
                // 调用 stop() 方法时会抛出 java.lang.ThreadDeath 异常，但通常情况下不需要显示捕捉该异常。
                // stop() 方法已被废弃，因为如果强制让线程停止，则可能会使一些清理性的工作得不到完成。另一方面，还会对锁定的对象进行解锁，导致数据得不到同步处理，出现数据不一致的问题。
                this.stop();
            } catch (ThreadDeath e) {
                System.out.println("进入了 catch 代码块！");
                e.printStackTrace();
            }
        }
    }

    /**
     * stop() 方法与 java.lang.ThreadDeath 异常
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMyThread11() throws IOException {
        MyThread08 myThread11 = new MyThread08();
        myThread11.start();
        System.in.read();
    }

    class SynchronizedObject {
        private String username = "a";
        private String password = "aa";

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

        synchronized public void printString(String username, String password) throws InterruptedException {
            this.username = username;
            Thread.sleep(100000);
            this.password = password;
        }
    }

    class MyThread09 extends Thread {
        private SynchronizedObject synchronizedObject;

        public MyThread09(SynchronizedObject synchronizedObject) {
            super();
            this.synchronizedObject = synchronizedObject;
        }

        @Override
        public void run() {
            try {
                synchronizedObject.printString("b", "bb");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testMyThread12() throws IOException, InterruptedException {
        SynchronizedObject synchronizedObject = new SynchronizedObject();
        MyThread09 myThread12 = new MyThread09(synchronizedObject);
        myThread12.start();
        Thread.sleep(500);
        // 使用 stop() 释放锁将会给数据造成不一致的后果。如果出现这样的情况，程序处理的数据就有可能遭到破坏，最终导致程序执行的流程错误，一定要特别注意。
        myThread12.stop();
        System.out.println("username=" + synchronizedObject.getUsername() + "，password=" + synchronizedObject.getPassword());
        System.in.read();
    }

    /**
     * 使用 return 停止线程：将 interrupt() 方法与 return 结合使用也能实现停止线程的效果。
     * 不过还是建议采用抛异常的方式停止线程，因为在 catch 块中还可以将异常向上抛，使得线程停止的事件得以传播。
     */
    class MyThread10 extends Thread {
        @Override
        public void run() {
            while (true) {
                if (this.isInterrupted()) {
                    System.out.println("线程停止了！");
                    return;
                }
                System.out.println("timer=" + System.currentTimeMillis());
            }
        }
    }

    @Test
    public void testMyThread13() throws IOException, InterruptedException {
        MyThread10 myThread13 = new MyThread10();
        myThread13.start();
        Thread.sleep(2000);
        myThread13.interrupt();
        System.in.read();
    }
}
