package com.jdk.concurrent.atomic;

import java.util.concurrent.atomic.AtomicInteger;

//AtomicInteger 实例,计数使用
public class CountDemo implements Runnable {

    private AtomicInteger cnt = new AtomicInteger(1);

    @Override
    public void run() {
        //自增
        int value = cnt.incrementAndGet();
        System.out.println(Thread.currentThread().getName() + " 改变后 cnt.value=" + value);
        //这和index++最大的不同是：index++不是线程安全的

    }

    public static void main(String[] args) {
        CountDemo demo = new CountDemo();
        for (int i = 0; i < 100; i++) {
            new Thread(demo).start();
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("final = " + demo.cnt.intValue());
    }
}
