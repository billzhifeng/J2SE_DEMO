package com.jdk.thread.synchronize.demo2;

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
            method.syncMethod2();
        }
    }

    /**
     * 一个对象的第一个同步方法被访问过程中，其他线程 不可以 访问该对象的其他同步方法
     * 
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        InvokeSynchronizedMethod method = new InvokeSynchronizedMethod();
        MyThread t1 = new MyThread("Thread-1", "synMethod", method);
        MyThread t2 = new MyThread("Thread-2", "synMethod2", method);
        //调用同步方法的线程启动
        t1.start();
        t2.start();

        //        打印结果：两个线程访问同一个对象的2个同步方法,第一个同步方法释放后，第二个同步方法才能进入
        //证明锁的是该对象        
        //java.lang.Thread run time= 2018-6-23 20:42:39
        //        java.lang.Thread run time= 2018-6-23 20:42:39
        //        Thread-2 ,start syncMethod2 time= 2018-6-23 20:42:39
        //        Thread-2 ,end syncMethod2 time= 2018-6-23 20:42:43
        //        Thread-1 ,start synMethod time= 2018-6-23 20:42:43
        //        Thread-1 ,end synMethod time= 2018-6-23 20:42:47
    }

}
