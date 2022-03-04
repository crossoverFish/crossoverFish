package com.tyu.base;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NioTest {

    static class Client {

        public static void main(String[] args) throws IOException {
            testClient();
        }

        /**
         * 客户端
         */
        public static void testClient() throws IOException {
            InetSocketAddress address = new InetSocketAddress(9000);

            // 1、获取通道（channel）
            SocketChannel socketChannel = SocketChannel.open(address);
            // 2、切换成非阻塞模式
            socketChannel.configureBlocking(false);

            // 3、分配指定大小的缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("hello world  ".getBytes());
            System.out.println("put完之后-->mark--->" + byteBuffer.mark());
            byteBuffer.flip();
            System.out.println("flip完之后-->mark--->" + byteBuffer.mark());
            socketChannel.write(byteBuffer);

            socketChannel.close();
        }


    }

    static class Server {


        public static void main(String[] args) throws IOException {
            testServer();
        }

        public static void testServer() throws IOException {

            // 1、获取Selector选择器
            Selector selector = Selector.open();

            // 2、获取通道
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 3.设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 4、绑定连接
            serverSocketChannel.bind(new InetSocketAddress(9000));

            // 5、将通道注册到选择器上,并注册的操作为：“接收”操作
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            // 6、采用轮询的方式，查询获取“准备就绪”的注册过的操作
            while (selector.select() > 0) {
                // 7、获取当前选择器中所有注册的选择键（“已经准备就绪的操作”）
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    // 8、获取“准备就绪”的选择键
                    SelectionKey selectedKey = selectedKeys.next();

                    // 9、判断key是具体的什么事件
                    if (selectedKey.isAcceptable()) {
                        // 10、若接受的事件是“接收就绪” 操作,就获取客户端连接
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        // 11、切换为非阻塞模式
                        socketChannel.configureBlocking(false);
                        // 12、将该通道注册到selector选择器上
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectedKey.isReadable()) {
                        // 13、获取该选择器上的“读就绪”状态的通道
                        SocketChannel socketChannel = (SocketChannel) selectedKey.channel();

                        // 14、读取数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int length = 0;
                        while ((length = socketChannel.read(byteBuffer)) != -1) {
                            byteBuffer.flip();
                            System.out.println(new String(byteBuffer.array(), 0, length));
                            byteBuffer.clear();
                        }
                        socketChannel.close();
                    }

                    // 15、移除选择键
                    selectedKeys.remove();
                }
            }

            // 7、关闭连接
            serverSocketChannel.close();
        }

        static class App {
            public static void main(String[] args) {

                // 创建一个缓冲区
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                // 看一下初始时4个核心变量的值
                System.out.println("初始时-->limit--->"+byteBuffer.limit());
                System.out.println("初始时-->position--->"+byteBuffer.position());
                System.out.println("初始时-->capacity--->"+byteBuffer.capacity());
                System.out.println("初始时-->mark--->" + byteBuffer.mark());

                System.out.println("有客户端连接了..");

                System.out.println("--------------------------------------");

                // 添加一些数据到缓冲区中
                String s = "JavaEE";
                byteBuffer.put(s.getBytes());

                // 看一下初始时4个核心变量的值
                System.out.println("put完之后-->limit--->"+byteBuffer.limit());
                System.out.println("put完之后-->position--->"+byteBuffer.position());
                System.out.println("put完之后-->capacity--->"+byteBuffer.capacity());
                System.out.println("put完之后-->mark--->" + byteBuffer.mark());

                System.out.println("--------------------------------------");

                byteBuffer.flip();


                System.out.println("flip完之后-->limit--->"+byteBuffer.limit());
                System.out.println("flip完之后-->position--->"+byteBuffer.position());
                System.out.println("flip完之后-->capacity--->"+byteBuffer.capacity());
                System.out.println("flip完之后-->mark--->" + byteBuffer.mark());

                byte[] bytes = new byte[byteBuffer.limit()];
                byteBuffer.get(bytes);
                System.out.println(new String(bytes, 0, bytes.length));

            }

        }

    }
}
