package com.study.test.temp;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTests {
    @Test
    public void test01() {
        MyCalculator calculator = new MyCalculator();
        Future<Integer> future = calculator.calculate(10);
        try {
            //future.cancel(true);
            System.out.println("=====>" + future.get(100, TimeUnit.SECONDS));
            while (!future.isDone()) {
                System.out.println("计算中……");
                TimeUnit.SECONDS.sleep(1);
            }
            Integer result = future.get();
            System.out.println("结算完成，结果：" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}

class MyCalculator {
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public Future<Integer> calculate(Integer input) {
        return executor.submit(() -> {
            // Thread.sleep(1000);
            TimeUnit.SECONDS.sleep(3);
            return input * input;
        });
    }
}
