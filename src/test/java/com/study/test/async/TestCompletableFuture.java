package com.study.test.async;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class TestCompletableFuture {
    /**
     * CompletableFuture 变换结果
     */
    @Test
    public void completableFutureTest01() {
        String result = CompletableFuture.supplyAsync(() -> {
            return "Hello ";
        }).thenApplyAsync(v -> v + "world").join();
        System.out.println("CompletableFuture 变换结果：" + result);
    }

    /**
     * CompletableFuture 消费结果
     */
    @Test
    public void completableFutureTest02() {
        CompletableFuture.supplyAsync(() -> {
            return "Hello world";
        }).thenAccept(v -> {
            System.out.println("CompletableFuture 消费结果：" + v);
        });
    }

    /**
     * CompletableFuture 结合两个 CompletionStage 的结果，进行转化后返回
     */
    @Test
    public void completableFutureTest03() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "world";
        }), (s1, s2) -> {
            return s1 + " " + s2;
        }).join();
        System.out.println("CompletableFuture 结合两个 CompletionStage 的结果，进行转化后返回：" + result);
    }

    /**
     * 两个 CompletionStage 谁计算的快，就用那个 CompletionStage 的结果进行下一步的处理
     */
    @Test
    public void completableFutureTest04() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hi Boy";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hi Girl";
        }), (s) -> {
            return s;
        }).join();
        System.out.println("两个 CompletionStage 谁计算的快，就用那个 CompletionStage 的结果进行下一步的处理：" + result);
    }

    /**
     * 运行时出现了异常可以通过 exceptionally() 进行补偿
     */
    @Test
    public void completableFutureTest05() {
        String result = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (true) {
                throw new RuntimeException("exception test!");
            }
            return "Hi Boy";
        }).exceptionally(e -> {
            System.out.println(e.getMessage());
            return "Hello world!";
        }).join();
        System.out.println(result);
    }
}
