package com.jdk.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangzhifeng
 * @date 2018年2月22日 下午5:43:17
 */
public class OutOfMemory {

    //运行时设置 -Xms1m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError
    byte[] byteArray = new byte[1 * 1024 * 1024];//1M

    public static void main(String[] args) {
        List<OutOfMemory> list = new ArrayList<OutOfMemory>();
        try {
            for (int i = 0; i < 20; i++) {
                list.add(new OutOfMemory());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
