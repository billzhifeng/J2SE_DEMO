package com.jdk.thread.factorydemo;

import java.util.concurrent.atomic.AtomicInteger;

//jdk 默认线程池实现原理
public class DefaultThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber   = new AtomicInteger(1);//原子类,线程编号

    private ThreadGroup                group        = null;                //线程组

    private final AtomicInteger        threadNumber = new AtomicInteger(1);//线程数目

    private String                     namePrefix   = "";                  //为每个创建的线程添加前缀

    //jdk线程池中默认的线程工程很简单,它做的事情就是统一给线程池中所有线程设置group\前缀\优先级
    DefaultThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();//取得线程组

        namePrefix = "pool_" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {

        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());

        if (t.isDaemon())//设置为非守护线程
            t.setDaemon(false);

        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);//统一优先级
        System.out.println("newThread: " + t.getName());
        return t;
    }

    public static void main(String[] args) {
        DefaultThreadFactory fac = new DefaultThreadFactory();

        for (int i = 0; i < 10; i++) {
            fac.newThread(new Mytask(i)).start();
        }
    }
}

class Mytask implements Runnable {
    private int num;

    public Mytask(int num) {
        this.num = num;
    }

    @Override
    public void run() {

        System.out.println("Task " + num + " is running!");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
