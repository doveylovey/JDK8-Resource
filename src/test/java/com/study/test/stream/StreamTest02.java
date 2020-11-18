package com.study.test.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamTest02 {
    private static List<OrderItem> orderItems = new ArrayList<>();

    static {
        orderItems.add(new OrderItem(1, "牛仔裤", 10));
        orderItems.add(new OrderItem(1, "牛仔裤", 10));
        orderItems.add(new OrderItem(1, "牛仔衣", 5));
        orderItems.add(new OrderItem(1, "牛仔衣", 5));
        orderItems.add(new OrderItem(1, "袜子", 5));
        orderItems.add(new OrderItem(1, "袜子", 5));
        orderItems.add(new OrderItem(2, "风衣", 10));
        orderItems.add(new OrderItem(2, "马丁靴", 5));
        orderItems.add(new OrderItem(3, "休闲裤", 10));
        orderItems.add(new OrderItem(3, "运动鞋", 5));
    }

    @Test
    public void test01() {
        Map<StatisticsTemp, OrderItem> hashMap = new HashMap<>();
        orderItems.forEach(orderItem -> {
            StatisticsTemp temp = new StatisticsTemp(orderItem.getUserId(), orderItem.getProductName());
            if (hashMap.containsKey(temp)) {
                // 注意必须重写 StatisticsTemp 中的 equals() 和 hashCode() 方法
                orderItem.setPurchaseNum(hashMap.get(temp).getPurchaseNum() + orderItem.getPurchaseNum());
            }
            hashMap.put(temp, orderItem);
        });

        List<OrderItem> resultList = new ArrayList<>();
        for (Map.Entry<StatisticsTemp, OrderItem> entry : hashMap.entrySet()) {
            resultList.add(entry.getValue());
        }
        resultList.sort(Comparator.comparing(OrderItem::getUserId).thenComparing(OrderItem::getProductName));
        resultList.forEach(System.out::println);
    }

    @Test
    public void test02() {
        List<OrderItem> resultList = new ArrayList<>();
        orderItems.parallelStream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getUserId() + orderItem.getProductName(), Collectors.toList()))
                .forEach((id, transfer) -> transfer.stream().reduce((a, b) -> new OrderItem(a.getUserId(), a.getProductName(), a.getPurchaseNum() + b.getPurchaseNum())).ifPresent(resultList::add));
        resultList.sort(Comparator.comparing(OrderItem::getUserId).thenComparing(OrderItem::getProductName));
        resultList.forEach(System.out::println);
    }
}

@Data
@Builder
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
class OrderItem {
    private Integer userId;
    private String productName;
    private Integer purchaseNum;
}

@Data
@Builder
//@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
class StatisticsTemp {
    private Integer userId;
    private String productName;
}