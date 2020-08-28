package com.study.test.io;

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
 */
public class JavaThreadPoolIoTests {
}

class ThreadPoolTimeServer {
    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("【ThreadPoolIO】time server is start in port：" + serverPort);
            Socket socket = null;
            ThreadPoolTimeServerHandler serverHandler = new ThreadPoolTimeServerHandler(50, 10000);
            while (true) {
                socket = serverSocket.accept();
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

class ThreadPoolTimeClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        int serverPort = 8080;
        // 创建 Socket 客户端
        try (Socket socket = new Socket("127.0.0.1", serverPort);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                // 向服务端发送 "query time" 指令
                out.println("query time");
                // 读取服务端响应结果并打印
                String response = in.readLine();
                System.out.println("客户端接收到的响应：" + response);
                //TimeUnit.SECONDS.sleep(3);
            }
        }
    }
}

class ThreadPoolTimeServerHandler {
    private ExecutorService executorService;

    public ThreadPoolTimeServerHandler(int maxPoolSize, int queueSize) {
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