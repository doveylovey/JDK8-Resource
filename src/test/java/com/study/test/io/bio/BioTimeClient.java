package com.study.test.io.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 同步阻塞式 IO 的客户端
 * <p>
 * 先启动服务端，再启动客户端。我们发现 BIO 的主要问题：
 * 每当有一个新的客户端请求接入时，服务端就要创建一个新的线程去处理新接入的客户端链路，即一个线程只能处理一个客户端连接。
 * 在高性能服务器应用领域，往往需要面向成千上万个客户端的并发连接，BIO 模型显然无法满足高性能、高并发的接入场景。
 *
 * @author doveylovey
 * @version v1.0.0
 * @email 1135782208@qq.com
 * @date 2020年08月28日
 */
public class BioTimeClient {
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