package com.study.test.temp;

import java.util.concurrent.locks.StampedLock;

public class StampedLockTests {
}

class Point {
    // 成员变量
    private double x;
    private double y;
    // 锁实例
    private final StampedLock stampedLock = new StampedLock();

    // 排它锁-写锁（writeLock）
    public void move(double deltaX, double deltaY) {
        long writeLock = stampedLock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            stampedLock.unlockWrite(writeLock);
        }
    }

    // 一个只读方法：其中存在乐观读锁到悲观读锁的转换
    public double distanceFromOrigin() {
        // 尝试获取乐观读锁
        long optimisticRead = stampedLock.tryOptimisticRead();
        // 将全部变量拷贝到方法体栈内
        double currentX = x, currentY = y;
        // 检查在获取到读锁stamp后，锁有没被其他写线程抢占
        if (!stampedLock.validate(optimisticRead)) {
            // 如果被抢占则获取一个共享读锁（悲观获取）
            optimisticRead = stampedLock.readLock();
            try {
                // 将全部变量拷贝到方法体栈内
                currentX = x;
                currentY = y;
            } finally {
                // 释放共享读锁
                stampedLock.unlockRead(optimisticRead);
            }
        }
        // 返回计算结果
        return Math.sqrt(currentX * currentX + currentY * currentY);
    }

    // 获取读锁，并尝试转换为写锁
    public void moveIfAtOrigin(double newX, double newY) {
        long optimisticRead = stampedLock.tryOptimisticRead();
        try {
            // 如果当前点在原点则移动
            while (x == 0.0 && y == 0.0) {
                // 尝试将获取的读锁升级为写锁
                long writeLock = stampedLock.tryConvertToWriteLock(optimisticRead);
                // 升级成功，则更新stamp，并设置坐标值，然后退出循环
                if (writeLock != 0L) {
                    optimisticRead = writeLock;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    // 读锁升级写锁失败则释放读锁，显示获取独占写锁，然后循环重试
                    stampedLock.unlockRead(optimisticRead);
                    optimisticRead = stampedLock.writeLock();
                }
            }
        } finally {
            stampedLock.unlock(optimisticRead);
        }
    }
}