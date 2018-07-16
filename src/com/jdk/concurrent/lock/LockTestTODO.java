package com.jdk.concurrent.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTestTODO {

    //TODO http://blog.csdn.net/yangcheng33/article/details/47708631
    //http://blog.csdn.net/youyou1543724847/article/details/52174968

    public static void main(String[] args) throws InterruptedException {

        Thread i1 = new Thread(new RunIt3());
        Thread i2 = new Thread(new RunIt3());
        i1.start();
        Thread.sleep(100L);
        i2.start();
        Thread.sleep(200L);
        i2.interrupt();
    }
}

class RunIt3 implements Runnable {

    private static Lock lock = new ReentrantLock();

    public void run() {
        try {
            lock.lock();
            //lock.lockInterruptibly();
            //lock.tryLock();
            //lock.tryLock(5, TimeUnit.SECONDS);

            System.out.println(Thread.currentThread().getName() + " running");
            TimeUnit.SECONDS.sleep(2);
            lock.unlock();
            System.out.println(Thread.currentThread().getName() + " finished");
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + " interrupted");

        }

    }
}
