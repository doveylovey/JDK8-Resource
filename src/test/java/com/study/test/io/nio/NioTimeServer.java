package com.study.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class NioTimeServer {
    public static void main(String[] args) {
        int port = 8080;
        NioTimeServerHandler handler = new NioTimeServerHandler(port);
        new Thread(handler, "nio-server-01").start();
    }
}

class NioTimeServerHandler implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    /**
     * 初始化多路复用器、绑定监听端口。
     * <p>
     * 在构造方法中进行资源初始化，创建多路复用器 Selector、ServerSocketChannel、对 Channel 和 TPC 参数进行配置(如：将 serverSocketChannel 设置为异步非阻塞模式，设置它的 backlog 为 1024)。
     * 系统资源初始化成功后，将 ServerSocketChannel 注册到 Selector 上，并监听 SelectionKey.OP_ACCEPT 操作位。
     * 如果资源初始化失败(如：端口占用)，则退出。
     *
     * @param serverPort
     */
    public NioTimeServerHandler(int serverPort) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(serverPort), 1024);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("【NIO】time server is start in port：" + serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                // 循环遍历 selector，休眠时间为1s，无论是否有读写事件发生，selector 每隔1s都被唤醒一次，selector 也提供了无参 select() 方法。
                // 当有处于就绪状态的 Channel 时，selector 就返回就绪状态的 Channel 的 SelectionKey 集合，通过对该集合进行迭代，可以进行网络的异步读写操作。
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
            }
        }
        // 多路复用器关闭后，所有注册在上面的 Channel 和 Pipe 等资源都会被自动去注册关闭，所以不需要重复释放资源
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey selectionKey) throws IOException {
        if (selectionKey.isValid()) {
            // 根据对 SelectionKey 操作位的判断即可获知网络事件的类型
            if (selectionKey.isAcceptable()) {
                // 处理新接入的客户端请求
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                // 通过 ServerSocketChannel 的 accept() 接收客户端的连接请求并创建 SocketChannel 实例，
                // 到这里就相当于完成了 TCP 的三次握手，TCP 物理链路正式建立。
                // 注意：要将 SocketChannel 设置为异步非阻塞，同时可以根据需要对 TCP 参数进行设置(如：TCP 接收和发送的缓冲区大小)
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (selectionKey.isReadable()) {
                // 读取客户端请求消息
                SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                // 创建一个 ByteBuffer：由于事先无法知道客户端发送的码流大小，故本例中开辟一个 1K 的缓冲区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                // 调用 SocketChannel 的 read() 方法读取码流，由于前面已将 SocketChannel 设置为异步非阻塞模式，因此这里的 read() 是非阻塞的
                int read = socketChannel.read(readBuffer);
                // 使用返回值进行判断，有三种可能情况：
                // 1、返回值 > 0：读取到了字节，对字节进行编解码。
                // 2、返回值 = 0：未读取到字节，属于正常情况，此时忽略。
                // 3、返回值 = -1：链路已经关闭，此时需要关闭 SocketChannel，释放资源。
                if (read > 0) {
                    // flip() 操作：将缓冲区当前的 limit 设置为 position、position 设置为 0，用于后续对缓冲区的读取操作。
                    readBuffer.flip();
                    // 根据缓冲区中可读的字节个数创建字节数组
                    byte[] bytes = new byte[readBuffer.remaining()];
                    // 调用 ByteBuffer 的 get() 操作将缓冲区中可读的字节数组复制到新创建的字节数组中
                    readBuffer.get(bytes);
                    // 调用字符串的构造函数创建请求消息体
                    String request = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("服务端接收到的请求：" + request);
                    String currentTime = "System Error！";
                    if ("query time".equalsIgnoreCase(request)) {
                        // 如果请求指令是 "query time"，则把服务器的当前时间编码后返回给客户端
                        currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                    }
                    // 将应答消息异步发送给客户端
                    this.doWrite(socketChannel, currentTime);
                } else if (read < 0) {
                    // 对端链路关闭
                    selectionKey.cancel();
                    socketChannel.close();
                } else {
                    // 读取到0字节，直接忽略
                }
            }
        }
    }

    private void doWrite(SocketChannel socketChannel, String response) throws IOException {
        if (response != null && response.trim().length() > 0) {
            // 将字符串编码成字节数组
            byte[] bytes = response.getBytes();
            // 根据字节数组的容量创建 ByteBuffer
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            // 调用 ByteBuffer 的 put() 操作将字节数组复制到缓冲区中
            writeBuffer.put(bytes);
            // 对缓冲区进行 flip() 操作
            writeBuffer.flip();
            // 调用 SocketChannel 的 write() 方法将缓冲区中的字节数组发送出去
            // 注意：SocketChannel 是异步非阻塞的，它并不保证一次能够把需要发送的字节数组发送完，此时就会出现"写半包"问题，
            // 我们需要注册写操作，不断轮询 Selector 将没有发送完的 ByteBuffer 发送完毕，
            // 可以通过 ByteBuffer 的 hasRemaining() 方法判断消息是否发送完成
            socketChannel.write(writeBuffer);
        }
    }
}

