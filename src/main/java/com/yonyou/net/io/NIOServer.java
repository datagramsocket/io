package com.yonyou.net.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * nio select poll都要遍历所有的IO询问状态
 * 不采用多路复用器方式的nio，代码如下，这个遍历的过程成本在用户态和内核态间的切换
 * 采用select poll 这种多路复用器，这个遍历过程触发了一次系统调用，用户把fds传递给内核，内核根据用户这次调用传递的fds进行遍历
 * @author: fcq
 * @create: 2021-01-02
 */
public class NIOServer {

    public static void main(String[] args) {
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9999));
            serverSocketChannel.configureBlocking(false);
            List<SocketChannel> list = new ArrayList();
            while(true){
                SocketChannel socketChannel = serverSocketChannel.accept();
                if(socketChannel != null){
                    socketChannel.configureBlocking(false);
                    list.add(socketChannel);
                }

                Iterator<SocketChannel> iterator = list.iterator();
                while(iterator.hasNext()){
                    SocketChannel channel = iterator.next();
                    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
                    try{
                        int read = channel.read(byteBuffer);
                        if(read > 0){
                            byteBuffer.flip();
                            byte[] bytes = new byte[byteBuffer.limit()];
                            byteBuffer.get(bytes);
                            System.out.println("read some data:" + new String(bytes, "UTF-8"));
                        }else if(read == 0){
                            byteBuffer = null;
                            System.gc();
                            continue;
                        }else {
                            channel.close();
                            iterator.remove();
                            System.out.println("client read -1....,remove this client");
                        }
                    }catch (Exception e){
                        //e.printStackTrace();
                        channel.close();
                        iterator.remove();
                        System.out.println("client shutdown....,remove this client");
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
