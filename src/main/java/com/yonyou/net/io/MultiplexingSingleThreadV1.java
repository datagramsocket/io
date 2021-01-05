package com.yonyou.net.io;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用器
 * @author: fcq
 * @create: 2020-12-31
 */
public class MultiplexingSingleThreadV1 {

    private static  Selector selector = null;

    public static void main(String[] args) throws Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9999));
        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            if(selector.select(500) > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if(selectionKey.isAcceptable()){
                        acceptHandler(selectionKey);
                    }else if(selectionKey.isReadable()){
                        readHandler(selectionKey);
                    }
                }
            }

        }

    }


    private static void readHandler(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel)selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        try {
            int read = channel.read(byteBuffer);
            if (read > 0) {
                byteBuffer.flip();
                byte[] bytes = new byte[byteBuffer.limit()];
                byteBuffer.get(bytes);
                System.out.println("read some data:" + new String(bytes, "UTF-8"));
            }else if(read == -1){
                channel.close();
                System.out.println("client read -1....,remove this client");
            }
        }catch (Exception e){
            try {
                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.out.println("client shutdown....,remove this client");
        }
    }

    private static void acceptHandler(SelectionKey selectionKey) {
        try{
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("accept new client");
            socketChannel.configureBlocking(false);
            //ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            socketChannel.register(selector, SelectionKey.OP_READ);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
