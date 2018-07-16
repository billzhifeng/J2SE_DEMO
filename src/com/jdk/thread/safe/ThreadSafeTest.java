package com.jdk.thread.safe;

public class ThreadSafeTest implements Runnable {

    private static int static_i;//静态变量   

    public void run() {
        static_i = 4;
        System.out.println("[" + Thread.currentThread().getName() + "]获取static_i 的值:" + static_i);
        static_i = 10;
        System.out.println("[" + Thread.currentThread().getName() + "]获取static_i*2的值:" + static_i * 2);
    }

    public static void main(String[] args) {
        ThreadSafeTest t = new ThreadSafeTest();
        //启动尽量多的线程才能很容易的模拟问题   
        for (int i = 0; i < 50; i++) {
            //t可以换成new Test(),保证每个线程都在不同的对象中执行，结果一样   
            new Thread(t, "线程" + i).start();
        }
    }
}
