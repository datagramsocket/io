package com.infinova.file.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * NoBufferedIo 10秒钟写了30M
 *
 * Buffered io   10秒钟写了700M
 *
 * 为什么使用BufferedOutputStream 效率会提升？
 * bufferedOutputStream 底层加入了缓冲区 ,当缓冲区满才会 调用write方法
 * 调用 Java 的write方法写磁盘时 要调用内核的写方法 此时程序要从用户态切换到内核态   此现象又称为系统调用(system call) 要消耗比较多的资源
 * buffered 减少了系统调用的次数，效率得到了提升
 * */
public class BufferedIo {
    private static volatile boolean flag = true;

    public static void main(String[] args) throws Exception{
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream("test.txt"));
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
