package com.jdk.concurrent.semaphore.productCustomDemo;

import java.util.Date;
import java.util.concurrent.Semaphore;

//消息
class Msg {
}

/**
 * @Described 生产者消费者模型
 */
public class ProductQueue<T> {

    private final T[] items;                       //队列存储区

    private Semaphore coreSign = new Semaphore(1); //独占信号 模拟独占锁

    private Semaphore notFull;                     //队列非满信号 因队列大小未知，需要在队列构建的时候初始化

    private Semaphore notEmpty = new Semaphore(0); //队列非空信号

    private int       head, tail, count;           //下标

    @SuppressWarnings("unchecked")
    public ProductQueue(int maxSize) {
        items = (T[]) new Object[maxSize];
        notFull = new Semaphore(maxSize);///初始化信号
    }

    public ProductQueue() {
        this(10);
    }

    /**
     * 放置数据
     */
    public void put(T t) throws InterruptedException {
        notFull.acquire();//获取入队列许可
        coreSign.acquire();//模拟独占锁 独占信号
        try {
            items[tail] = t;
            if (++tail == getCapacity()) {
                tail = 0;
            }
            ++count;
        } finally {
            coreSign.release();//释放模拟信号
            notEmpty.release();//通知队列非空
        }
    }

    /**
     * 取数据
     */
    public T take() throws InterruptedException {
        notEmpty.acquire();//获取出队列许可
        coreSign.acquire();//模拟独占锁 独占信号
        try {
            T ret = items[head];
            items[head] = null;//GC
            if (++head == getCapacity()) {
                head = 0;
            }
            --count;
            return ret;
        } finally {
            coreSign.release();
            notFull.release();
        }
    }

    /**
     * 获取容量（队列）
     */
    public int getCapacity() {
        return items.length;
    }

    /**
     * 获取元素数目
     */
    public int size() throws InterruptedException {
        coreSign.acquire();
        try {
            return count;
        } finally {
            coreSign.release();
        }
    }

    //主函数
    public static void main(String[] args) {
        final ProductQueue<Object> queue = new ProductQueue<Object>(5);
        new Thread() {//消费线程 每100毫秒收一次
            public void run() {
                try {
                    for (int i = 0; i < 10; ++i) {
                        System.out.println(new Date() + " - 消费者" + i + "： 等待取数据...");
                        Object obj = queue.take();
                        System.out.println(new Date() + " - 消费者" + i + "： 取到数据" + obj);
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
        }.start();
        new Thread() {//生成线程 每10ms放进去一个数据
            public void run() {
                try {
                    for (int i = 0; i < 10; ++i) {
                        System.out.println(new Date() + " - 生产者" + i + "： 等待生产数据...");
                        Msg msg = new Msg();
                        queue.put(msg);
                        System.out.println(new Date() + " - 生产者" + i + "： 生产数据" + msg);
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            };
        }.start();
    }
}
