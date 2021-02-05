package com.study.test.concurrent;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 浅谈 synchronized：
 * synchronized 是 Java 的一个关键字，也就是 Java 语言内置的特性，如果一个代码块被 synchronized 修饰，
 * 当一个线程获取到锁执行代码块时，其他线程只能一直等待获取锁的线程释放锁，已得到锁的线程释放锁会有三种情况：
 * 1、已得到锁的线程执行完该代码块，线程释放对锁的占有
 * 2、已得到锁的线程执行发生异常，此时 JVM 会让该线程自动释放锁
 * 3、调用 wait() 方法，在等待的时候立即释放锁，方便其他的线程使用锁
 * <p>
 * Lock 的特性：
 * 1、Lock 不是 Java 语言内置的
 * 2、synchronized 是在 JVM 层面上实现的，如果代码执行出现异常，JVM 会自动释放锁，而 Lock 不行，要保证锁一定会被释放就必须将 unLock 放到 finally 代码块中(手动释放)
 * 3、在资源竞争不是很激烈的情况下，synchronized 的性能要优于 ReentrantLock，但是在很激烈的情况下，synchronized 的性能会下降几十倍
 * 4、ReentrantLock 增加了锁：
 * void lock();// 无条件的锁
 * void lockInterruptibly() throws InterruptedException;// 可中断的锁，如果获取到锁则立即返回，如果没有获取到锁则处于休眠状态，直到获得锁或被别的线程中断去做其他的事情。但如果是 synchronized 的话，没有获取到锁则会一直等待下去
 * boolean tryLock();// 如果获取到锁则立即返回 true，否则立即(不会等待)返回 false
 * boolean tryLock(long timeout,TimeUnit unit);// 如果获取到锁则立即返回 true，否则等待参数给定的时间，在等待过程中如果获取到锁则返回 true，等待超时则返回 false
 * <p>
 * Condition 的特性：
 * 1、Condition 中的 await() 方法相当于 Object 中的 wait() 方法，Condition 中的 signal() 方法相当于 Object 中的 notify() 方法，Condition 中的 signalAll() 相当于 Object 中的 notifyAll() 方法。区别：Object 中的这些方法是和同步锁一起使用的；而 Condition 是需要与互斥锁/共享锁一起使用的
 * 2、Condition 更强大的地方在于：能够更加精细的控制多线程的休眠与唤醒。对于同一个锁，可以创建多个 Condition，在不同情况下使用不同的 Condition。
 * 例如，假如多线程读写同一个缓冲区：当向缓冲区中写入数据后唤醒"读线程"，当从缓冲区中读出数据后唤醒"写线程"，并且当缓冲区满时"写线程"需要等待；当缓冲区空时"读线程"需要等待。
 * 如果用 Object 类中的 wait()、notify()、notifyAll() 实现该缓冲区，当向缓冲区写入数据后需要唤醒"读线程"时，不可能通过 notify() 或 notifyAll() 明确的指定唤醒"读线程"，而只能通过 notifyAll() 唤醒所有线程(但无法区分唤醒的是读线程还是写线程)。通过 Condition 就能明确的指定唤醒读线程。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class LockConditionTest01 {
    public static void main(String[] args) {
        Task task = new Task();
        for (int i = 0; i < 20; i++) {
            new Thread(new AddThread(task), "AddThread-" + i).start();
        }
        for (int i = 0; i < 20; i++) {
            new Thread(new SubThread(task), "SubThread-" + i).start();
        }
    }

    static class AddThread implements Runnable {
        private Task task;

        public AddThread(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.add();
        }
    }

    static class SubThread implements Runnable {
        private Task task;

        public SubThread(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.sub();
        }
    }

    static class Task {
        private final Lock lock = new ReentrantLock();
        private final Condition addCondition = lock.newCondition();
        private final Condition subCondition = lock.newCondition();
        private static int num = 0;
        private List<String> list = new LinkedList<>();

        public void add() {
            lock.lock();
            try {
                while (list.size() >= 5) {
                    // 当集合已满，则"添加"线程等待
                    addCondition.await();
                }
                num++;
                String element = "添加元素-" + num;
                list.add(element);
                System.out.println("当前线程：" + Thread.currentThread().getName() + "，添加元素：" + element + "，集合大小：" + list.size());
                this.subCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {//释放锁
                lock.unlock();
            }
        }

        public void sub() {
            lock.lock();
            try {
                while (list.isEmpty()) {
                    // 当集合为空时，"减少"线程等待
                    subCondition.await();
                }
                String element = list.get(0);
                list.remove(0);
                System.out.println("当前线程：" + Thread.currentThread().getName() + "，移除元素：" + element + "，集合大小：" + list.size());
                num--;
                addCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
