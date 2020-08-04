package com.study.test.thread;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;

/**
 * 作用描述：线程的优先级。
 * 在操作系统中，线程可以划分优先级，优先级较高的线程得到的 CPU 资源较多，即 CPU 会优先执行优先级较高的线程对象中的任务。
 * 设置线程优先级有助于帮 “线程规划器” 确定在下一次选择哪一个线程来优先执行。
 * 可以使用 {@link java.lang.Thread#setPriority(int newPriority)} 方法。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月04日
 */
public class ThreadTest10 {
    class MyInheritThread01 extends Thread {
        @Override
        public void run() {
            // MyInheritThread02 线程的优先级继承自 MyInheritThread01 线程
            System.out.println("MyInheritThread01 Priority：" + this.getPriority());
            new MyInheritThread02().start();
        }
    }

    class MyInheritThread02 extends Thread {
        @Override
        public void run() {
            System.out.println("MyInheritThread02 Priority：" + this.getPriority());
        }
    }

    /**
     * 线程优先级具有继承性：在 Java 中，线程的优先级具有继承性，比如 A 线程启动 B 线程，则 B 线程的优先级与 A 线程一致。
     *
     * @throws IOException
     */
    @Test
    public void testMyInheritThread01() throws IOException {
        // MyInheritThread01 线程的优先级继承自 MainThread 线程
        System.out.println("MainThread Priority(begin)：" + Thread.currentThread().getPriority());
        // 更改 MainThread 线程的优先级。注意有无下面这一行代码的输出结果差异
        Thread.currentThread().setPriority(8);
        System.out.println("MainThread Priority(end)：" + Thread.currentThread().getPriority());
        new MyInheritThread01().start();
        System.in.read();
    }

    class MyRegularityThread01 extends Thread {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            long addResult = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 50000; j++) {
                    new Random().nextInt();
                    addResult += i;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("【MyRegularityThread01】耗时：" + (endTime + startTime));
        }
    }

    class MyRegularityThread02 extends Thread {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            long addResult = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 50000; j++) {
                    new Random().nextInt();
                    addResult += i;
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("【MyRegularityThread02】耗时：" + (endTime + startTime));
        }
    }

    /**
     * 线程优先级具有规则性：虽然使用 setPriority() 方法可以设置线程的优先级，但还没有看到设置优先级所带来的效果。
     *
     * @throws IOException
     */
    @Test
    public void testMyRegularityThread01() throws IOException {
        // 多运行几次，你会发现：高优先级的线程总是先执行完。
        // 注意：高优先级的线程总是大部分先执行完，并不代表高优先级的线程全部先执行完。
        // 另外，不要以为 MyRegularityThread01 线程先被 main 线程调用就会先执行完毕，出现这样的结果全是因为 MyRegularityThread01 线程的优先级为最高值10造成的。
        // 当线程的优先级差距很大时，谁先执行完和代码的调用顺序无关。为了验证该结论，请看 testMyRegularityThread02() 测试方法
        for (int i = 0; i < 5; i++) {
            MyRegularityThread01 myRegularityThread01 = new MyRegularityThread01();
            myRegularityThread01.setPriority(10);
            myRegularityThread01.start();

            MyRegularityThread02 myRegularityThread02 = new MyRegularityThread02();
            myRegularityThread02.setPriority(1);
            myRegularityThread02.start();
        }
        System.in.read();
    }

    @Test
    public void testMyRegularityThread02() throws IOException {
        // 多运行几次，你会发现：大部分的 MyRegularityThread02 线程先执行完，这就验证了线程优先级与代码执行顺序无关。
        // 出现这样的结果是因为 MyRegularityThread02 线程的优先级是最高的，说明线程的优先级具有一定的规则性，也就是 CPU 尽量将执行资源分配给优先级比较高的线程
        for (int i = 0; i < 5; i++) {
            MyRegularityThread01 myRegularityThread01 = new MyRegularityThread01();
            myRegularityThread01.setPriority(1);
            myRegularityThread01.start();

            MyRegularityThread02 myRegularityThread02 = new MyRegularityThread02();
            myRegularityThread02.setPriority(10);
            myRegularityThread02.start();
        }
        System.in.read();
    }

    class MyRandomnessThread01 extends Thread {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int j = 0; j < 1000; j++) {
                new Random().nextInt();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("【MyRandomnessThread01】耗时：" + (endTime + startTime));
        }
    }

    class MyRandomnessThread02 extends Thread {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            for (int j = 0; j < 1000; j++) {
                new Random().nextInt();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("【MyRandomnessThread02】耗时：" + (endTime + startTime));
        }
    }

    /**
     * 线程优先级具有随机性：上面的案例介绍了优先级较高的线程优先执行完 run() 方法中的任务，但这个结果不能说的太肯定，
     * 因为线程的优先级还具有随机性，也就是说优先级较高的线程不一定每次斗仙执行完。
     * <p>
     * 根据这个测试结果可以得出一个结论：不要把线程的优先级与运行结果的顺序作为衡量的标准。
     * 优先级较高的线程并不一定每次都先执行完 run() 方法中的任务，即线程优先级与打印顺序无关。不要将二者扯上关系，它们的关系具有不确定性和随机性。
     *
     * @throws IOException
     */
    @Test
    public void testMyRandomnessThread01() throws IOException {
        for (int i = 0; i < 5; i++) {
            // 为了让结果更能体现出随机性，将两个线程的优先级设置得接近一些。此处一个设置为5，另一个设置为6。
            MyRandomnessThread01 myRandomnessThread01 = new MyRandomnessThread01();
            myRandomnessThread01.setPriority(5);
            myRandomnessThread01.start();

            MyRandomnessThread02 myRandomnessThread02 = new MyRandomnessThread02();
            myRandomnessThread02.setPriority(6);
            myRandomnessThread02.start();
        }
        // 根据这个测试结果可以得出一个结论：不要把线程的优先级与运行结果的顺序作为衡量的标准。
        // 优先级较高的线程并不一定每次都先执行完 run() 方法中的任务，即线程优先级与打印顺序无关。不要将二者扯上关系，它们的关系具有不确定性和随机性。
        System.in.read();
    }

    class MySpeedThread01 extends Thread {
        private int count = 0;

        public int getCount() {
            return count;
        }

        @Override
        public void run() {
            while (true) {
                count++;
            }
        }
    }

    class MySpeedThread02 extends Thread {
        private int count = 0;

        public int getCount() {
            return count;
        }

        @Override
        public void run() {
            while (true) {
                count++;
            }
        }
    }

    /**
     * 测试哪个线程运行的快
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testMySpeedThread01() throws IOException, InterruptedException {
        MySpeedThread01 mySpeedThread01 = new MySpeedThread01();
        mySpeedThread01.setPriority(Thread.NORM_PRIORITY - 3);
        mySpeedThread01.start();

        MySpeedThread02 mySpeedThread02 = new MySpeedThread02();
        mySpeedThread02.setPriority(Thread.NORM_PRIORITY + 3);
        mySpeedThread02.start();

        Thread.sleep(2000);
        mySpeedThread01.stop();
        mySpeedThread02.stop();

        System.out.println("【MySpeedThread01】运算结果：" + mySpeedThread01.getCount());
        System.out.println("【MySpeedThread02】运算结果：" + mySpeedThread02.getCount());
        System.in.read();
    }
}
