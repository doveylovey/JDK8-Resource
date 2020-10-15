package com.study.test.async;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Java8 新的异步编程方式
 */
public class CompletableFutureTests {
    @Test
    public void runAsyncTest() {
        System.out.println("<--------------------【开始】无返回值的异步方法-------------------->");
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("无返回值的异步方法0 ==》 " + i);
            }
        });
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("无返回值的异步方法1 ==》 " + i);
            }
        });
        CompletableFuture<Void> future2 = CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("无返回值的异步方法2 --> " + i);
            }
        });
        for (int i = 0; i < 100; i++) {
            System.out.println("》》》》》调用 get() 前：" + i);
        }
        try {
            System.out.println(future1.get());
            future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("》》》》》调用 get() 后：" + i);
        }
    }

    @Test
    public void supplyAsyncTest() {
        System.out.println("<--------------------【开始】有返回值的异步方法-------------------->");
        CompletableFuture<List<String>> future1 = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("有返回值的异步方法1 ==》 " + i);
            }
            return Arrays.asList("a", "b", "c", "d", "e", "f");
        });
        CompletableFuture<List<String>> future2 = CompletableFuture.supplyAsync(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println("有返回值的异步方2 --> " + i);
            }
            return Arrays.asList("a1", "b1", "c1", "d1", "e1", "f1");
        });
        for (int i = 0; i < 100; i++) {
            System.out.println("》》》》》调用 get() 前：" + i);
        }
        List<String> result1 = new ArrayList<>();
        List<String> result2 = new ArrayList<>();
        try {
            result1 = future1.get();
            result2 = future2.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 100; i++) {
            System.out.println("》》》》》调用 get() 后：" + i);
        }
        System.out.println(result1);
        System.out.println(result2);
    }
}
