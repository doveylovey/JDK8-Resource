package com.study.test.async;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class TestFuture {
    @Test
    public void futureCallableTest01() {
        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> result = executor.submit(new Callable<String>() {
            public String call() throws Exception {
                return "Future 和 Callable 用法示例1";
            }
        });
        executor.shutdown();
        try {
            System.out.println("执行结果：" + result.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void futureCallableTest02() {
        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Future 和 Callable 用法示例2";
            }
        });
        new Thread(task).start();
        try {
            System.out.println("执行结果：" + task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void futureCallableTest03() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<String> task = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Future 和 Callable 用法示例3";
            }
        });
        executor.submit(task);
        try {
            System.out.println("执行结果：" + task.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
