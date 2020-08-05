package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 作用描述：synchronized 同步语句块。
 * 用 synchronized 关键字声明方法在某些情况下是有弊端的，比如线程 A 调用同步方法执行一个长时间的任务，那么线程 B 则必须等待很长时间。
 * 在这种情况下就可以使用 synchronized 同步语句块来解决。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest09 {
    /**
     * 计时器工具类：用于分别记录两个线程的开始、结束时间
     */
    static class TimerUtils {
        public static long beginTime1;
        public static long endTime1;
        public static long beginTime2;
        public static long endTime2;
    }

    /**
     * synchronized 方法的弊端
     */
    class SynchronizedDisadvantage {
        private String data1;
        private String data2;

        public synchronized void doLongTimeTask() throws InterruptedException {
            System.out.println("【doLongTimeTask()方法】begin");
            Thread.sleep(3000);
            data1 = "【data1】处理耗时任务后从远程返回值，threadName=" + Thread.currentThread().getName();
            data2 = "【data2】处理耗时任务后从远程返回值，threadName=" + Thread.currentThread().getName();
            System.out.println(data1 + "\n" + data2);
            System.out.println("【doLongTimeTask()方法】end");
        }
    }

    class SynchronizedDisadvantageThreadA extends Thread {
        private SynchronizedDisadvantage synchronizedDisadvantage;

        public SynchronizedDisadvantageThreadA(SynchronizedDisadvantage synchronizedDisadvantage) {
            super();
            this.synchronizedDisadvantage = synchronizedDisadvantage;
        }

        @Override
        public void run() {
            super.run();
            TimerUtils.beginTime1 = System.currentTimeMillis();
            try {
                synchronizedDisadvantage.doLongTimeTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimerUtils.endTime1 = System.currentTimeMillis();
        }
    }

    class SynchronizedDisadvantageThreadB extends Thread {
        private SynchronizedDisadvantage synchronizedDisadvantage;

        public SynchronizedDisadvantageThreadB(SynchronizedDisadvantage synchronizedDisadvantage) {
            super();
            this.synchronizedDisadvantage = synchronizedDisadvantage;
        }

        @Override
        public void run() {
            super.run();
            TimerUtils.beginTime2 = System.currentTimeMillis();
            try {
                synchronizedDisadvantage.doLongTimeTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimerUtils.endTime2 = System.currentTimeMillis();
        }
    }

    /**
     * 从运行结果可以看出：大约耗时6秒。
     * 在使用 synchronized 关键字来声明 {@link SynchronizedDisadvantage#doLongTimeTask()} 方法时，从运行时间就可以看出明显弊端，
     * 可以使用 synchronized 同步语句块来解决这类问题。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedDisadvantage01() throws IOException, InterruptedException {
        SynchronizedDisadvantage synchronizedDisadvantage = new SynchronizedDisadvantage();
        new SynchronizedDisadvantageThreadA(synchronizedDisadvantage).start();
        new SynchronizedDisadvantageThreadB(synchronizedDisadvantage).start();
        Thread.sleep(10000);
        long beginTime = Math.min(TimerUtils.beginTime1, TimerUtils.beginTime2);
        long endTime = Math.max(TimerUtils.endTime1, TimerUtils.endTime2);
        System.out.println("任务结束，大约耗时 " + (endTime - beginTime) + " 毫秒");
        System.in.read();
    }

    /**
     * synchronized 同步语句块的使用
     */
    class UseSynchronizedCodeBlock {
        public void serviceMethod() throws InterruptedException {
            synchronized (this) {
                System.out.println("【serviceMethod()方法】threadName=" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("【serviceMethod()方法】threadName=" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            }
        }
    }

    class UseSynchronizedCodeBlockThreadA extends Thread {
        private UseSynchronizedCodeBlock useSynchronizedCodeBlock;

        public UseSynchronizedCodeBlockThreadA(UseSynchronizedCodeBlock useSynchronizedCodeBlock) {
            super();
            this.useSynchronizedCodeBlock = useSynchronizedCodeBlock;
        }

        @Override
        public void run() {
            super.run();
            try {
                useSynchronizedCodeBlock.serviceMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class UseSynchronizedCodeBlockThreadB extends Thread {
        private UseSynchronizedCodeBlock useSynchronizedCodeBlock;

        public UseSynchronizedCodeBlockThreadB(UseSynchronizedCodeBlock useSynchronizedCodeBlock) {
            super();
            this.useSynchronizedCodeBlock = useSynchronizedCodeBlock;
        }

        @Override
        public void run() {
            super.run();
            try {
                useSynchronizedCodeBlock.serviceMethod();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 该示例虽然使用了 synchronized 同步语句块，但执行效率并没有提高，执行效果还是同步运行的。
     *
     * @throws IOException
     */
    @Test
    public void testUseSynchronizedCodeBlock01() throws IOException {
        UseSynchronizedCodeBlock useSynchronizedCodeBlock = new UseSynchronizedCodeBlock();
        UseSynchronizedCodeBlockThreadA threadA = new UseSynchronizedCodeBlockThreadA(useSynchronizedCodeBlock);
        threadA.setName("A");
        threadA.start();
        UseSynchronizedCodeBlockThreadB threadB = new UseSynchronizedCodeBlockThreadB(useSynchronizedCodeBlock);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 用同步代码块解决同步法的弊端
     */
    class SynchronizedAdvantage {
        private String data1;
        private String data2;

        public void doLongTimeTask() throws InterruptedException {
            System.out.println("【doLongTimeTask()方法】begin");
            Thread.sleep(3000);
            String tempData1 = "【data1】处理耗时任务后从远程返回值，threadName=" + Thread.currentThread().getName();
            String tempData2 = "【data2】处理耗时任务后从远程返回值，threadName=" + Thread.currentThread().getName();
            synchronized (this) {
                data1 = tempData1;
                data2 = tempData2;
            }
            System.out.println(data1 + "\n" + data2);
            System.out.println("【doLongTimeTask()方法】end");
        }
    }

    class SynchronizedAdvantageThreadA extends Thread {
        private SynchronizedAdvantage synchronizedAdvantage;

        public SynchronizedAdvantageThreadA(SynchronizedAdvantage synchronizedAdvantage) {
            super();
            this.synchronizedAdvantage = synchronizedAdvantage;
        }

        @Override
        public void run() {
            super.run();
            TimerUtils.beginTime1 = System.currentTimeMillis();
            try {
                synchronizedAdvantage.doLongTimeTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimerUtils.endTime1 = System.currentTimeMillis();
        }
    }

    class SynchronizedAdvantageThreadB extends Thread {
        private SynchronizedAdvantage synchronizedAdvantage;

        public SynchronizedAdvantageThreadB(SynchronizedAdvantage synchronizedAdvantage) {
            super();
            this.synchronizedAdvantage = synchronizedAdvantage;
        }

        @Override
        public void run() {
            super.run();
            TimerUtils.beginTime2 = System.currentTimeMillis();
            try {
                synchronizedAdvantage.doLongTimeTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            TimerUtils.endTime2 = System.currentTimeMillis();
        }
    }

    /**
     * 从运行结果可以看出：大约耗时3秒。
     * 通过该示例可以得知：当一个线程访问 Object 对象中的一个 synchronized 同步代码块时，另一个线程仍然可以访问该 Object 对象中的非 synchronized 代码块
     * <p>
     * 实验进行到这里，虽然运行时间缩短、运行速度加快，但 synchronized 同步代码块真的是同步的吗？真的持有当前调用对象的锁吗？答案为是，下面用代码验证，请看
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedAdvantage01() throws IOException, InterruptedException {
        SynchronizedAdvantage synchronizedAdvantage = new SynchronizedAdvantage();
        new SynchronizedAdvantageThreadA(synchronizedAdvantage).start();
        new SynchronizedAdvantageThreadB(synchronizedAdvantage).start();
        Thread.sleep(10000);
        long beginTime = Math.min(TimerUtils.beginTime1, TimerUtils.beginTime2);
        long endTime = Math.max(TimerUtils.endTime1, TimerUtils.endTime2);
        System.out.println("任务结束，大约耗时 " + (endTime - beginTime) + " 毫秒");
        System.in.read();
    }

    /**
     * 一半同步、一半异步。
     * 本示例将要说明的是：在 synchronized 块中的就同步执行，不在 synchronized 块中的就异步执行。
     */
    class HalfSynchronized {
        public void doLongTimeTask() {
            for (int i = 0; i < 100; i++) {
                System.out.println("【doLongTimeTask()方法】异步部分。threadName=" + Thread.currentThread().getName() + "，i=" + (i + 1));
            }
            System.out.println("==========华丽的分割线==========");
            synchronized (this) {
                for (int i = 0; i < 100; i++) {
                    System.out.println("【doLongTimeTask()方法】同步部分。threadName=" + Thread.currentThread().getName() + "，i=" + (i + 1));
                }
            }
        }
    }

    class HalfSynchronizedThreadA extends Thread {
        private HalfSynchronized halfSynchronized;

        public HalfSynchronizedThreadA(HalfSynchronized halfSynchronized) {
            super();
            this.halfSynchronized = halfSynchronized;
        }

        @Override
        public void run() {
            super.run();
            halfSynchronized.doLongTimeTask();
        }
    }

    class HalfSynchronizedThreadB extends Thread {
        private HalfSynchronized halfSynchronized;

        public HalfSynchronizedThreadB(HalfSynchronized halfSynchronized) {
            super();
            this.halfSynchronized = halfSynchronized;
        }

        @Override
        public void run() {
            super.run();
            halfSynchronized.doLongTimeTask();
        }
    }

    /**
     * 多运行几次。
     * 实验结果：进入 synchronized 代码块后就排队执行。即非同步时交叉打印，同步时按顺序打印。
     *
     * @throws IOException
     */
    @Test
    public void testHalfSynchronized01() throws IOException {
        HalfSynchronized halfSynchronized = new HalfSynchronized();
        new HalfSynchronizedThreadA(halfSynchronized).start();
        new HalfSynchronizedThreadB(halfSynchronized).start();
        System.in.read();
    }

    /**
     * synchronized 代码块间的同步性。
     * 在使用同步 synchronized (this) 代码块时需要注意：当一个线程访问 Object 的一个 synchronized (this) 同步代码块时，
     * 其他线程对这个 Object 中的所有其他 synchronized (this) 同步代码块的访问将被阻塞，这说明 synchronized 使用的 “对象监视器” 是一个。
     */
    class ManySynchronizedCodeBlock {
        public void serviceMethodA() throws InterruptedException {
            synchronized (this) {
                System.out.println("【serviceMethodA()方法】threadName=" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                Thread.sleep(2000);
                System.out.println("【serviceMethodA()方法】threadName=" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            }
        }

        public void serviceMethodB() {
            synchronized (this) {
                System.out.println("【serviceMethodB()方法】threadName=" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                System.out.println("【serviceMethodB()方法】threadName=" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            }
        }
    }

    class ManySynchronizedCodeBlockThreadA extends Thread {
        private ManySynchronizedCodeBlock manySynchronizedCodeBlock;

        public ManySynchronizedCodeBlockThreadA(ManySynchronizedCodeBlock manySynchronizedCodeBlock) {
            super();
            this.manySynchronizedCodeBlock = manySynchronizedCodeBlock;
        }

        @Override
        public void run() {
            super.run();
            try {
                manySynchronizedCodeBlock.serviceMethodA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class ManySynchronizedCodeBlockThreadB extends Thread {
        private ManySynchronizedCodeBlock manySynchronizedCodeBlock;

        public ManySynchronizedCodeBlockThreadB(ManySynchronizedCodeBlock manySynchronizedCodeBlock) {
            super();
            this.manySynchronizedCodeBlock = manySynchronizedCodeBlock;
        }

        @Override
        public void run() {
            super.run();
            manySynchronizedCodeBlock.serviceMethodB();
        }
    }

    /**
     * 从结果可以看出：两个同步代码块按顺序执行。
     *
     * @throws IOException
     */
    @Test
    public void testManySynchronizedCodeBlock01() throws IOException {
        ManySynchronizedCodeBlock manySynchronizedCodeBlock = new ManySynchronizedCodeBlock();
        ManySynchronizedCodeBlockThreadA threadA = new ManySynchronizedCodeBlockThreadA(manySynchronizedCodeBlock);
        threadA.setName("A");
        threadA.start();
        ManySynchronizedCodeBlockThreadB threadB = new ManySynchronizedCodeBlockThreadB(manySynchronizedCodeBlock);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 验证 synchronized (this) 同步代码块锁定的是当前对象。
     * 和 synchronized 方法一样，synchronized (this) 同步代码块也是锁定当前对象的。
     */
    class SynchronizedLockCurrentObject {
        public void doLongTimeTask() throws InterruptedException {
            synchronized (this) {
                for (int i = 0; i < 100; i++) {
                    System.out.println("【doLongTimeTask()方法】threadName=" + Thread.currentThread().getName() + "，i=" + (i + 1));
                    Thread.sleep(100);
                }
            }
        }

        /**
         * 注意该方法有无 synchronized 关键字的输出结果：若无，异步打印(线程之间乱序)；若有，同步打印(线程之间顺序)。
         */
        public /*synchronized*/ void doOtherTask() {
            System.err.println("【doOtherTask()方法】threadName=" + Thread.currentThread().getName());
        }
    }

    class SynchronizedLockCurrentObjectThreadA extends Thread {
        private SynchronizedLockCurrentObject synchronizedLockCurrentObject;

        public SynchronizedLockCurrentObjectThreadA(SynchronizedLockCurrentObject synchronizedLockCurrentObject) {
            super();
            this.synchronizedLockCurrentObject = synchronizedLockCurrentObject;
        }

        @Override
        public void run() {
            super.run();
            try {
                synchronizedLockCurrentObject.doLongTimeTask();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class SynchronizedLockCurrentObjectThreadB extends Thread {
        private SynchronizedLockCurrentObject synchronizedLockCurrentObject;

        public SynchronizedLockCurrentObjectThreadB(SynchronizedLockCurrentObject synchronizedLockCurrentObject) {
            super();
            this.synchronizedLockCurrentObject = synchronizedLockCurrentObject;
        }

        @Override
        public void run() {
            super.run();
            synchronizedLockCurrentObject.doOtherTask();
        }
    }

    /**
     * 注意 {@link SynchronizedLockCurrentObject#doOtherTask()} 方法前有无 synchronized 关键字的输出结果：若无，异步打印(线程之间乱序)；若有，同步打印(线程之间顺序)。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testSynchronizedLockCurrentObject01() throws IOException, InterruptedException {
        SynchronizedLockCurrentObject synchronizedLockCurrentObject = new SynchronizedLockCurrentObject();
        SynchronizedLockCurrentObjectThreadA threadA = new SynchronizedLockCurrentObjectThreadA(synchronizedLockCurrentObject);
        threadA.setName("A");
        threadA.start();
        Thread.sleep(100);
        SynchronizedLockCurrentObjectThreadB threadB = new SynchronizedLockCurrentObjectThreadB(synchronizedLockCurrentObject);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 将任意对象作为对象监视器。
     * 多个线程调用同一对象中不同名称的 synchronized 同步方法或 synchronized(this) 同步代码块时，调用效果就是按顺序执行，也就是同步的、阻塞的。
     * 这说明 synchronized 同步方法或 synchronized 同步代码块分别有两种作用：
     * 1、synchronized 同步方法作用：
     * A)对其他 synchronized 同步方法或 synchronized(this) 同步代码块的调用呈阻塞状态。
     * B)同一时间只有一个线程可以执行 synchronized 同步方法中的代码。
     * 2、synchronized 同步代码块作用：
     * A)对其他 synchronized 同步方法或 synchronized(this) 同步代码块的调用呈阻塞状态。
     * B)同一时间只有一个线程可以执行 synchronized(this) 同步代码块中的代码。
     * <p>
     * 除了使用 synchronized(this) 格式来同步代码块之外，Java 还支持使用 “任意对象” 作为 “对象监视器” 来实现同步代码块。这个 “任意对象” 大多数是实例变量或方法参数，其使用格式为 synchronized(除 this 外的任意对象)。
     * 根据前面对 synchronized(this) 同步代码块的作用总结可知，synchronized(非 this 对象)的格式只有一种：synchronized(非 this 对象)同步代码块。
     * 1、在多个线程持有 “对象监视器” 为同一个对象的前提下，同一时间只有一个线程可以执行 synchronized(非 this 对象)同步代码块中的代码。
     * 2、当持有 “对象监视器” 为同一个对象的前提下，同一时间只有一个线程可以执行 synchronized(非 this 对象)同步代码块中的代码。
     * <p>
     * 锁非 this 对象的优点：如果在一个类中有多个 synchronized 方法，这时虽然能实现同步，但会受到阻塞，影响运行效率。
     * 但如果使用同步代码块锁非 this 对象，则 synchronized(非 this 对象)同步代码块中的程序与同步方法是异步的，不会与其他锁 this 对象的同步方法争抢 this 锁，可以大大提高运行效率。
     */
    class AnyObjectAsMonitor {
        private String username;
        private String password;
        //private String strAsMonitor = new String();

        public void setValue(String username, String password) throws InterruptedException {
            // 注意 strAsMonitor 变量所在位置：若是全局变量，则对象监视器就是同一个对象；若是局部变量，则对象监视器就是不同对象。
            // 使用 “synchronized(非 this 对象)同步代码块” 格式进行同步操作时，对象监视器必须是同一个对象。如果不是，则运行结果就是异步调用，会交叉运行。
            String strAsMonitor = new String();
            synchronized (strAsMonitor) {
                System.out.println("【setValue()方法】threadName=" + Thread.currentThread().getName() + "，在 " + System.currentTimeMillis() + " 进入同步代码块！");
                this.username = username;
                Thread.sleep(3000);
                this.password = password;
                System.out.println("【setValue()方法】threadName=" + Thread.currentThread().getName() + "，在 " + System.currentTimeMillis() + " 离开同步代码块！");
            }
        }
    }

    class AnyObjectAsMonitorThreadA extends Thread {
        private AnyObjectAsMonitor anyObjectAsMonitor;

        public AnyObjectAsMonitorThreadA(AnyObjectAsMonitor anyObjectAsMonitor) {
            super();
            this.anyObjectAsMonitor = anyObjectAsMonitor;
        }

        @Override
        public void run() {
            super.run();
            try {
                anyObjectAsMonitor.setValue("a", "aa");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class AnyObjectAsMonitorThreadB extends Thread {
        private AnyObjectAsMonitor anyObjectAsMonitor;

        public AnyObjectAsMonitorThreadB(AnyObjectAsMonitor anyObjectAsMonitor) {
            super();
            this.anyObjectAsMonitor = anyObjectAsMonitor;
        }

        @Override
        public void run() {
            super.run();
            try {
                anyObjectAsMonitor.setValue("b", "bb");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注意 {@link AnyObjectAsMonitor#setValue(java.lang.String, java.lang.String)} 方法中的对象监视器 strAsMonitor 所处的位置
     *
     * @throws IOException
     */
    @Test
    public void testAnyObjectAsMonitor01() throws IOException {
        AnyObjectAsMonitor anyObjectAsMonitor = new AnyObjectAsMonitor();
        AnyObjectAsMonitorThreadA threadA = new AnyObjectAsMonitorThreadA(anyObjectAsMonitor);
        threadA.setName("A");
        threadA.start();
        AnyObjectAsMonitorThreadB threadB = new AnyObjectAsMonitorThreadB(anyObjectAsMonitor);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 验证使用 “synchronized(非 this 对象)同步代码块” 格式时，持有不同的对象监视器是异步的效果。
     * 验证 “synchronized(非 this 对象)同步代码块” 与 synchronized 同步方法是异步调用的效果。
     */
    class TwoMonitorSynchronized {
        private String strAsMonitor = new String();

        public void serviceMethodA() throws InterruptedException {
            synchronized (strAsMonitor) {
                System.out.println("【serviceMethodA()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
                Thread.sleep(3000);
                System.out.println("【serviceMethodA()方法】threadName：" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            }
        }

        public synchronized void serviceMethodB() {
            System.out.println("【serviceMethodB()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
            System.out.println("【serviceMethodB()方法】threadName：" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
        }
    }

    class TwoMonitorSynchronizedThreadA extends Thread {
        private TwoMonitorSynchronized twoMonitorSynchronized;

        public TwoMonitorSynchronizedThreadA(TwoMonitorSynchronized twoMonitorSynchronized) {
            super();
            this.twoMonitorSynchronized = twoMonitorSynchronized;
        }

        @Override
        public void run() {
            super.run();
            try {
                twoMonitorSynchronized.serviceMethodA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class TwoMonitorSynchronizedThreadB extends Thread {
        private TwoMonitorSynchronized twoMonitorSynchronized;

        public TwoMonitorSynchronizedThreadB(TwoMonitorSynchronized twoMonitorSynchronized) {
            super();
            this.twoMonitorSynchronized = twoMonitorSynchronized;
        }

        @Override
        public void run() {
            super.run();
            twoMonitorSynchronized.serviceMethodB();
        }
    }

    /**
     * 由于对象监视器不同，所以运行结果就是异步的。
     * 同步代码块放在非 synchronized 方法中进行声明，并不能保证调用方法的线程的同步性(顺序性)，也就是说线程调用方法的顺序是无序的。
     * 虽然在同步代码块中得执行顺序是同步的，这样极易出现脏读问题。使用 “synchronized(非 this 对象)同步代码块” 也可以解决脏读问题。
     *
     * @throws IOException
     */
    @Test
    public void testTwoMonitorSynchronized01() throws IOException {
        TwoMonitorSynchronized twoMonitorSynchronized = new TwoMonitorSynchronized();
        TwoMonitorSynchronizedThreadA threadA = new TwoMonitorSynchronizedThreadA(twoMonitorSynchronized);
        threadA.setName("A");
        threadA.start();
        TwoMonitorSynchronizedThreadB threadB = new TwoMonitorSynchronizedThreadB(twoMonitorSynchronized);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 验证多个线程调用同一个方法是随机的。
     */
    class ManyThreadCallSameMethodByRandom {
        private List<String> list = new ArrayList<>();

        public synchronized void addElement(String element) {
            System.out.println("【addElement()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
            list.add(element);
            System.out.println("【addElement()方法】threadName：" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
        }

        public synchronized int getSize() {
            System.out.println("【getSize()方法】threadName：" + Thread.currentThread().getName() + "，begin time：" + System.currentTimeMillis());
            int size = list.size();
            System.out.println("【getSize()方法】threadName：" + Thread.currentThread().getName() + "，end   time：" + System.currentTimeMillis());
            return size;
        }
    }

    class ManyThreadCallSameMethodByRandomThreadA extends Thread {
        private ManyThreadCallSameMethodByRandom manyThreadCallSameMethodByRandom;

        public ManyThreadCallSameMethodByRandomThreadA(ManyThreadCallSameMethodByRandom manyThreadCallSameMethodByRandom) {
            super();
            this.manyThreadCallSameMethodByRandom = manyThreadCallSameMethodByRandom;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 100; i++) {
                manyThreadCallSameMethodByRandom.addElement("ThreadA-" + (i + 1));
            }
        }
    }

    class ManyThreadCallSameMethodByRandomThreadB extends Thread {
        private ManyThreadCallSameMethodByRandom manyThreadCallSameMethodByRandom;

        public ManyThreadCallSameMethodByRandomThreadB(ManyThreadCallSameMethodByRandom manyThreadCallSameMethodByRandom) {
            super();
            this.manyThreadCallSameMethodByRandom = manyThreadCallSameMethodByRandom;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i < 100; i++) {
                manyThreadCallSameMethodByRandom.addElement("ThreadB-" + (i + 1));
            }
        }
    }

    /**
     * 从运行结果来看：同步代码块中的代码是同步打印的，当前线程的 begin 和 end 是成对出现的。但线程 A 和线充 B 的执行却是异步的，这就有可能导致脏读。
     * 由于线程执行方法的顺序不确定，所以当 A 和 B 两个线程执行待遇欧分支判断的方法时，就会出现逻辑上的错误，有可能出现脏读。参考案例 {@link SynchronizedTest09#testDirtyReadByBranchJudge01()}
     *
     * @throws IOException
     */
    @Test
    public void testManyThreadCallSameMethodByRandom01() throws IOException {
        ManyThreadCallSameMethodByRandom manyThreadCallSameMethodByRandom = new ManyThreadCallSameMethodByRandom();
        ManyThreadCallSameMethodByRandomThreadA threadA = new ManyThreadCallSameMethodByRandomThreadA(manyThreadCallSameMethodByRandom);
        threadA.setName("A");
        threadA.start();
        ManyThreadCallSameMethodByRandomThreadB threadB = new ManyThreadCallSameMethodByRandomThreadB(manyThreadCallSameMethodByRandom);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }

    /**
     * 分支判断导致脏读
     */
    class DirtyReadByBranchJudge {
        public MyOneList addElement(MyOneList list, String element) throws InterruptedException {
            // 注意使用【代码段一】和【代码段二】程序的输出结果
            // 【代码段一】
            if (list.getSize() < 1) {
                Thread.sleep(2000);// 模拟从远程花费2秒返回数据
                list.addElement(element);
            }
            // 【代码段二】
            /*synchronized (list) {
                if (list.getSize() < 1) {
                    Thread.sleep(2000);// 模拟从远程花费2秒返回数据
                    list.addElement(element);
                }
            }*/
            return list;
        }
    }

    /**
     * 只能存放一个元素的自定义集合工具类
     */
    class MyOneList {
        private List<String> list = new ArrayList<>();

        public synchronized void addElement(String element) {
            list.add(element);
        }

        public synchronized int getSize() {
            return list.size();
        }
    }

    class DirtyReadByBranchJudgeThreadA extends Thread {
        private MyOneList myOneList;

        public DirtyReadByBranchJudgeThreadA(MyOneList myOneList) {
            super();
            this.myOneList = myOneList;
        }

        @Override
        public void run() {
            super.run();
            DirtyReadByBranchJudge dirtyReadByBranchJudge = new DirtyReadByBranchJudge();
            try {
                dirtyReadByBranchJudge.addElement(myOneList, "A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class DirtyReadByBranchJudgeThreadB extends Thread {
        private MyOneList myOneList;

        public DirtyReadByBranchJudgeThreadB(MyOneList myOneList) {
            super();
            this.myOneList = myOneList;
        }

        @Override
        public void run() {
            super.run();
            DirtyReadByBranchJudge dirtyReadByBranchJudge = new DirtyReadByBranchJudge();
            try {
                dirtyReadByBranchJudge.addElement(myOneList, "B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 在 {@link DirtyReadByBranchJudge#addElement(com.study.test.synchronize.SynchronizedTest09.MyOneList, java.lang.String)} 方法中：
     * 使用【代码段一】，会出现脏读，是因为两个线程是以异步的方式返回 list 参数的 size() 大小，解决办法就是同步化。
     * 使用【代码段二】，由于 list 参数对象在项目中是一份实例，是单例的，而且也正需要对  list 参数的 getSize() 方法做同步调用，所以就对 list 参数进行同步处理。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testDirtyReadByBranchJudge01() throws IOException, InterruptedException {
        MyOneList myOneList = new MyOneList();
        DirtyReadByBranchJudgeThreadA threadA = new DirtyReadByBranchJudgeThreadA(myOneList);
        threadA.setName("A");
        threadA.start();
        DirtyReadByBranchJudgeThreadB threadB = new DirtyReadByBranchJudgeThreadB(myOneList);
        threadB.setName("B");
        threadB.start();
        Thread.sleep(6000);
        System.out.println("集合中的元素数：" + myOneList.getSize());
        System.in.read();
    }
}
