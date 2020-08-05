package com.study.test.synchronize;

import org.junit.Test;

import java.io.IOException;

/**
 * 作用描述：出现异常，锁自动释放。
 * 当一个线程执行的代码出现异常时，其所持有的锁会自动释放。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月05日
 */
public class SynchronizedTest07 {
    class ReleaseLockWhenException {
        public synchronized void testMethod() {
            if ("A".equalsIgnoreCase(Thread.currentThread().getName())) {
                System.out.println("【A Thread】threadName=" + Thread.currentThread().getName() + "，beginTime=" + System.currentTimeMillis());
                int i = 1;
                while (i == 1) {
                    if ("0.123456".equals((Math.random() + "").substring(0, 8))) {
                        System.out.println("【A Thread】threadName=" + Thread.currentThread().getName() + "，exceptionTime=" + System.currentTimeMillis());
                        Integer.parseInt("a");
                    }
                }
            } else {
                System.out.println("【OtherThread】threadName=" + Thread.currentThread().getName() + "，beginTime=" + System.currentTimeMillis());
            }
        }
    }

    class ReleaseLockWhenExceptionThreadA extends Thread {
        private ReleaseLockWhenException releaseLockWhenException;

        public ReleaseLockWhenExceptionThreadA(ReleaseLockWhenException releaseLockWhenException) {
            this.releaseLockWhenException = releaseLockWhenException;
        }

        @Override
        public void run() {
            releaseLockWhenException.testMethod();
        }
    }

    class ReleaseLockWhenExceptionThreadB extends Thread {
        private ReleaseLockWhenException releaseLockWhenException;

        public ReleaseLockWhenExceptionThreadB(ReleaseLockWhenException releaseLockWhenException) {
            this.releaseLockWhenException = releaseLockWhenException;
        }

        @Override
        public void run() {
            releaseLockWhenException.testMethod();
        }
    }

    /**
     * 线程 A 出现异常并释放锁，线程 B 进入方法并正常打印。
     * 由此可得出结论：当一个线程执行的代码出现异常时，其所持有的锁会自动释放。
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testReleaseLockWhenException01() throws IOException, InterruptedException {
        ReleaseLockWhenException releaseLockWhenException = new ReleaseLockWhenException();
        ReleaseLockWhenExceptionThreadA threadA = new ReleaseLockWhenExceptionThreadA(releaseLockWhenException);
        threadA.setName("A");
        threadA.start();
        Thread.sleep(500);
        ReleaseLockWhenExceptionThreadB threadB = new ReleaseLockWhenExceptionThreadB(releaseLockWhenException);
        threadB.setName("B");
        threadB.start();
        System.in.read();
    }
}
