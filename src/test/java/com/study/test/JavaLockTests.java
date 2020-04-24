package com.study.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JavaLockTests {
}

class ReentrantLockTests01 {
    private List<Integer> list01 = new ArrayList<>();

    public void insert01(Thread thread) {
        Lock lock = new ReentrantLock();// 注意这个地方
        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            for (int i = 0; i < 5; i++) {
                list01.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(thread.getName() + "获取锁异常！");
        } finally {
            lock.unlock();
            System.out.println(thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }

    /**
     * 多执行几次：可能会出现 后面的线程 在 前面的线程 释放锁之前获得锁。
     * 原因：在 insert01() 方法中的 lock 变量是局部变量，每个线程执行该方法时都会保存一个副本，那么理所当然每个线程执行到 lock.lock() 处获取的是不同的锁，所以就不会发生冲突。
     * 改进：只需要将 lock 声明为类的属性即可。參考 {@link ReentrantLockTests02#main(String[])}
     */
    public static void main(String[] args) {
        final ReentrantLockTests01 lockTest01 = new ReentrantLockTests01();

        new Thread(() -> {
            lockTest01.insert01(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest01.insert01(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest01.insert01(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest01.insert01(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest01.insert01(Thread.currentThread());
        }).start();
    }
}

class ReentrantLockTests02 {
    private List<Integer> list02 = new ArrayList<>();
    Lock lock = new ReentrantLock();// 注意这个地方

    public void insert02(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName() + "得到了锁");
            for (int i = 0; i < 5; i++) {
                list02.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(thread.getName() + "获取锁异常！");
        } finally {
            lock.unlock();
            System.out.println(thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }

    /**
     * {@link ReentrantLockTests01#main(String[])} 改进版
     */
    public static void main(String[] args) {
        final ReentrantLockTests02 lockTest02 = new ReentrantLockTests02();

        new Thread(() -> {
            lockTest02.insert02(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest02.insert02(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest02.insert02(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest02.insert02(Thread.currentThread());
        }).start();

        new Thread(() -> {
            lockTest02.insert02(Thread.currentThread());
        }).start();
    }
}

class TryLockTests {
    private List<Integer> list = new ArrayList<>();
    Lock lock = new ReentrantLock();// 注意这个地方

    public void insert(Thread thread) {
        /**
         * tryLock() 使用方式
         */
        if (lock.tryLock()) {
            try {
                System.out.println(thread.getName() + "得到了锁");
                for (int i = 0; i < 5; i++) {
                    list.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
                System.err.println(thread.getName() + "获取锁异常！");
            } finally {
                lock.unlock();
                System.out.println(thread.getName() + "执行了 finally 代码块，释放了锁");
            }
        } else {
            System.out.println(thread.getName() + "没有获取到锁……");
        }
    }

    public static void main(String[] args) {
        final TryLockTests tryLockTests = new TryLockTests();

        new Thread(() -> {
            tryLockTests.insert(Thread.currentThread());
        }).start();

        new Thread(() -> {
            tryLockTests.insert(Thread.currentThread());
        }).start();

        new Thread(() -> {
            tryLockTests.insert(Thread.currentThread());
        }).start();

        new Thread(() -> {
            tryLockTests.insert(Thread.currentThread());
        }).start();

        new Thread(() -> {
            tryLockTests.insert(Thread.currentThread());
        }).start();
    }
}

class LockInterruptiblyTests {
    private List<Integer> list = new ArrayList<>();
    Lock lock = new ReentrantLock();// 注意这个地方

    public void insert(Thread thread) throws InterruptedException {
        /**
         * 注意，如果需要正确中断等待锁的线程，必须将获取锁放在外面，然后将 InterruptedException 抛出
         */
        lock.lockInterruptibly();
        try {
            System.out.println(thread.getName() + "得到了锁");
            long start = System.currentTimeMillis();
            for (; ; ) {
                if (System.currentTimeMillis() - start > Integer.MAX_VALUE) {
                    // 插入数据
                    break;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println(thread.getName() + "获取锁异常！");
        } finally {
            lock.unlock();
            System.out.println(thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }

    /**
     * 运行之后，发现 Thread-1 能够被正确中断。
     *
     * @param args
     */
    public static void main(String[] args) {
        LockInterruptiblyTests test = new LockInterruptiblyTests();
        MyThread thread1 = new MyThread(test);
        MyThread thread2 = new MyThread(test);
        thread1.start();
        thread2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread2.interrupt();
    }

    static class MyThread extends Thread {
        private LockInterruptiblyTests test;

        public MyThread(LockInterruptiblyTests test) {
            this.test = test;
        }

        @Override
        public void run() {
            try {
                test.insert(Thread.currentThread());
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "被中断");
            }
        }
    }
}

class ReentrantReadWriteLockTests01 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final ReentrantReadWriteLockTests01 test = new ReentrantReadWriteLockTests01();
        /**
         * 总结来说，Lock 和 synchronized 有以下几点不同：
         * 1、Lock 是一个接口，而 synchronized 是 Java 中的关键字，synchronized 是内置的语言实现。
         * 2、synchronized 在发生异常时会自动释放线程占有的锁，因此不会导致死锁现象发生；而 Lock 在发生异常时，如果没有主动通过 unLock() 去释放锁，则很可能造成死锁现象，因此使用 Lock 时需要在 finally 块中释放锁。
         * 3、Lock 可以让等待锁的线程响应中断，而 synchronized 却不行。使用 synchronized 时，等待的线程会一直等待下去，不能够响应中断。
         * 4、通过 Lock 可以知道有没有成功获取锁，而 synchronized 却无法办到。
         * 5、Lock 可以提高多个线程进行读操作的效率。
         *
         * Lock 和 synchronized 的选择(从性能上来说)：
         * 如果竞争资源不激烈，两者的性能是差不多的。
         * 当竞争资源非常激烈时(即有大量线程同时竞争)，此时 Lock 的性能要远远优于 synchronized。
         * 所以在具体使用时要根据实际情况选择。
         */
        new Thread(() -> test.getBySynchronized(Thread.currentThread())).start();
        new Thread(() -> test.getBySynchronized(Thread.currentThread())).start();

//        new Thread(() -> test.getByReadLock(Thread.currentThread())).start();
//        new Thread(() -> test.getByReadLock(Thread.currentThread())).start();
    }

    /**
     * 多执行几次，试试加 synchronized 和不加 synchronized 的区别
     * <p>
     * 结论：
     * 加 synchronized 输出结果：直到第一个线程执行完读操作之后，才会打印第二个线程执行读操作的信息
     * 不加 synchronized 输出结果：第一个线程和第二个线程执行读操作的打印信息顺序不固定
     *
     * @param thread
     */
    public /*synchronized*/ void getBySynchronized(Thread thread) {
        System.out.println("【synchronized】" + thread.getName() + "开始读操作");
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start <= 1) {
            System.out.println("【synchronized】" + thread.getName() + "正在进行读操作……");
        }
        System.out.println("【synchronized】" + thread.getName() + "读操作结束");
    }

    /**
     * 使用读写锁：多个线程可以同时进行读操作，这样就大大提升了读操作的效率。
     * 注意(关于 ReentrantReadWriteLock 类中的其他方法可以查阅 API 文档)：
     * 1、如果有一个线程已经占用了读锁，则此时其他线程如果要申请写锁，则申请写锁的线程会一直等待释放读锁。
     * 2、如果有一个线程已经占用了写锁，则此时其他线程如果申请写锁或者读锁，则申请的线程会一直等待释放写锁。
     *
     * @param thread
     */
    public void getByReadLock(Thread thread) {
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try {
            System.out.println("【ReentrantReadWriteLock】" + thread.getName() + "读操作开始");
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println("【ReentrantReadWriteLock】" + thread.getName() + "正在进行读操作……");
            }
            System.out.println("【ReentrantReadWriteLock】" + thread.getName() + "读操作结束");
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("【ReentrantReadWriteLock】" + thread.getName() + "获取锁异常！");
        } finally {
            readLock.unlock();
            System.out.println("【ReentrantReadWriteLock】" + thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }
}

class ReentrantReadWriteLockTests02 {
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) {
        final ReentrantReadWriteLockTests02 test = new ReentrantReadWriteLockTests02();

        new Thread(() -> test.readLock(Thread.currentThread())).start();
        new Thread(() -> test.readLock(Thread.currentThread())).start();
        new Thread(() -> test.readLock(Thread.currentThread())).start();
        new Thread(() -> test.readLock(Thread.currentThread())).start();
        new Thread(() -> test.readLock(Thread.currentThread())).start();
    }

    public void readLock(Thread thread) {
        ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
        readLock.lock();
        try {
            System.out.println("【读】" + thread.getName() + "操作开始");
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println("【读】" + thread.getName() + "正在进行操作……");
            }
            System.out.println("【读】" + thread.getName() + "操作结束");
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("【读】" + thread.getName() + "获取锁异常！");
        } finally {
            readLock.unlock();
            System.out.println("【读】" + thread.getName() + "执行了 finally 代码块，释放了锁");
        }

        ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
        writeLock.lock();
        try {
            System.out.println("【写】" + thread.getName() + "操作开始");
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println("【写】" + thread.getName() + "正在进行操作……");
            }
            System.out.println("【写】" + thread.getName() + "操作结束");
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("【写】" + thread.getName() + "获取锁异常！");
        } finally {
            writeLock.unlock();
            System.out.println("【写】" + thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }
}

class ReentrantReadWriteLockTests03 {
    // 共享数据，只能有一个线程能写该数据，但可以有多个线程同时读该数据
    private Object data = null;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    // 读数据
    public void read(Thread thread) {
        // 加读锁
        lock.readLock().lock();
        try {
            System.out.println("【读】" + thread.getName() + "准备读取数据……");
            Thread.sleep((long) (Math.random() * 1000));
            System.out.println("【读】" + thread.getName() + "读取到了数据：" + data);
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("【读】" + thread.getName() + "获取锁异常！");
        } finally {
            // 释放读锁
            lock.readLock().unlock();
            System.out.println("【读】" + thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }

    // 写数据
    public void write(Thread thread, Object data) {
        // 加写锁
        lock.writeLock().lock();
        try {
            System.out.println("【写】" + thread.getName() + "准备写入数据……");
            Thread.sleep((long) (Math.random() * 1000));
            this.data = data;
            System.out.println("【写】" + thread.getName() + "写入的数据：" + data);
        } catch (Exception e) {
            // TODO: handle exception
            System.err.println("【写】" + thread.getName() + "获取锁异常！");
        } finally {
            // 释放写锁
            lock.writeLock().unlock();
            System.out.println("【写】" + thread.getName() + "执行了 finally 代码块，释放了锁");
        }
    }

    public static void main(String[] args) {
        final ReentrantReadWriteLockTests03 tests03 = new ReentrantReadWriteLockTests03();
        // 启动3组线程：每组1个读线程、1个写线程
        for (int i = 0; i < 3; i++) {
            // 启动1个读线程
            new Thread(() -> {
                while (true) {
                    tests03.read(Thread.currentThread());
                }
            }).start();
            // 启动1个写线程
            new Thread(() -> {
                while (true) {
                    tests03.write(Thread.currentThread(), new Random().nextInt(10000));
                }
            }).start();
        }
    }
}
