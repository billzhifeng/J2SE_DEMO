package com.jdk.thread.pool.rejectedExecutionHandler;

public class Worker implements Runnable {
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is running");
    }
}
