package com.study.test.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
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
public class AioTimeClient {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 8080;
        AioTimeClientHandler handler = new AioTimeClientHandler(address, port);
        new Thread(handler, "aio-client-01").start();
    }
}

class AioTimeClientHandler implements Runnable, CompletionHandler<Void, AioTimeClientHandler> {
    private AsynchronousSocketChannel asyncSocketChannel;
    private String host;
    private Integer port;
    private CountDownLatch countDownLatch;

    public AioTimeClientHandler(String host, Integer port) {
        this.host = host;
        this.port = port;
        try {
            // 通过 AsynchronousSocketChannel 的 open() 方法创建一个新的 AsynchronousSocketChannel 对象
            asyncSocketChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 创建 CountDownLatch 进行等待，防止异步操作还没有完成线程就退出了
        countDownLatch = new CountDownLatch(1);
        // 通过 AsynchronousSocketChannel 的 connect() 方法发起异步操作，参数含义如下：
        // SocketAddress remote：该通道要连接的远程地址。
        // A attachment：AsynchronousSocketChannel 的附件，用于回调通知时作为入参被传递，调用者可以自定义。
        // CompletionHandler<Void,? super A> handler：异步操作回调通知接口，有调用者实现。
        // 本例中后两个参数都是 AioTimeClientHandler 类本身，因为它实现了 CompletionHandler 接口。
        asyncSocketChannel.connect(new InetSocketAddress(host, port), this, this);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            asyncSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步连接成功后的方法回调
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Void result, AioTimeClientHandler attachment) {
        // 创建请求消息体，对其编码后复制到发送缓冲区 writeBuffer 中
        byte[] request = "query time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        writeBuffer.put(request);
        writeBuffer.flip();
        // 调用 AsynchronousSocketChannel 的 write() 方法进行异步写。与服务端类似，可以实现 CompletionHandler 接口用于完成写操作完成后的回调
        asyncSocketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.hasRemaining()) {
                    // 如果发送缓冲区中还有尚未发送的字节，则继续异步发送。
                    asyncSocketChannel.write(attachment, attachment, this);
                } else {
                    // 如果发送缓冲区中所有字节都已经发送完成，则执行异步读取操作。
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    // 异步读取服务端的响应消息。由于 read() 操作是异步的，所以我们通过匿名内部类实现 CompletionHandler 接口，
                    // 当读取完成被 JDK 回调时，构造应答消息
                    asyncSocketChannel.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            // 从 CompletionHandler 的 ByteBuffer 中读取应答消息，然后打印结果
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
                            String response = new String(bytes, StandardCharsets.UTF_8);
                            System.out.println("客户端接收到的响应：" + response);
                            countDownLatch.countDown();
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            // 当读取发生异常时，关闭链路，同时调用 CountDownLatch 的 countDown() 方法让 AioTimeClientHandler 线程执行完毕，客户端退出执行
                            try {
                                asyncSocketChannel.close();
                                countDownLatch.countDown();
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
                    asyncSocketChannel.close();
                    countDownLatch.countDown();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AioTimeClientHandler attachment) {
        exc.printStackTrace();
        try {
            asyncSocketChannel.close();
            countDownLatch.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}