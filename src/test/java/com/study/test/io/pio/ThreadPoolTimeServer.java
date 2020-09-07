package com.study.test.io.pio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 伪异步 IO：解决 BIO 一个请求一个线程处理的问题，伪异步 IO 通过一个线程池来处理多个客户端请求，形成客户端数 M 与线程池最大线程
 * 数 N 的比例关系，其中 M 可以远远大于 N，通过线程池可以灵活地调配线程资源，设置线程的最大值，防止由于海量并发接入导致线程耗尽。
 * <p>
 * 伪异步 IO 通信框架采用了线程池实现，因此避免了为每个请求都创建一个独立线程造成的线程资源耗尽问题。但其底层通信仍然采用的是同步阻塞模型，因此无法从根本上解决问题。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月28日
 */
public class ThreadPoolTimeServer {
    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("【ThreadPoolIO】time server is start in port：" + serverPort);
            Socket socket = null;
            // 创建处理类的线程池
            TimeServerThreadPool serverHandler = new TimeServerThreadPool(50, 10000);
            while (true) {
                socket = serverSocket.accept();
                // 当接收到新客户端连接的时候，将请求 Socket 封装成一个 Task，然后调用线程池的 execute() 方法执行，从而避免为每个请求都创建一个线程
                serverHandler.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (serverSocket != null) {
                System.out.println("【ThreadPoolIO】time server close！");
                serverSocket.close();
            }
        }
    }
}


class TimeServerThreadPool {
    private ExecutorService executorService;

    public TimeServerThreadPool(int maxPoolSize, int queueSize) {
        // 由于线程池和消息队列都是有界的，因此，无论客户端的并发连接数有多大，都不会导致线程个数过多或内存溢出，相比传统的一连接一线程模型是一种改良
        this.executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueSize));
    }

    public void execute(Runnable task) {
        executorService.execute(task);
    }
}

class TimeServerHandler implements Runnable {
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        /**
         * 注意 java.io.InputStream.read(byte[]) API 的源码说明：当对 Socket 的输入流进行读取操作时，它会一直阻塞下去，
         * 直到出现"有数据可读、可用数据已经读取完毕、发生空指针或IO异常"三种情况之一。
         * 这意味着：当对方发送请求或应答消息比较缓慢时、网络传输较慢时，读取输入流一方的通信线程将被长时间阻塞，如果对方要60s才能
         * 将数据发送完，读取一方的IO线程也将会被同步阻塞60s，在此期间，其他接入消息只能在消息队列中排队。
         * <p>
         * 当调用 java.io.OutputStream.write(byte[]) 方法写输出流时，它将会被阻塞，直到所有要发送的字节全部写入完毕或发生异常。
         */
        try (BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
             PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true)) {
            String currentTime = "System Error！";
            String request = null;
            while (true) {
                // 通过 BufferedReader 读取一行，如果已经读到了输入流的尾部，则返回值为 null，退出循环。
                // 如果读取到了非空值，则对内容进行判断，如果请求指令为 "query time"，则获取系统当前时间，
                // 并通过 PrintWriter 的 println() 函数发送给客户端，最后退出循环。
                request = in.readLine();
                if (request == null) {
                    break;
                }
                System.out.println("服务端接收到的请求：" + request);
                if ("query time".equalsIgnoreCase(request)) {
                    currentTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                    TimeUnit.SECONDS.sleep(2);
                }
                out.println(currentTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放输入流、输出流、Socket 套接字句柄资源，最后线程自动销毁并被虚拟机回收
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}