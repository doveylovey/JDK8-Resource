package com.study.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * 注意：本示例没有考虑 "半包读" 和 "半包写" 问题，否则代码会很复杂，作为示例程序没有这个必要。
 * 使用 NIO 编程的优点如下：
 * 1、客户端发起的连接操作是异步的，可以ton过在多路复用器上注册 SelectionKey.OP_CONNECT 等待后续结果，不需要像其他客户端那样被同步阻塞。
 * 2、SocketChannel 的读写操作都是异步的，如果没有可读写的数据它不会同步等待，而是直接返回，这样 IO 通信线程就可以处理其他的链路，无需同步等待这个链路可用。
 * 3、线程模型的优化：由于 JDK 的 Selector 在 Linux 等主流操作系统上通过 epoll 实现，它没有连接句柄数的限制(只受限于操作系统的最大句柄数或对单个进程的句柄限制)，
 * 这意味着一个 Selector 线程可以同时处理成千上万个客户端连接，而且性能不会随着客户端的增加而线性下降，因此 NIO 非常适合做高性能、高负载的网络服务器。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年09月07日
 */
public class NioTimeClient {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 8080;
        NioTimeClientHandler handler = new NioTimeClientHandler(address, port);
        new Thread(handler, "nio-client-01").start();
    }
}

class NioTimeClientHandler implements Runnable {
    private String address;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public NioTimeClientHandler(String address, int port) {
        // 初始化 NIO 的多路复用器和 SocketChannel 对象，注意要将 SocketChannel 对象设置为异步非阻塞模式
        this.address = ((address == null || address == "") ? "127.0.0.1" : address);
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            // 发送连接请求：本示例的连接肯定是成功的，故无需重连操作，因此将其放到循环之前
            this.doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while (!stop) {
            // 在循环中轮询多路复用器，当有就绪的 Channel 时就执行 handleInput(SelectionKey selectionKey) 方法
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()) {
                    selectionKey = iterator.next();
                    iterator.remove();
                    try {
                        this.handleInput(selectionKey);
                    } catch (IOException e) {
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        // 线程退出循环后，需对连接资源进行释放，以实现"优雅退出"。
        if (selector != null) {
            try {
                // 多路复用器资源释放，由于多路复用器上可能注册了成千上万的 Channel 或 pipe，
                // 如果一一对这些资源进行释放显然不合适，因此 JDK 底层会自动释放所有跟这个多路复用器关联的资源，可以参考源码
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(address, port))) {
            // 如果连接成功，则将 SocketChannel 注册到多路复用器 Selector 上，注册 SelectionKey.OP_READ
            socketChannel.register(selector, SelectionKey.OP_READ);
            this.doWrite(socketChannel);
        } else {
            // 没有没有直接连接成功，则说明服务端没有返回 TCP 握手应答消息，但这并不代表连接失败，
            // 需将 SocketChannel 注册到多路复用器 Selector 上，注册 SelectionKey.OP_CONNECT，
            // 当服务端返回 TCP syn-ack 消息后，Selector 就能轮询到这个 SocketChannel 处于连接就绪状态
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        // 首先对 SelectionKey 进行判断，看它处于什么状态
        if (selectionKey.isValid()) {
            // 如果处于连接状态，说明服务端已经返回 ACK 应答消息，此时对连接结果进行判断
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            // 调用 SocketChannel 的 finishConnect() 方法：返回值为 true 表示客户端连接成功，返回值为 false 或抛出 IOException 异常表示客户端连接失败
            if (socketChannel.finishConnect()) {
                // 连接成功后，将 SocketChannel 注册到多路复用器上，注册 SelectionKey.OP_READ 操作位，监听网络读操作
                socketChannel.register(selector, SelectionKey.OP_READ);
                // 发送请求消息给服务端
                this.doWrite(socketChannel);
            } else {
                // 连接失败，进程退出
                System.exit(1);
            }
            if (selectionKey.isReadable()) {
                // 如果客户端接收到了服务端的应答消息，则 SocketChannel 是可读的，由于无法事先判断应答码流的大小，
                // 此处就预分配 1M 的接收缓冲区用于读取应答消息，调用 SocketChannel 的 read() 方法进行异步读取操作。
                // 由于是异步操作，所以必须对读取结果进行判断，可以参考 NioTimeServerHandler.handleInput(SelectionKey selectionKey)
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(readBuffer);
                if (readBytes > 0) {
                    // 如果读取到了消息，则对消息进行解码，最后打印结果
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String response = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("客户端接收到的响应：" + response);
                    this.stop = true;
                } else if (readBytes < 0) {
                    // 对端链路关闭
                    selectionKey.cancel();
                    socketChannel.close();
                } else {
                    // 读取到0字节，忽略
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        // 构造请求消息体，对其编码后写入到发送缓冲区中，最后调用 SocketChannel 的 write() 方法进行发送
        // 由于发送是异步的，所以会存在"半包写"问题，此处不再赘述。
        // 最后通过 ByteBuffer 的 hasRemaining() 方法对发送结果进行判断，如果缓冲区中的消息全部发送完成则打印一条句
        byte[] request = "query time".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        writeBuffer.put(request);
        writeBuffer.flip();
        socketChannel.write(writeBuffer);
        if (!writeBuffer.hasRemaining()) {
            System.out.println("【NIO】向服务端发送请求成功");
        }
    }
}

