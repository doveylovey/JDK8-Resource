package com.study.test.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * 先启动服务端，再启动客户端。我们发现 BIO 的主要问题：
 * 每当有一个新的客户端请求接入时，服务端就要创建一个新的线程去处理新接入的客户端链路，即一个线程只能处理一个客户端连接。
 * 在高性能服务器应用领域，往往需要面向成千上万个客户端的并发连接，BIO 模型显然无法满足高性能、高并发的接入场景。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月28日
 */
public class JavaBioTests {
}

/**
 * 同步阻塞式 IO 的服务端
 */
class BioTimeServer {
    public static void main(String[] args) throws IOException {
        int serverPort = 8080;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("【BIO】time server is start in port：" + serverPort);
            Socket socket = null;
            while (true) {
                // 通过一个无限循环来监听客户端的连接，如果没有客户端接入，则服务端主线程就会阻塞在 ServerSocket 的 accept 操作上。
                // 启动服务端，通过 JvisualVM 打印线程堆栈，可以发现服务端主线程确实阻塞在 ServerSocket 的 accept 操作上。
                socket = serverSocket.accept();
                new Thread(new BioTimeServerHandler(socket)).start();
            }
        } finally {
            if (serverSocket != null) {
                System.out.println("【BIO】time server close！");
                serverSocket.close();
            }
        }
    }
}

/**
 * 同步阻塞式 IO 的客户端
 */
class BioTimeClient {
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
                TimeUnit.SECONDS.sleep(3);
            }
        }
    }
}

class BioTimeServerHandler implements Runnable {
    private Socket socket;

    public BioTimeServerHandler(Socket socket) {
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
                }
                out.println(currentTime);
            }
        } catch (IOException e) {
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