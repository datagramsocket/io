package com.yonyou.net.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: fcq
 * @create: 2021-01-01
 *
 * BIO的缺点：因为BIO的阻塞特性accept read 阻塞，导致每来一个客户端都必须开一个线程进行处理，对资源消耗大
 */
public class BIOServer {

    private static int BACK_LOG = 2;



    public static void main(String[] args){
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(9999), BACK_LOG);
            System.out.println("server start on port 9999");
        }catch (IOException e){
            e.printStackTrace();
        }

        while(true){
            try{
                System.in.read();
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try{
                        InputStream in = socket.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        while(true){
                            char[] buffer = new char[1024];
                            int read = reader.read(buffer);
                            if(read == -1){
                                System.out.println("client read -1....");
                                in.close();
                                reader.close();
                                socket.close();
                                break;
                            }else if(read > 0){
                                System.out.println("client read some data:" + new String(buffer));
                            }else {
                                continue;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }).start();
            }catch (IOException e){
                e.printStackTrace();
            }

        }

    }
}
