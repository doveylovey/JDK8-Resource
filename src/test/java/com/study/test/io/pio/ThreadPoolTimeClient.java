package com.study.test.io.pio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
public class ThreadPoolTimeClient {
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