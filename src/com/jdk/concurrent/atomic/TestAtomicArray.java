package com.jdk.concurrent.atomic;

import java.util.concurrent.atomic.AtomicIntegerArray;

public class TestAtomicArray {

    public static void main(String[] args) throws Exception {
        AtomicIntegerArray atomicArray = new AtomicIntegerArray(5);
        // 设置指定索引位的数值
        atomicArray.set(0, 5);
        System.out.println("current = " + atomicArray.get(0));

        //（实际上默认值为0，这里加了5）
        System.out.println("current = " + atomicArray.addAndGet(0, 5));
        // --
        int current = atomicArray.decrementAndGet(0);
        System.out.println("current = " + current);
    }
}
