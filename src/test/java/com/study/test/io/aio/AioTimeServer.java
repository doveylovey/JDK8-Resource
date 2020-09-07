package com.study.test.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

/**
 * NIO 2.0 引入了新的异步通道的概念，并提供了异步文件通道和异步套接字通道的实现，异步通道提供两种方式获取操作结果：
 * 1、通过 java.util.concurrent.Future 类来表示异步操作的结果。
 * 2、在执行异步操作的时候传入一个 java.nio.channels.CompletionHandler  接口的实现类作为操作完成的回调。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年09月07日
 */
public class AioTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        AioTimeServerHandler handler = new AioTimeServerHandler(port);
        new Thread(handler, "aio-server-01").start();
    }
}

class AioTimeServerHandler implements Runnable {
    private int serverPort;
    CountDownLatch countDownLatch;
    AsynchronousServerSocketChannel asyncServerSocketChannel;

    public AioTimeServerHandler(int serverPort) {
        this.serverPort = serverPort;
        try {
            // 创建一个异步的服务端通道 AsynchronousServerSocketChannel，然后调用其 bind() 方法绑定监听端口，若端口合法且未被占用，则绑定成功
            asyncServerSocketChannel = AsynchronousServerSocketChannel.open();
            asyncServerSocketChannel.bind(new InetSocketAddress(serverPort));
            System.out.println("【AIO】time server is start in port：" + serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 初始化一个 CountDownLatch 对象，其作用是：在完成一组正在执行的操作之前，允许当前线程一直阻塞。
        // 注意：在本例中，让线程在此阻塞以防止服务端执行完成退出。在实际项目中，无需启动独立的线程来处理 AsynchronousServerSocketChannel
        countDownLatch = new CountDownLatch(1);
        // 接收客户端连接：异步操作
        this.doAccept();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        asyncServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}

class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AioTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AioTimeServerHandler attachment) {
        // 从 AioTimeServerHandler 获取其成员变量 AsynchronousServerSocketChannel，再调用其 accept() 方法
        // 既然接收客户端已经成功了，为什么还要再次调用 accept() 方法呢？原因如下：
        // 当调用 AsynchronousServerSocketChannel 的 accept() 方法后，如果有新的客户端连接接入，系统将回调我们传入的
        // CompletionHandler 实例的 completed() 方法，表示新的客户端已经接入成功，因为一个 AsynchronousServerSocketChannel
        // 可以接收成千上万个客户端，所以需要继续调用它的 accept() 方法，接收其他的客户端连接，最终形成一个循环。
        // 每当接收一个客户端连接成功后，再异步接收新的客户端连接。
        attachment.asyncServerSocketChannel.accept(attachment, this);
        // 链路建立成功之后，服务端需要接收客户端的请求消息。
        // 创建一个新的 ByteBuffer，预分配 1M 的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 调用 AsynchronousSocketChannel 的 read() 方法进行异步读操作。read() 方法参数详解：
        // ByteBuffer dst：接收缓冲区，用于从异步 Channel 中读取数据包。
        // A attachment：异步 Channel 携带的附件，通知回调的时候作为入参使用。
        // CompletionHandler<Integer,? super A> handler：接收通知回调的业务 handler，本例中为 ReadCompletionHandler。
        result.read(buffer, buffer, new ReadCompletionHandler(result));
    }

    @Override
    public void failed(Throwable exc, AioTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.countDownLatch.countDown();
    }
}

class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel asyncSocketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel asyncSocketChannel) {
        if (this.asyncSocketChannel == null) {
            // 将 AsynchronousSocketChannel 通过参数传递到 ReadCompletionHandler 中当做成员变量使用，主要用于读取半包消息和发送应答
            this.asyncSocketChannel = asyncSocketChannel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        // 首先对 attachment 进行 flip() 操作，为后续从缓冲区读取数据做准备
        attachment.flip();
        // 根据缓冲区的可读字节数创建 byte 数组
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        // 构造请求消息体
        String request = new String(body, StandardCharsets.UTF_8);
        System.out.println("服务端接收到的请求：" + request);
        String currentTime = "System Error！";
        // 对请求消息进行判断
        if ("query time".equalsIgnoreCase(request)) {
            // 如果请求指令是 "query time"，则把服务器的当前时间编码后返回给客户端
            currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
        }
        // 将服务端响应发送给客户端
        this.doWrite(currentTime);
    }

    private void doWrite(String currentTime) {
        // 对当前时间进行合法性校验
        if (currentTime != null && currentTime.trim().length() > 0) {
            // 调用字符串的解码方法将应答消息编码成字节数组
            byte[] bytes = currentTime.getBytes();
            // 将消息复制到发送缓冲区 writeBuffer 中
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            // 调用 AsynchronousSocketChannel 的异步 write() 方法，该方法有三个与 read() 方法相同的参数，
            // 这里直接实现 write() 方法的异步回调接口 java.nio.channels.CompletionHandler
            asyncSocketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 对发送的 writeBuffer 进行判断，如果还有剩余的字节可写，说明没有发送完成，需要继续发送，直到发送成功
                    if (attachment.hasRemaining()) {
                        // 如果没有发送完，则继续发送
                        asyncSocketChannel.write(attachment, attachment, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    // 当发生异常时，对异常 Throwable 进行判断：如果是 IO 异常就关闭链路、释放资源；如果是其他异常，按照业务自己的逻辑进行处理
                    // 本示例中没有对异常进行分类，只要发生了读写异常，就关闭链路、释放资源。
                    try {
                        asyncSocketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.asyncSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}