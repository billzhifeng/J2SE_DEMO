package com.jdk.concurrent.lock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * PROPRIETOR
 * 
 * @author wangzhifeng
 * @date 2018年6月25日 下午3:09:17
 */
public class ReadWriteLockDemo {
    //    ReentrantLock 实现了标准的互斥操作，也就是一次只能有一个线程持有锁，也即所谓独占锁的概念。
    //显然这个特点在一定程度上面减低了吞吐量，实际上独占锁是一种保守的锁策略，在这种情况下任何“读/读”，“写/读”，“写/写”操作都不能同时发生。
    //    但实际应用场景中我们会经常遇到这样的情况：某些资源需要并发访问，并且大部分时间是用来进行读操作的，写操作比较少，
    //而锁是有一定的开销的，当并发比较大的时候，锁的开销就比较可观了。所以如果可能的话就尽量少用锁，
    //如果非要用锁的话就尝试看能否能实现读写分离，将其改造为读写锁。
    //
    //    ReadWriteLock描述的是：一个资源能够被多个读线程访问，或者被一个写线程访问，但是不能同时存在读写线程。
    //也就是说读写锁使用的场合是一个共享资源被大量读取操作，而只有少量的写操作（修改数据）。
    //看一下JDK的API文档：
    //public interface ReadWriteLock {
    //        Lock readLock();
    //        Lock writeLock();
    //    }
    public static void main(String args[]) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        final Lock readLock = lock.readLock();
        final Lock writeLock = lock.writeLock();
        final Resource resource = new Resource();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        for (int i = 0; i < 5; ++i) {//写线程
            new Thread("写线程-" + i) {
                public void run() {
                    System.out.println("------------------------" + format.format(new Date()) + " - "
                            + Thread.currentThread().getName() + "启动完成");
                    writeLock.lock();
                    try {
                        resource.setValue(resource.getValue() + 1);
                        System.out.println("------------------------" + format.format(new Date()) + " - "
                                + Thread.currentThread().getName() + "获取了写锁，修正数据为：" + resource.getValue());
                        Thread.sleep(1000L);//休眠
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        writeLock.unlock();
                    }
                };
            }.start();
            new Thread("读线程----" + i) {
                public void run() {
                    System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName() + "启动完成");
                    readLock.lock();
                    try {
                        System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                                + "获取了读锁，读取的数据为：" + resource.getValue());
                        Thread.sleep(200L);//休眠
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        readLock.unlock();
                    }
                };
            }.start();
        }
        System.out.println("写线程启动完成------------------------");
        for (int i = 10; i < 15; ++i) {//读线程
            new Thread("读线程=" + i) {
                public void run() {
                    System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                            + "启动完成------------------------");
                    readLock.lock();
                    try {
                        System.out.println(format.format(new Date()) + " - " + Thread.currentThread().getName()
                                + "获取了读锁，读取的数据为：" + resource.getValue());
                        Thread.sleep(200L);//休眠
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        readLock.unlock();
                    }
                };
            }.start();
        }
    }
}
//打印结果如下： 结果分析，写的时候其他读写都不能做。一个资源能够被同时多个读线程访问
//写线程启动完成------------------------
//2018-06-25 15:28:38.741 - Thread-2获取了写锁，修正数据为：1
//2018-06-25 15:28:39.743 - Thread-1获取了写锁，修正数据为：2
//2018-06-25 15:28:40.743 - Thread-0获取了写锁，修正数据为：3
//2018-06-25 15:28:41.743 - Thread-4获取了写锁，修正数据为：4
//2018-06-25 15:28:42.744 - Thread-3获取了写锁，修正数据为：5
//2018-06-25 15:28:43.744 - Thread-5获取了写锁，修正数据为：6
//2018-06-25 15:28:44.745 - Thread-6获取了写锁，修正数据为：7
//2018-06-25 15:28:45.745 - Thread-7获取了写锁，修正数据为：8
//2018-06-25 15:28:46.745 - Thread-8获取了写锁，修正数据为：9
//2018-06-25 15:28:47.746 - Thread-9获取了写锁，修正数据为：10
//2018-06-25 15:28:48.746 - Thread-11获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-12获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-10获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-14获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-15获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-13获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-16获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-18获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-17获取了读锁，读取的数据为：10
//2018-06-25 15:28:48.746 - Thread-19获取了读锁，读取的数据为：10

class Resource {
    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
