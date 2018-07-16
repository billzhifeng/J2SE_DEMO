package com.jdk.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NioWriterDemo1 {
    static private byte[] message = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };

    public static void main(String[] args) {

        try {
            FileOutputStream fos = new FileOutputStream("NoiWriteTest.txt");

            FileChannel channel = fos.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int len = message.length;
            for (int i = 0; i < len; i++) {
                buffer.put(message[i]);
            }
            buffer.flip();

            channel.write(buffer);

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
