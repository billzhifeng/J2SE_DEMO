package com.jdk.concurrent.lock;

import java.util.concurrent.locks.ReentrantLock;

public class FairLock {

    //实现公平锁
    //在实例化锁对象的时候，构造方法有2个，一个是无参构造方法，默认不传参数是非公平锁。
    //另一个是传入一个boolean变量的构造方法。当传入值为true的时候，该锁为公平锁。
    //    JDK源码
    //    public ReentrantLock(boolean fair) {
    //        sync = fair ? new FairSync() : new NonfairSync();
    //    }
    //    公平锁：按照线程加锁的顺序来获取锁
    //    非公平锁：随机竞争来得到锁
    //    此外，JAVA还提供isFair()来判断一个锁是不是公平锁。

    private ReentrantLock lock;

    public FairLock(boolean isFair) {
        lock = new ReentrantLock(isFair);
    }

    private void doLock() {
        try {
            lock.lock();
            System.out.println("Thread_" + Thread.currentThread().getName() + "_获得了锁.");
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final FairLock fairLock = new FairLock(true);//公平锁

        Runnable runable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Thread_" + Thread.currentThread().getName() + "_执行了");
                fairLock.doLock();
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(runable).start();
        }

        //print 打印的结果是按照线程加锁的顺序输出的，即线程运行了，则会先获得锁,
        //        Thread_Thread-0_执行了
        //        Thread_Thread-3_执行了
        //        Thread_Thread-4_执行了
        //        Thread_Thread-2_执行了
        //        Thread_Thread-1_执行了
        //        Thread_Thread-5_执行了
        //        Thread_Thread-0_获得了锁.
        //        Thread_Thread-6_执行了
        //        Thread_Thread-7_执行了
        //        Thread_Thread-8_执行了
        //        Thread_Thread-3_获得了锁.
        //        Thread_Thread-9_执行了
        //        Thread_Thread-4_获得了锁.
        //        Thread_Thread-2_获得了锁.
        //        Thread_Thread-1_获得了锁.
        //        Thread_Thread-5_获得了锁.
        //        Thread_Thread-6_获得了锁.
        //        Thread_Thread-7_获得了锁.
        //        Thread_Thread-8_获得了锁.
        //        Thread_Thread-9_获得了锁.

        //===把Run类里的true改为false就为非公平锁了
        //        Thread_Thread-0_执行了
        //        Thread_Thread-3_执行了
        //        Thread_Thread-0_获得了锁.
        //        Thread_Thread-2_执行了
        //        Thread_Thread-1_执行了
        //        Thread_Thread-3_获得了锁.
        //        Thread_Thread-5_执行了
        //        Thread_Thread-5_获得了锁.
        //        Thread_Thread-4_执行了
        //        Thread_Thread-4_获得了锁.
        //        Thread_Thread-6_执行了
        //        Thread_Thread-6_获得了锁.
        //        Thread_Thread-7_执行了
        //        Thread_Thread-7_获得了锁.
        //        Thread_Thread-8_执行了
        //        Thread_Thread-8_获得了锁.
        //        Thread_Thread-9_执行了
        //        Thread_Thread-9_获得了锁.
        //        Thread_Thread-2_获得了锁.
        //        Thread_Thread-1_获得了锁.
    }

}
