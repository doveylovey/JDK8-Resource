package com.study.test.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock 锁和 Condition 条件
 * <p>
 * 当满足一定条件时，调用 Condition 的 await() 方法使当前线程进入休眠状态进行等待，调用 signalAll() 方法可唤醒因调用 await() 而进入休眠状态的线程。
 * <p>
 * synchronized 和 Lock 都是用来在多线程环境下对资源的同步访问进行控制，以避免因多个线程对数据的并发读写造成的数据混乱问题。
 * 与 synchronized 不同的是，Lock 锁实现同步时需要使用者手动控制锁的获取和释放，其灵活性可以实现更复杂的多线程同步和更高的性能，
 * 但使用者必须要在 finally 代码块中显示释放锁。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2021年02月05日
 */
public class ProducerConsumerTest01 {
    public static void main(String[] args) {
        Goods goods = new Goods(10);
        Producer producer = new Producer(goods);
        Customer customer = new Customer(goods);
        List<Thread> list = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            Thread thread = new Thread(producer, "【生产者-" + i + "】");
            list.add(thread);
        }
        for (int i = 1; i < 10; i++) {
            Thread thread = new Thread(customer, "【消费者-" + i + "】");
            list.add(thread);
        }
        for (Thread thread : list) {
            thread.start();
        }
    }

    static class Goods {
        private String goodsName;
        private Integer count = 0;
        private Integer full;
        // 获得 lock 对象
        private Lock lock = new ReentrantLock();
        // 获得生产者等待队列对象
        private Condition producerCondition = lock.newCondition();
        // 获得消费者等待队列对象
        private Condition customerCondition = lock.newCondition();

        public Goods(Integer full) {
            this.full = full;
        }

        public void putGoods(String goodsName) {
            // 使用锁实现同步，获取所得操作，当锁被其他线程占用时，当前线程将进入休眠
            lock.lock();
            try {
                while (this.count >= this.full) {
                    System.out.println("库存充足，无需生产");
                    try {
                        // 满足条件时，线程休眠并释放锁；当调用 signalAll() 时，线程唤醒并重新获得锁
                        producerCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.goodsName = goodsName;
                this.count++;
                System.out.println(Thread.currentThread().getName() + "生产：" + this.toString());
                // 唤醒因 producerCondition.await() 休眠的线程
                customerCondition.signalAll();
            } finally {
                // 解锁
                lock.unlock();
            }
        }

        public void takeGoods() {
            lock.lock();
            try {
                while (this.count <= 0) {
                    System.err.println("库存不足，正在生产");
                    try {
                        customerCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.count--;
                System.out.println(Thread.currentThread().getName() + "消费：" + this.toString());
                // 这个时候已经生产完了，生产者唤醒消费者进行消费
                producerCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }

        @Override
        public String toString() {
            return "goodsName=" + goodsName + "，count=" + count;
        }
    }

    static class Producer implements Runnable {
        private Goods goods;

        public Producer(Goods goods) {
            this.goods = goods;
        }

        @Override
        public void run() {
            while (true) {
                this.goods.putGoods("玫瑰花");
            }
        }
    }

    static class Customer implements Runnable {
        private Goods goods;

        public Customer(Goods goods) {
            this.goods = goods;
        }

        @Override
        public void run() {
            while (true) {
                this.goods.takeGoods();
            }
        }
    }
}
