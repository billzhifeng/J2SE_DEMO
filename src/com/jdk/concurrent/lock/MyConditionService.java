package com.jdk.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyConditionService {

    private Lock lock = new ReentrantLock();

    public void testMethod() {
        lock.lock();
        for (int i = 0; i < 5; i++) {
            System.out.println("ThreadName = " + Thread.currentThread().getName() + (" " + (i + 1)));
        }
        lock.unlock();
    }

    public static void main(String[] args) {

        MyConditionService service = new MyConditionService();
        new Thread(service::testMethod).start();
        new Thread(service::testMethod).start();
        new Thread(service::testMethod).start();
        new Thread(service::testMethod).start();
        new Thread(service::testMethod).start();

        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //        结果太长就不放出来，具体可以看我源码。总之，就是每个线程的打印1-5都是同步进行，顺序没有乱。
        //        通过下面的例子，可以看出Lock对象加锁的时候也是一个对象锁，持续对象监视器的线程才能执行同步代码，其他线程只能等待该线程释放对象监视器。
    }
}
