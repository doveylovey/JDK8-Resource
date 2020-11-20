package com.study.test.designpattern;

import org.junit.Test;

/**
 * 适配器模式的特点在于兼容，从代码上的特点来说，适配类与原有的类具有相同的接口，并且持有新的目标对象。
 * 装饰器模式的特点在于增强，它的特点是被装饰类和所有的装饰类必须实现同一个接口，而且必须持有被装饰的对象，可以无限装饰。
 * 代理模式的特点在于隔离，隔离调用类和被调用类的关系，通过一个代理类去调用。
 * <p>
 * 总的来说就是如下三句话：
 * 适配器模式是将一个类 A 通过某种方式转换成另一个类 B。
 * 装饰模式是在一个原有类 A 的基础上增加某些新功能变成另一个类 B。
 * 代理模式是将一个类 A 转换成具体的操作类 B。
 */
public class AllTests {
    @Test
    public void testSource() {
        SourceOrder sourceOrder = new SourceOrderImpl();
        sourceOrder.updateDate("123456", "2020-12-25", "user");
    }

    @Test
    public void testAdapter() {
        SourceOrderAdapter sourceOrderAdapter = new SourceOrderAdapterImpl();
        sourceOrderAdapter.updateDate("123456", "user");
    }

    @Test
    public void testProxy() {
        SourceOrder sourceOrder = new SourceOrderProxy();
        sourceOrder.updateDate("123456", "2020-12-25", "user");
        sourceOrder.updateDate("123456", "2020-12-25", "admin");
    }

    @Test
    public void testDecorator() {
        SourceOrder sourceOrder = new SourceOrderDecorator(new SourceOrderImpl());
        sourceOrder.updateDate("123456", "2020-12-25", "user");
    }
}

/**
 * 原接口，需要传入 orderId、时间、客户端
 */
interface SourceOrder {
    void updateDate(String orderId, String date, String client);
}

class SourceOrderImpl implements SourceOrder {
    @Override
    public void updateDate(String orderId, String date, String client) {
        System.out.println(client + " 已将订单 " + orderId + " 的有效期延长至 " + date);
    }
}

/**
 * 适配器模式
 */
interface SourceOrderAdapter {
    void updateDate(String orderId, String client);
}

class SourceOrderAdapterImpl implements SourceOrderAdapter {
    SourceOrder sourceOrder;

    public SourceOrderAdapterImpl() {
        sourceOrder = new SourceOrderImpl();
    }

    @Override
    public void updateDate(String orderId, String client) {
        // 适配的方式随意，但需保证完全兼容原有的，即保证调用原接口
        sourceOrder.updateDate(orderId, "2020-12-31", client);
    }
}

/**
 * 代理模式
 * <p>
 * 代理模式和适配器模式的区别：代理模式与原对象实现同一个接口，而适配器模式需实现新接口。
 */
class SourceOrderProxy implements SourceOrder {
    SourceOrder sourceOrder;

    public SourceOrderProxy() {
        sourceOrder = new SourceOrderImpl();
    }

    @Override
    public void updateDate(String orderId, String date, String client) {
        // 进行判断，如果是 admin 则更新否则让其输入账号密码
        if ("admin".equals(client)) {
            sourceOrder.updateDate(orderId, date, client);
        } else {
            System.out.println("账号不是 admin，没有查询权限！");
        }
    }
}

/**
 * 装饰器模式：必须要有被装饰的类和装饰的类。
 * <p>
 * 装饰器模式和代理模式的区别：代理模式本身持有原对象，不需要从外部传入；而装饰模式需要从外部传入原对象，并且可以没有顺序。
 * 从使用上来看：代理模式注重的是隔离限制，让外部不能访问你实际的调用对象(如权限控制)，装饰模式注重的是功能拓展，在同一个方法下实现更多功能。
 */
class SourceOrderDecorator implements SourceOrder {
    SourceOrder sourceOrder;

    public SourceOrderDecorator(SourceOrder sourceOrder) {
        this.sourceOrder = sourceOrder;
    }

    @Override
    public void updateDate(String orderId, String date, String client) {
        sourceOrder.updateDate(orderId, date, client);
        System.out.println(client + " 已将订单 " + orderId + " 的退款期延长至 " + date);
    }
}
