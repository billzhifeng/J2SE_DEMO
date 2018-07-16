package com.jdk.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioReaderDemo1 {

    public static void main(String[] args) {

        try {
            FileInputStream fis = new FileInputStream("counttest.txt");
            // 获取通道  
            FileChannel channel = fis.getChannel();
            // 创建缓冲区  
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 读取数据到缓冲区 
            channel.read(buffer);

            buffer.flip();

            while (buffer.remaining() > 0) {
                byte b = buffer.get();
                System.out.print(((char) b));
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
