package com.jdk.thread.synchronize.demo1;

import java.util.Date;

/**
 * @author wangzhifeng
 * @date 2018年6月23日 下午7:08:12
 */
public class MyThread extends Thread {

    private String                   methodName;

    private InvokeSynchronizedMethod method;

    public MyThread(String threadName, String methodName, InvokeSynchronizedMethod method) {
        super(threadName);
        this.methodName = methodName;
        this.method = method;
    }

    public void run() {
        System.out.println(Thread.class.getName() + " run time= " + new Date().toLocaleString());
        if ("synMethod".equals(this.methodName)) {
            method.synMethod();
        } else {
            method.noSyncMethod();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //test1();

        test2();
    }

    /**
     * 一个对象的同步方法被访问过程中，其他线程依然可以访问其他非同步方法
     * 
     * @throws InterruptedException
     */
    public static void test1() throws InterruptedException {
        InvokeSynchronizedMethod method = new InvokeSynchronizedMethod();
        MyThread t1 = new MyThread("Thread-1", "synMethod", method);
        MyThread t2 = new MyThread("Thread-2", "noSynMethod", method);
        //调用同步方法的线程启动
        t1.start();
        //主线程sleep 500毫秒,保证进入同步方法中,然后启动另一个线程访问非同步方法
        Thread.sleep(500L);
        t2.start();

        //        打印结果，可以看到一个对象的同步方法被访问过程中，其他线程依然可以访问其他非同步方法
        //        Thread-1 ,start synMethod ...
        //        Thread-2  ,start noSyncMethod 
        //        Thread-1 ,end synMethod
    }

    /**
     * 一个线程进入同步方法，其他线程是无法访问该同步方法
     * 
     * @throws InterruptedException
     */
    public static void test2() throws InterruptedException {
        InvokeSynchronizedMethod method = new InvokeSynchronizedMethod();
        MyThread t1 = new MyThread("Thread-1", "synMethod", method);
        MyThread t2 = new MyThread("Thread-2", "synMethod", method);
        //调用同步方法的线程启动
        t1.start();
        //主线程sleep 500毫秒,保证进入同步方法中,然后启动另一个线程访问同一个同步方法
        Thread.sleep(500L);
        t2.start();

        // 可以看到一个线程进入同步方法，其他线程是无法访问该同步方法
        //        java.lang.Thread run time= 2018-6-23 19:26:37
        //        Thread-1 ,start synMethod ...
        //        java.lang.Thread run time= 2018-6-23 19:26:38
        //        Thread-1 ,end synMethod
        //        Thread-2 ,start synMethod ...
        //        Thread-2 ,end synMethod
    }

}
