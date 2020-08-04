package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：留意 i-- 与 System.out.println() 的异常、this 与 Thread.currentThread() 的区别
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年07月27日
 */
public class ThreadTest04 {
    class MyThread01 extends Thread {
        private int i = 5;

        /**
         * 留意 i-- 与 System.out.println() 的异常。
         * 注意 run() 方法中有无 synchronized 关键字的区别。
         */
        @Override
        public /*synchronized*/ void run() {
            // 注意：i-- 由之前示例中的单独一行运行改为在 System.out.println() 方法中直接打印
            System.out.println("i = " + (i--) + ", 当前线程：" + Thread.currentThread().getName());
        }
    }

    @Test
    public void testMyThread01() throws IOException {
        // 该程序运行后还是有可能出现非线程安全问题
        // 本测试用例的目的：虽然 println() 方法在内部是同步的，但 i-- 操作是在进入 println() 之前发生的，所以有发生非线程安全问题的可能
        // 所以，为了防止非线程安全问题，还是需要使用同步方法
        MyThread01 myThread01 = new MyThread01();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        new Thread(myThread01).start();
        System.in.read();
    }

    class MyThread02 extends Thread {
        public MyThread02() {
            System.out.println("【构造方法】当前线程：" + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            System.out.println("【run()方法】当前线程：" + Thread.currentThread().getName());
        }
    }

    @Test
    public void testMyThread02() throws IOException {
        MyThread02 myThread02 = new MyThread02();
        // MyThread02 类的构造方法是被 main 线程调用的，而 run() 方法是被 Thread-0 线程自动调用的
        myThread02.start();
        // MyThread02 类的构造方法、run() 方法都是被 main 线程调用的：直接调用 run() 方法达不到多线程的目的
        //myThread02.run();
        System.in.read();
    }

    class MyCurrentThread extends Thread {
        public MyCurrentThread() {
            System.out.println("【构造方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【构造方法】this.currentThread().getName()=" + this.currentThread().getName());
            System.out.println("【构造方法】this.getName()=" + this.getName());
        }

        @Override
        public void run() {
            System.out.println("【run()方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【run()方法】this.currentThread().getName()=" + this.currentThread().getName());
            System.out.println("【run()方法】this.getName()=" + this.getName());
        }
    }

    @Test
    public void testMyCurrentThread01() throws IOException {
        // 该例涉及2个类：ThreadTest04 和 MyCurrentThread。
        // 首先，由 main 线程创建 MyCurrentThread 类的对象，在类初始化时会调其构造方法，打印的当前线程名字就是 main。
        // 注意 this.getName() 方法：MyCurrentThread 类本身没有这个方法，而是其父类 Thread 的，由于该方法目前没有被重写，所以调用它会给出默认值即 Thread-0。
        // 其次，线程直接调用 start() 方法，此时该线程已经被命名成 A 了，再调用 this.getName() 方法得到的就是重写后的 A 了。
        MyCurrentThread myCurrentThread1 = new MyCurrentThread();
        myCurrentThread1.setName("A");
        myCurrentThread1.start();
        System.in.read();
    }

    @Test
    public void testMyCurrentThread02() throws IOException {
        MyCurrentThread myCurrentThread2 = new MyCurrentThread();
        Thread thread2 = new Thread(myCurrentThread2);
        thread2.setName("A");
        thread2.start();
        System.in.read();
    }

    @Test
    public void testMyCurrentThread03() throws IOException {
        MyCurrentThread myCurrentThread3 = new MyCurrentThread();
        Thread thread3 = new Thread(myCurrentThread3);
        myCurrentThread3.setName("A");
        thread3.start();
        System.in.read();
    }

    /**
     * 自定义线程类时，如果继承自 java.lang.Thread，那么自定义线程类就可以使用 this 关键字调用继承自父类 Thread 的方法，this 就是当前的对象。
     * 另一方面，Thread.currentThread() 可以获取当前线程的引用，一般都是在没有线程对象又需要获得线程信息时通过 Thread.currentThread() 获取当前代码段所在线程的引用。
     * <p>
     * 尽管 this 与 Thread.currentThread() 都可以获取到 Thread 的引用，但是在某种情况下获取到的引用是有差别的。
     * 下面举例进行说明，注意 testMyCurrentThisThread01()、testMyCurrentThisThread02()、testMyCurrentThisThread03() 三个测试方法的输出结果。
     */
    class MyCurrentThisThread extends Thread {
        public MyCurrentThisThread() {
            System.out.println("【构造方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【构造方法】Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());
            System.out.println("【构造方法】this.getName()=" + this.getName());
            System.out.println("【构造方法】this.isAlive()=" + this.isAlive());
            System.out.println("【构造方法】Thread.currentThread() == this 结果：" + (Thread.currentThread() == this));
        }

        @Override
        public void run() {
            System.out.println("【run()方法】Thread.currentThread().getName()=" + Thread.currentThread().getName());
            System.out.println("【run()方法】Thread.currentThread().isAlive()=" + Thread.currentThread().isAlive());
            System.out.println("【run()方法】this.getName()=" + this.getName());
            System.out.println("【run()方法】this.isAlive()=" + this.isAlive());
            System.out.println("【run()方法】Thread.currentThread() == this 结果：" + (Thread.currentThread() == this));
        }
    }

    @Test
    public void testMyCurrentThisThread01() throws IOException {
        // 这个测试用例中 this 与 Thread.currentThread() 是同一个引用。下面分析构造方法和 run() 方法的输出结果。
        // ========================1、构造方法的输出结果
        // 【构造方法】Thread.currentThread().getName()=main
        // 【构造方法】Thread.currentThread().isAlive()=true
        // 说明：先实例化 MyCurrentThisThread，调用 MyCurrentThisThread 构造方法的是主线程 main。
        // 【构造方法】this.getName()=Thread-0
        // 【构造方法】this.isAlive()=false
        // 说明：这个 this 是 MyCurrentThisThread 的引用，是个线程类，但是这个线程类并没有设置名字，所以 Thread 给了个默认名称 Thread-0。
        // 默认名字的规则定义为：public Thread() { init(null, null, "Thread-" + nextThreadNum(), 0); }
        // 因为仅仅是运行构造方法，线程还未运行，所以 this.isAlive()=false
        // ========================2、run() 方法的输出结果
        // 【run()方法】Thread.currentThread().getName()=A
        // 【run()方法】Thread.currentThread().isAlive()=true
        // 说明：当前线程的名字为 A，是通过 myCurrentThisThread1.setName("A"); 手动赋值的，并且该线程是运行着的。
        // 【run()方法】this.getName()=A
        // 【run()方法】this.isAlive()=true
        // 说明：我们运行的线程是 MyCurrentThisThread 的引用，而 this 也是 MyCurrentThisThread 的引用，所以打印结果与 Thread.currentThread() 相同，并且 Thread.currentThread() == this 结果为 true
        MyCurrentThisThread myCurrentThisThread1 = new MyCurrentThisThread();
        myCurrentThisThread1.setName("A");
        myCurrentThisThread1.start();
        System.in.read();
    }

    @Test
    public void testMyCurrentThisThread02() throws IOException {
        // 此时 this 与 Thread.currentThread() 不再是同一个引用了。注意与 testMyCurrentThisThread01() 方法的区别。
        // 将自定义线程对象以参数形式传递给 Thread 构造方法再进行 start() 启动线程，这时直接启动的线程实际是 thread2，
        // 而作为参数的 myCurrentThisThread2 赋给 Thread 类的 target 属性，之后在 Thread 的 run() 方法中调用 target.run()，
        // 此时 Thread.currentThread() 是 Thread 的引用，而 this 仍然是 MyCurrentThisThread 的引用，所以是不一样的。
        MyCurrentThisThread myCurrentThisThread2 = new MyCurrentThisThread();
        Thread thread2 = new Thread(myCurrentThisThread2);
        thread2.setName("A");
        thread2.start();
        System.in.read();
    }

    @Test
    public void testMyCurrentThisThread03() throws IOException {
        MyCurrentThisThread myCurrentThisThread3 = new MyCurrentThisThread();
        Thread thread3 = new Thread(myCurrentThisThread3);
        myCurrentThisThread3.setName("A");
        thread3.start();
        System.in.read();
    }
}
