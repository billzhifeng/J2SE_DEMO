package com.jdk.concurrent.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author wangzhifeng
 * @date 2018年6月25日 下午3:09:17
 */
public class WriteLockGetReadLockDemo {
    //重入性
    //写线程获取写入锁后可以再次获取读取锁，但是读线程获取读取锁后却不能获取写入锁
    //读写锁允许读线程和写线程按照请求锁的顺序重新获取读取锁或者写入锁。当然了只有写线程释放了锁，读线程才能获取重入锁。

    public static void main(String args[]) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        final Lock readLock = lock.readLock();
        final Lock writeLock = lock.writeLock();
        final Resource2 resource2 = new Resource2();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        final Resource resource = new Resource();
        //写线程
        new Thread("写线程B") {
            public void run() {
                System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName() + "启动");

                writeLock.lock();
                try {
                    resource.setValue(resource.getValue() + 1);
                    System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                            + "获取了写锁，修正数据为：" + resource.getValue());

                    readLock.lock();
                    try {
                        System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                                + "获取了读锁，读取的数据为：" + resource.getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        readLock.unlock();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                    writeLock.unlock();
                }
            };
        }.start();

        try {
            Thread.sleep(20L);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        new Thread("读线程A") {
            public void run() {
                System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName() + "启动");
                readLock.lock();
                try {
                    System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                            + "获取了读锁，读取的数据为：" + resource2.getValue() + ",尝试获取写锁...");
                    writeLock.lock();
                    resource2.setValue(resource2.getValue() + 1);
                    System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                            + "获取了写锁，修正数据为：" + resource2.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    writeLock.unlock();
                    readLock.unlock();
                }
            };
        }.start();
    }
}
//打印结果如下： 结果分析，写的时候其他读写都不能做。一个资源能够被同时多个读线程访问
//2018-06-25 16:35:50.120 - 写线程B启动
//2018-06-25 16:35:50.121 - 写线程B获取了写锁，修正数据为：1
//2018-06-25 16:35:50.121 - 写线程B获取了读锁，读取的数据为：1
//2018-06-25 16:35:50.140 - 读线程A启动
//2018-06-25 16:35:50.141 - 读线程A获取了读锁，读取的数据为：0,尝试获取写锁...  发现一直不成功

class Resource2 {
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
