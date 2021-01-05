package com.yonyou.net.io;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author: fcq
 * @create: 2021-01-02
 */
public class BIOClient {

    public static void main(String[] args) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("127.0.0.1", 9999));
            OutputStream out = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true){
                String s = reader.readLine();
                if("exit".equalsIgnoreCase(s)){
                    reader.close();
                    out.close();
                    socket.close();
                    break;
                }
                out.write(s.getBytes("UTF-8"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
