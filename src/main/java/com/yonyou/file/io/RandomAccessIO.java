package com.yonyou.file.io;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class RandomAccessIO {
    public static void main(String[] args) throws Exception{
        File file = new File("test.txt");

        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.writeByte(46565345);
        FileChannel channel = raf.getChannel();
        MappedByteBuffer mapByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());

        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);  //申请直接内存， jvm堆外申请
        //ByteBuffer buffer = ByteBuffer.allocate(1024);     //申请内存， jvm堆中申请


    }
}
