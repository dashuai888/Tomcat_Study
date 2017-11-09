package com.dashuai.a_simpleServer;

import java.io.*;
import java.net.Socket;

/**
 * java 的 Socket 类表示一个客户端 socket.
 * @author wangyishuai 2017年10月29日
 */
public class SocketEg {
    /**
     * 一个 阻塞I/O 客户端的实现案例.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        /*
         * 创建一个socket流，同时指定该流要连接的主机（通过主机名指定的，当然也能直接指定ip地址）及其对应的端口号 这里本机测试，设置的是回环接口地址.
         * 创建socket，下面就可以通过它来发送或者接收网络数据。
         */
        Socket socket = new Socket("127.0.0.1", 8111);
        // 如果要发送文本，就需要创建一个字符输出流，但是要先建立字节输出流，因为网络数据是字节流的形式
        OutputStream outputStream = socket.getOutputStream();
        // 字符输出流基于上面创建的字节输出流建立，PrintWriter 可以把字节序列使用默认的编码规则格式的转化为字符序列
        // 设置自动刷新为true，字符流在输出时，会把缓存的输出流刷新
        PrintWriter printWriter = new PrintWriter(outputStream, true);

        /*
         * 要接收文本，一样的道理，也就是需要读取数据，先创建输入流
         */
        InputStream inputStream = socket.getInputStream();
        // 要读取的是文本内容，故使用 InputStreamReader，它是一个将字节流转化为字符流的桥梁，它读取字节并将其解码为字符
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        // 从字符输入流中读取数据，并使用缓存 buffers 保存字符序列使其转化为文本内容
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        /*
         * 模拟发送HTTP请求消息.
         */
        printWriter.println("GET /Tomcat_Study/src/main/java/com/dashuai/chapter01/simpleWebServer/http.md HTTP/1.1");
        printWriter.println("Host: 127.0.0.1:8111");
        printWriter.println("Connection: Close");
        printWriter.println();

        /*
         * 由于是在本地请求，用的是一个socket，下面模拟读取响应消息其实就是上面的请求消息
         */
        StringBuilder stringBuilder = new StringBuilder();
        boolean loop = true;
        while (loop) {
            // 阻塞I/O，需要等待输入流是否已经准备好被读取，如果buffer非空，那么就说明缓存的字符流已经准备好被读取了。
            // bufferedReader.ready(),如果返回false，其实并不能保证后面会不会有流继续来，故需要循环判断
            if (bufferedReader.ready()) {
                int i = 0;
                while (i != -1) {
                    // 读取单个的字符，如果buffer中的输入流读取完毕，就返回 -1。否则返回读取的字符的int形式编码（char类型）。范围为0-65535
                    i = bufferedReader.read();
                    stringBuilder.append((char) i); // 转换回char
                }
                loop = false; // 读取完毕
            }
            // 为了保证读取数据完整，需要暂停当前线程，等待一段时间
            Thread.sleep(50);
        }

        System.out.println(stringBuilder.toString());
        socket.close();
    }
}
