package com.infinova.file.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class NoBufferedIo {

    private static volatile boolean flag = true;
    public static void main(String[] args) throws Exception{
        OutputStream outputStream = new FileOutputStream("test.txt");
        new Thread(() -> {
            while(flag){
                try {
                    outputStream.write("this is test\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(10000);
        flag = false;
        System.in.read();
    }
}
